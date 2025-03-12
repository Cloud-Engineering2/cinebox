package cinebox.common.runner;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import cinebox.common.enums.PlatformType;
import cinebox.common.enums.Role;
import cinebox.domain.user.entity.User;
import cinebox.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInitializer implements CommandLineRunner {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		String adminIdentifier = "admin";

		// 기존에 admin 계정이 있는지 확인
		boolean adminExists = userRepository.existsByIdentifier(adminIdentifier);

		if (!adminExists) {
			// admin 계정 생성
			User adminUser = User.builder()
					.identifier(adminIdentifier)
					.password(passwordEncoder.encode("admin123"))
					.email("admin@cinebox.com")
					.name("관리자")
					.phone("010-1234-5678")
					.role(Role.ADMIN)
					.birthDate(LocalDate.parse("1900-01-01"))
					.platformType(PlatformType.LOCAL)
					.build();

			userRepository.save(adminUser);
			log.info("관리자 계정이 생성되었습니다.");
		}
	}
}
