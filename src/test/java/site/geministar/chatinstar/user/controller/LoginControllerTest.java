package site.geministar.chatinstar.user.controller;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import site.geministar.chatinstar.user.model.Reply;
import site.geministar.chatinstar.user.model.Usr;
import site.geministar.chatinstar.until.mapper.UsrMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@MapperScan("site.geministar.chatinstar.mapper")
public class LoginControllerTest {
    @Autowired
    private WebTestClient client;

    @Autowired
    private UsrMapper mapper;

    /**
     * 测试正常用户注册
     */
    @Test
    public void testRightRegister(){
        // 正常用户
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("114514", "12345678", "1")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(1, "注册成功"));
        // 删除该用户
        mapper.deleteById("114514");
    }

    /**
     * 测试错误用户ID注册
     */
    @Test
    public void testWrongIDRegister() {
        // 用户ID异常用户
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("你好", "12345678", "1")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(0, "用户ID只能包含字母、数字和下划线"));
    }

    /**
     * 测试错误用户密码注册
     */
    @Test
    public void testWrongPWRegister() {
        // 用户密码异常用户
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("1", "1", "1")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(0, "密码只能包含字母、数字和下划线，且长度不能小于8，不能超过16"));
    }

    /**
     *  测试错误用户名注册
     */
    @Test
    public void testWrongNameRegister() {
        // 用户名异常用户
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("1","12345678","TMD")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(0,"用户名中存在敏感词"));
    }

    /**
     * 测试重复用户注册
     */
    @Test
    public void testDuplicateRegister() {
        // 重复用户
        client.post().uri("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("admin", "12345678", "1")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(0, "用户ID已存在"));
    }

    /**
     * 测试正常用户登录
     */
    @Test
    public void testRightLogin() {
        // 正常用户
        client.post().uri("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("admin", "Dl2004061605","")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(1, "星双子"));
    }

    /**
     * 测试错误密码登录
     */
    @Test
    public void testWrongPasswordLogin() {
        // 错误密码
        client.post().uri("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("admin", "Dl20040616","")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(0, "用户ID或密码错误"));
    }

    /**
     * 测试错误用户ID登录
     */
    @Test
    public void testWrongIDLogin() {
        // 错误密码
        client.post().uri("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(new Usr("114514", "1","")), Usr.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Reply.class).isEqualTo(new Reply(0, "用户ID或密码错误"));
    }
}
