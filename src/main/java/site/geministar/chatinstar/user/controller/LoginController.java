package site.geministar.chatinstar.user.controller;

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
                return new Reply(1, JWTUtil.create(map));
            } else {
                return new Reply(0,"用户ID或密码错误");
            }
        } else {
            return new Reply(0, "用户ID或密码错误");
        }
    }
}
