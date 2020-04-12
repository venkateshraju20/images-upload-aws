package code.boot.img.upload.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import code.boot.img.upload.entity.UserProfile;

@Repository
public class UserProfileDataService {

	private final DummyUserData dataDummyData;

	@Autowired
	public UserProfileDataService(DummyUserData dataDummyData) {
		super();
		this.dataDummyData = dataDummyData;
	}

	public List<UserProfile> getUserProfiles() {
		return dataDummyData.getUserProfiles();
	}
}
