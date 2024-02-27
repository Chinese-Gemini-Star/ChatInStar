package site.geministar.chatinstar.chat.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import site.geministar.chatinstar.until.HttpUtil;
import site.geministar.chatinstar.until.jwt.JWTUtil;

import java.util.Map;

@Slf4j
public class ChatWebSocketHandshakeInterceptor implements HandshakeInterceptor {
    /**
     * 握手之前，验证jwt
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        String token = HttpUtil.getURIParams(query).get("token");
        try {
            JWTUtil.read(token);
        }catch (JWTVerificationException e){
            log.info("异常的jwt token, error:{}",e.toString());
            return false;
        }
        log.info("jwt token验证通过");
        return true;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, Exception exception) {

    }
}
