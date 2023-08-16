package com.mybox.file.model;

import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

import static com.mybox.file.component.UploadFileUtils.getFileType;

@Builder
@Data
public class UploadFile {

	private String originalFilename;
	private String storedFilename;
	private String contentType;

	// 용량, 기타 등등

	public static UploadFile of(Path path) {
		return UploadFile.builder()
				.originalFilename(path.toFile().getName())
				.contentType(getFileType(path))
				.build();
	}
}
