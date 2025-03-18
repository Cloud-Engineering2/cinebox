package cinebox.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import cinebox.common.utils.CookieUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CookieConfig {
	@Value("${domain}")
	private String domain;
	
	@PostConstruct
	public void init() {
		CookieUtil.DOMAIN = domain;
		log.info("CookieUtil.DOMAIN initialized to: {}", domain);
	}
}
