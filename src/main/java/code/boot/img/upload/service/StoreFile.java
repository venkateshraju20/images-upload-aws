package code.boot.img.upload.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

@Service
public class StoreFile {

	private final AmazonS3 s3;

	@Autowired
	public StoreFile(AmazonS3 s3) {
		this.s3 = s3;
	}

	public void save(String path, String fileName, Optional<Map<String, String>> optionalMetadata,
			InputStream inputStream) {

		ObjectMetadata metadata = new ObjectMetadata();

		optionalMetadata.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(metadata::addUserMetadata);
			}
		});

		try {
			s3.putObject(path, fileName, inputStream, metadata);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException("Failed to store a file in S3", e);
		}
	}

	public byte[] download(String path, String key) {
		try {
			S3Object s3object = s3.getObject(path, key);
			return IOUtils.toByteArray(s3object.getObjectContent());
		} catch (AmazonServiceException | IOException e) {
			throw new IllegalStateException("Failed to download a file to s3", e);
		}
	}

}
