package cinebox.domain.auth.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import cinebox.domain.auth.entity.TokenRedis;

public interface TokenRedisRepository extends CrudRepository<TokenRedis, String> {
	Optional<TokenRedis> findByAccessToken(String accessToken);
}
