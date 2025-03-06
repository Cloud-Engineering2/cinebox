package cinebox.domain.movie.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import cinebox.common.exception.movie.S3ServerException;
import cinebox.domain.movie.dto.MovieRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	public String uploadFile(MovieRequest request, MultipartFile image) {
		String fileName = generateFileName(request);
		
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(image.getContentType());
			metadata.setContentLength(image.getSize());
			
			amazonS3.putObject(bucketName, fileName, image.getInputStream(), metadata);
			
			return amazonS3.getUrl(bucketName, fileName).toString();
		} catch (IOException e) {
			throw S3ServerException.EXCEPTION;
		}
	}

	private String generateFileName(MovieRequest request) {
		String folderName = "posters/";
		String title = request.title().replaceAll("[^가-힣a-zA-Z0-9]", "_");
		String releaseDate = request.releaseDate().toString();
		String uuid = java.util.UUID.randomUUID().toString();
		return folderName + title + "_" + releaseDate + "_" + uuid;
	}
}
