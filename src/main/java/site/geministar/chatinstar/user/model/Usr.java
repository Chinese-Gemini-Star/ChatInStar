package site.geministar.chatinstar.user.model;

import com.tangzc.mpe.autotable.annotation.Ignore;
import com.tangzc.mpe.autotable.annotation.NotNull;
import com.tangzc.mpe.autotable.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户模型
 *
 * @author Geministar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Usr {
    /**
     * 用户Id
     */
    @NotNull
    private String id;

    /**
     *  用户密码
     */
    @NotNull
    private String password;

    /**
     * 用户名
     */
    @NotNull
    private String name;

    /**
     * 图标颜色
     */
    @NotNull
    private String color;

    /**
     * 身份认证
     */
    @Ignore
    private String token;

    public Usr(String id, String password, String name, String color) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.color = color;
    }
}
