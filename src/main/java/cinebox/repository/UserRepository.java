package cinebox.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cinebox.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserId(Long userId);
	Optional<User> findByIdentifierAndIsDeletedFalse(String identifier);
	Optional<User> findByUserIdAndIsDeletedFalse(Long userId);
	Collection<User> findAllByIsDeletedFalse();
	Optional<User> findByIdentifier(String identifier);
	
	@Query(value = "SELECT * FROM user WHERE user_id = :userId", nativeQuery = true)
	Optional<User> findByIdIncludingDeleted(@Param("userId") Long userId);

	@Query(value = "SELECT * FROM user WHERE user_id = :userId AND is_deleted = true", nativeQuery = true)
	Optional<User> findDeletedByUserId(@Param("userId") Long userId);
	
	boolean existsByIdentifier(String identifier);
	boolean existsByEmail(String email);
	boolean existsByPhone(String phone);
}
