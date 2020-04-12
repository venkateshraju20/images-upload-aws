package code.boot.img.upload.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import code.boot.img.upload.entity.UserProfile;
import code.boot.img.upload.service.UserProfileService;

@RestController
@RequestMapping("/api/user-profile")
@CrossOrigin("*")
public class UserProfileController {

	private final UserProfileService userProfileService;

	@Autowired
	public UserProfileController(UserProfileService userProfileService) {
		super();
		this.userProfileService = userProfileService;
	}

	@GetMapping
	public List<UserProfile> getUserProfile() {
		return userProfileService.getUserProfile();
	};

	@PostMapping(path = "{userProfileId}/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void uploadProfileImage(@PathVariable("userProfileId") UUID userProfileId,
			@RequestParam("file") MultipartFile file) {
		userProfileService.uploadProfileImage(userProfileId, file);
	}

	@GetMapping("/{userProfileId}/image/download")
	public byte[] downloadProfileImage(@PathVariable("userProfileId") UUID userProfileId) {
		return userProfileService.downloadProfileImage(userProfileId);
	}
}
