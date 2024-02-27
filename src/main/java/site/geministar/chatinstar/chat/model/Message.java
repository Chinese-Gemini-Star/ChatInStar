package site.geministar.chatinstar.chat.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tangzc.mpe.autotable.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Message {
    /**
     * 聊天消息id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    @NotNull
    private String userid;
    /**
     * 用户名
     */
    @Ignore
    private String username;
    /**
     * 用户图标颜色
     */
    @Ignore
    private String color;
    /**
     * 消息发送时间
     */
    @NotNull
    private String time;
    /**
     * 聊天消息内容
     */
    @NotNull
    private String content;
}
