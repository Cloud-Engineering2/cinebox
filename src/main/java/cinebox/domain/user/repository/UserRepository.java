package cinebox.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cinebox.common.enums.PlatformType;
import cinebox.domain.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByIdentifier(String identifier);

	@Query(value = "SELECT * FROM user WHERE user_id = :userId", nativeQuery = true)
	Optional<User> findByIdIncludingDeleted(@Param("userId") Long userId);

	@Query(value = "SELECT * FROM user WHERE user_id = :userId AND is_deleted = true", nativeQuery = true)
	Optional<User> findDeletedByUserId(@Param("userId") Long userId);

	boolean existsByIdentifier(String identifier);
	boolean existsByEmail(String email);
	boolean existsByPhone(String phone);

	Optional<User> findByEmailAndPlatformType(String email, PlatformType kakao);

	boolean existsByIdentifierAndPlatformType(String identifier, PlatformType local);

	boolean existsByEmailAndPlatformType(String email, PlatformType local);

	boolean existsByPhoneAndPlatformType(String phone, PlatformType local);

	Optional<User> findByIdentifierAndPlatformType(String username, PlatformType local);

	// 수정 시 본인의 값은 제외하고 중복값 조회
	boolean existsByEmailAndUserIdNotAndPlatformType(String email, Long userId, PlatformType platformType);
	boolean existsByPhoneAndUserIdAndPlatformType(String phone, Long userId, PlatformType platformType);
}
