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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	public String uploadFile(MovieRequest request, MultipartFile image) {
		if (image == null) {
			log.info("이미지가 첨부되지 않음");
			return null;
		}
		
		String fileName = generateFileName(request);
		log.info("S3 파일 업로드 시작: fileName={}", fileName);
		
		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(image.getContentType());
			metadata.setContentLength(image.getSize());
			
			amazonS3.putObject(bucketName, fileName, image.getInputStream(), metadata);
			
			String url = amazonS3.getUrl(bucketName, fileName).toString();
			log.info("S3 파일 업로드 완료: url={}", url);
			return url;
		} catch (IOException e) {
			log.error("S3 파일 업로드 실패: {}", e.getMessage());
			throw S3ServerException.EXCEPTION;
		}
	}

	private String generateFileName(MovieRequest request) {
		String folderName = "posters/";
		String title = request.title().replaceAll("[^가-힣a-zA-Z0-9]", "_");
		String releaseDate = request.releaseDate().toString();
		String uuid = java.util.UUID.randomUUID().toString();
		return folderName + title + "/" + releaseDate + "_" + uuid;
	}
}
