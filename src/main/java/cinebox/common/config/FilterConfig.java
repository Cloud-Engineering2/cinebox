package cinebox.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestMethodLoggingFilter> loggingFilter() {
        FilterRegistrationBean<RequestMethodLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestMethodLoggingFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
