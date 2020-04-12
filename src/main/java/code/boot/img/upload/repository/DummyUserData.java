package code.boot.img.upload.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import code.boot.img.upload.entity.UserProfile;

@Repository
public class DummyUserData {

	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

	static {
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "user1", null));
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "user2", null));
	}

	public List<UserProfile> getUserProfiles() {
		return USER_PROFILES;
	};
}
