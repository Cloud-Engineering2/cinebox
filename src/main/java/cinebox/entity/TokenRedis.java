package cinebox.entity;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "token", timeToLive = 60 * 60 * 24 * 7)
public class TokenRedis {
	@Id
	private String id;

	@Indexed
	private String accessToken;

	private String refreshToken;

	public void updateAccessToken(String newAccessToken) {
		this.accessToken = newAccessToken;
	}
}
