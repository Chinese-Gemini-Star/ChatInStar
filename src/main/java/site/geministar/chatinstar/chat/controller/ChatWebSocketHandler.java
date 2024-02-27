package site.geministar.chatinstar.chat.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import site.geministar.chatinstar.chat.model.Message;
import site.geministar.chatinstar.chat.model.TransMessage;
import site.geministar.chatinstar.until.HttpUtil;
import site.geministar.chatinstar.until.jwt.JWTUtil;
import site.geministar.chatinstar.until.mapper.MessageMapper;
import toolgood.words.StringSearch;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private StringSearch search;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * session-用户id双向映射
     */
    private final BiMap<String, WebSocketSession> sessions = HashBiMap.create();
    /**
     * 前端连接
     *
     * @param session ws会话
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // 获取用户id
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        String token = HttpUtil.getURIParams(query).get("token");
        String id = JWTUtil.read(token).get("id").asString();
        String username = JWTUtil.read(token).get("name").asString();

        // 验证重复登录
        if (sessions.containsKey(id)) {
            // 关闭旧连接
            WebSocketSession old = sessions.get(id);
            old.close(new CloseStatus(3005, "此账号在其他设备登录，您已被迫下线"));
            sessions.remove(id);
        }
        sessions.put(id,session); // 保存会话

        // 发送欢迎消息
        Message message = new Message(null,"root","聊天室管理员","#66CCFF", LocalDateTime.now().toString(),"欢迎用户%s:%s进入聊天室".formatted(id,username));
        messageMapper.insert(message);
        sendMessageToAll(new TransMessage<Message>(0,message));
        log.info("用户{}:{}进入聊天室",id,username);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> msg) throws Exception {
        log.info("收到消息:{}",msg.getPayload());

        // 解析JSON,确认消息类型
        JsonNode jsonNode = objectMapper.readTree(msg.getPayload().toString());
        int type = jsonNode.get("type").asInt(-1);

        // 对不同的消息进行处理
        switch (type) {
            // 异常消息
            case -1 -> {
                log.error("异常消息:{}",msg.getPayload());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new TransMessage<>(-1, "异常消息"))));
            }
            // 用户聊天消息
            case 0 -> {
                // 解析消息
                Message message = objectMapper.readValue(jsonNode.get("content").toString(), Message.class);
                // 检测消息中是否存在敏感词
                if(search.ContainsAny(message.getContent())) {
                    log.info("消息{}包含敏感词",msg.getPayload());
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(new TransMessage<>(-1, "消息包含敏感词"))));
                } else {
                    // 保存消息
                    messageMapper.insert(message);
                    // 发送消息给所有用户
                    sendMessageToAll(new TransMessage<>(0,message));
                }
            }
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) throws Exception {

    }

    /**
     * 前端断开
     *
     * @param session ws会话
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull
    CloseStatus closeStatus) throws Exception {
        log.info("用户{}退出聊天室",sessions.inverse().get(session));
        sessions.inverse().remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送消息给所有客户端
     * @param msg
     */
    private void sendMessageToAll(@NonNull TransMessage<?> msg) {
        sessions.forEach((id,s) -> {
            try{
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
            } catch (IOException e) {
                log.error("发送消息失败:{}",e.getMessage());
            }
        });
    }
}
