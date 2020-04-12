package code.boot.img.upload.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import code.boot.img.upload.entity.UserProfile;
import code.boot.img.upload.repository.UserProfileDataService;
import code.boot.img.upload.s3bucket.S3BucketName;

@Service
public class UserProfileService {

	private final UserProfileDataService userProfileDataService;
	private final StoreFile storeFile;

	@Autowired
	public UserProfileService(UserProfileDataService userProfileDataService, StoreFile storeFile) {
		this.userProfileDataService = userProfileDataService;
		this.storeFile = storeFile;
	}

	public List<UserProfile> getUserProfile() {
		return userProfileDataService.getUserProfiles();
	}

	public void uploadProfileImage(UUID userProfileId, MultipartFile file) {

		isFileEmpty(file);

		isFileImageOrNot(file);

		UserProfile user = getUserProfileInfo(userProfileId);

		Map<String, String> metadata = extractMetadata(file);

		String path = String.format("%s/%s", S3BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
		String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

		try {
			storeFile.save(path, fileName, Optional.of(metadata), file.getInputStream());
			user.setProfileImageLink(fileName);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public byte[] downloadProfileImage(UUID userProfileId) {
		UserProfile user = getUserProfileInfo(userProfileId);

		String path = String.format("%s/%s", S3BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());

		return user.getProfileImageLink().map(key -> storeFile.download(path, key)).orElse(new byte[0]);

	}

	private Map<String, String> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}

	private UserProfile getUserProfileInfo(UUID userProfileId) {
		return userProfileDataService.getUserProfiles().stream()
				.filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId)).findFirst().orElseThrow(
						() -> new IllegalStateException(String.format("User profile %s not found", userProfileId)));
	}

	private void isFileImageOrNot(MultipartFile file) {
		if (!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_PNG.getMimeType())
				.contains(file.getContentType())) {
			throw new IllegalStateException("File must be an image " + file.getContentType());
		}

	}

	private void isFileEmpty(MultipartFile file) {
		if (file.isEmpty()) {
			throw new IllegalStateException("File not found " + file);
		}

	}

}
