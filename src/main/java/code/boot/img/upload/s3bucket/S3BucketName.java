package code.boot.img.upload.s3bucket;

public enum S3BucketName {

	PROFILE_IMAGE("boot-upload-image");

	private final String bucketName;

	private S3BucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getBucketName() {
		return bucketName;
	}

}