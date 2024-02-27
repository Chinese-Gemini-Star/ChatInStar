package site.geministar.chatinstar.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reply {
    /**
     * 状态码
     * 0: 错误， 1： 成功
     */
    private int  status;
    /**
     * 错误信息
     */
    private String message;
}
