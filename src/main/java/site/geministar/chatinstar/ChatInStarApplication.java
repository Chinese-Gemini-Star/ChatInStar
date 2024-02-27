package site.geministar.chatinstar;

import com.tangzc.mpe.autotable.EnableAutoTable;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import toolgood.words.StringSearch;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableAutoTable
@MapperScan("site.geministar.chatinstar.until.mapper")
public class ChatInStarApplication {
	// 应用级bean注册
	/**
	 *  敏感词检测bean
	 *	词库由配置文件中string.search.keyWordPath指定,填写相对resources的路径
	 *
	 * @return 敏感词检测bean
	 * @throws IOException IO异常
	 */
	@Bean
	public StringSearch search(@Value("${string.search.keyWordPath}") @DefaultValue("sensi_words.txt") String path) throws IOException {
		StringSearch search = new StringSearch();
		List<String> keyWords = new LinkedList<>();
		// 从本地读取敏感词
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource(path).getInputStream()));
		while(true) {
			var word = reader.readLine();
			if(word == null) {
				break;
			}
			keyWords.add(word);
		}
		reader.close();

		// 绑定敏感词
		search.SetKeywords(keyWords);
		return search;
	}

	// 启动程序
	public static void main(String[] args) {
		SpringApplication.run(ChatInStarApplication.class, args);
	}
}
