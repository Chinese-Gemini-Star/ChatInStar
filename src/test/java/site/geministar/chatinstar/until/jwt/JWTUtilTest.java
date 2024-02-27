package site.geministar.chatinstar.until.jwt;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class JWTUtilTest {
    @Test
    public void test() {
        //  创建JWT
        Map<String,String> map = new HashMap<>();
        map.put("name","geministar");
        String jwt = JWTUtil.create(map);
        System.out.println("JWT: " + jwt);

        // 解析JWT
        Map<String, String> map2 = new HashMap<>();
        JWTUtil.read(jwt).forEach((key, value) -> {
            if(!key.equals("exp")) {
                map2.put(key,value.asString());
            }
        });
        System.out.println("map2: " + map2);
        assert map.equals(map2);
    }
}
