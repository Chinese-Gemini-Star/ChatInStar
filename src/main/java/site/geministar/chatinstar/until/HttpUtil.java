package site.geministar.chatinstar.until;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
    public static Map<String,String> getURIParams(String uri) {
        Map<String,String> params = new HashMap<>();

        // 解析请求参数
        Pattern pattern = Pattern.compile("([^&?=]+)=([^&?=]*)");
        Matcher matcher = pattern.matcher(uri);

        while(matcher.find()) {
            params.put(matcher.group(1), matcher.group(2));
        }

        return params;
    }
}
