package cinebox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/screen/**").permitAll() // 상영 정보 API 인증 없이 허용
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.disable()); // 세션 사용하지 않음

        return http.build();
    }
}
