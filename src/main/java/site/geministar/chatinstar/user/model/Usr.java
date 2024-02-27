package site.geministar.chatinstar.user.model;

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
}
