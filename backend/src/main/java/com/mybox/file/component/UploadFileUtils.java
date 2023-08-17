package com.mybox.file.component;

import com.mybox.file.model.response.FilesResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface UploadFileUtils {

	FilesResponse getFiles(String subPath);

	void saveFiles(String savePath, List<MultipartFile> files);

	static String getFileType(Path path) {
		String contentType = "";
		try {
			contentType = Files.probeContentType(path);
			if (!StringUtils.hasText(contentType)) {
				contentType = checkFileIsDirectory(path);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return contentType;
	}

	private static String checkFileIsDirectory(Path path) {
		File noContentType = new File(path.toUri());
		return noContentType.isDirectory() ? "directory" : "";
	}

}
