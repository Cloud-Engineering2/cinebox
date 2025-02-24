package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;
import cinebox.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUserId(Long userId);
	boolean existsByIdentifier(String identifier);
	User findByIdentifier(String identifier);

}
