package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.BookingSeat;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long>{
	
	//List<BookingSeat> findByScreenId(Long screenId);
	
	List<BookingSeat> findByScreen_ScreenId(Long screenId);

}