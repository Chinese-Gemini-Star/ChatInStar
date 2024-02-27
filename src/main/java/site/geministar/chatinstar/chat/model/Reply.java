package site.geministar.chatinstar.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reply<T> {
    /**
     * 消息类型
     */
    private int type;
    /**
     * 消息内容
     */
    private T content;
}
