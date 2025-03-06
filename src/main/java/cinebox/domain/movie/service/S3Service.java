package cinebox.domain.movie.service;

import org.springframework.web.multipart.MultipartFile;

import cinebox.domain.movie.dto.MovieRequest;

public interface S3Service {
	String uploadFile(MovieRequest request, MultipartFile image);
}
