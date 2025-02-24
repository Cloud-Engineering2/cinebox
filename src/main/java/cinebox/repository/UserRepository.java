package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
