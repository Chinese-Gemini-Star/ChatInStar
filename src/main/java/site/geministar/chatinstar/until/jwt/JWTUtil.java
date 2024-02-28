package site.geministar.chatinstar.until.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Map;

/**
 * 生成与解析JWt
 */
@Service
public class JWTUtil {
    /**
     * 生成jwt token
     *
     * @param map 参数
     * @return jwt token
     */
    public static String create(@NonNull Map<String,String> map) {
        JWTCreator.Builder jwt = JWT.create();

        // 设置过期事件
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);//过期时间为1分钟
        jwt.withExpiresAt(calendar.getTime());

        // 绑定参数
        map.forEach(jwt::withClaim);

        return jwt.sign(Algorithm.HMAC256("chatInStar"));
    }

    /**
     * 解析jwt token
     *
     * @param jwt jwt token
     * @return jwt参数
     */
    public static Map<String, Claim> read(@NonNull String jwt) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("chatInStar")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
        return decodedJWT.getClaims();
    }
}
