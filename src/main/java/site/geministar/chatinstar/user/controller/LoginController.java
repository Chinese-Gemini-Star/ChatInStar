package site.geministar.chatinstar.user.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.geministar.chatinstar.user.model.Reply;
import site.geministar.chatinstar.user.model.Usr;
import site.geministar.chatinstar.until.jwt.JWTUtil;
import site.geministar.chatinstar.until.mapper.UsrMapper;
import toolgood.words.StringSearch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  登录控制器
 *
 * @author geminiStar
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {
    @Autowired
    private StringSearch search;

    @Autowired
    private UsrMapper mapper;

    /**
     * 用户注册接口
     *
     * @return 处理结果
     */
    @PostMapping("/register")
    public Reply register(@RequestBody Usr usr) {
        log.info("注册用户信息:{}", usr);

        // 验证合法性
        Reply reply = checkLegal(usr);
        if(reply != null) return reply;

        // 检查用户ID是否已存在
        if(mapper.selectById(usr.getId()) != null) {
            return new Reply(0,"用户ID已存在");
        }

        // 加密密码
        usr.setPassword(BCrypt.hashpw(usr.getPassword(), BCrypt.gensalt()));
        // 写入数据库
        mapper.insert(usr);
        return new Reply(1,"注册成功");
    }

    /**
     * 用户登录接口
     *
     * @param usr 用户对象
     * @return 登录成功时返回用户名，否则返回错误信息
     */
    @PostMapping("/login")
    public Reply login(@RequestBody Usr usr) {
        log.info("登录用户信息:{}", usr);

        // 在数据库中查找同ID用户
        var userFound = mapper.selectById(usr.getId());
        if(userFound != null) {
            // 验证密码
            if(BCrypt.checkpw(usr.getPassword(), mapper.selectById(usr.getId()).getPassword())) {
                Map<String,String> map = new HashMap<>();
                map.put("name",userFound.getName());
                map.put("id",userFound.getId());
                map.put("color",userFound.getColor());
                return new Reply(1, JWTUtil.create(map));
            } else {
                return new Reply(0,"用户ID或密码错误");
            }
        } else {
            return new Reply(0, "用户ID或密码错误");
        }
    }

    @PostMapping("/change")
    public Reply change(@RequestBody Usr usr) {
        log.info("修改用户信息:{}",usr);

        // 验证id是否一致
        try {
            if(!Objects.equals(usr.getId(), JWTUtil.read(usr.getToken()).get("id").asString())) {
                return new Reply(0,"用户认证信息异常");
            }
        }catch (JWTVerificationException e) {
            log.info("异常的jwt token, error:{}",e.toString());
            return new Reply(0,"用户认证信息异常");
        }

        // 验证合法性
        Reply reply = checkLegal(usr);
        if(reply != null) return reply;

        // 加密密码
        usr.setPassword(BCrypt.hashpw(usr.getPassword(), BCrypt.gensalt()));

        // 改入数据库
        mapper.updateById(usr);
        return new Reply(1,"修改成功");
    }

    @PostMapping("/refreshJWT")
    public Reply refreshJWT(@RequestBody String jwt) {
        log.info("更新JWT请求:{}",jwt);
        Map<String, Claim> info;

        // 解析JWt
        try {
            info = JWTUtil.read(jwt);
        } catch (JWTVerificationException e) {
            log.info("异常的jwt token, error:{}",e.toString());
            return new Reply(0,"用户认证信息异常");
        }

        // 重新生成JWT
        Map<String,String> copy = new HashMap<>();
        info.forEach((k, v)->{
            if(k.equals("exp")) return;
            copy.put(k, v.asString());
        });

        return new Reply(1, JWTUtil.create(copy));
    }

    /**
     * 验证用户的各项信息是否合法
     *
     * @param usr
     * @return 如有异常，返回异常信息对象；如无异常，返回null
     */
    private Reply checkLegal(Usr usr) {
        if(!usr.getId().matches("^[a-zA-Z0-9_]+$")) {
            log.info("用户ID不合法：{}", usr.getName());
            return new Reply(0,"用户ID只能包含字母、数字和下划线");
        }
        if(!usr.getPassword().matches("^[a-zA-Z0-9_]{8,16}$")) {
            log.info("密码不合法：{}", usr.getPassword());
            return new Reply(0,"密码只能包含字母、数字和下划线，且长度不能小于8，不能超过16");
        }
        if(search.ContainsAny(usr.getName())) {
            log.info("用户名{}中包含敏感词：{}", usr.getName(),search.FindAll(usr.getName()));
            return new Reply(0,"用户名中存在敏感词");
        }
        if(!usr.getColor().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")) {
            log.info("图标颜色不合法：{}",usr.getColor());
            return new Reply(0,"图标颜色不合法，请使用#开头的6位或8位十六进制表示法的RGB代码");
        }
        return null;
    }
}
