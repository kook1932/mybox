package com.mybox.file.model;

import com.mybox.file.component.UploadFileUtils;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;

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
				.contentType(UploadFileUtils.getFileType(path))
				.build();
	}
}
