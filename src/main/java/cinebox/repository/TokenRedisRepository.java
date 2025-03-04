package cinebox.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import cinebox.entity.TokenRedis;

public interface TokenRedisRepository extends CrudRepository<TokenRedis, String> {
	Optional<TokenRedis> findByAccessToken(String accessToken);
}
