package com.mybox.file.component;

import com.mybox.file.model.UploadFile;
import com.mybox.file.model.response.FilesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UploadFileUtils {

	@Value("${file.upload-dir}")
	private String uploadDir;

	public FilesResponse getFilesAndDirectory(String subPath) {
		String currentPath = StringUtils.cleanPath(uploadDir + subPath);
		return new FilesResponse(currentPath, getUploadFiles(currentPath));
	}

	private List<UploadFile> getUploadFiles(String currentPath) {
		List<UploadFile> uploadFiles = new ArrayList<>();
		try {
			uploadFiles = getFilesOnPath(currentPath);
		} catch (IOException e) {
			log.error("Not Found Files : {}", e.getMessage());
		}
		return uploadFiles;
	}

	private List<UploadFile> getFilesOnPath(String currentPath) throws IOException {
		return Files.walk(Paths.get(currentPath), 1)
				.filter(path -> !path.equals(Paths.get(currentPath)))
				.map(UploadFile::of)
				.collect(Collectors.toList());
	}

	public void saveMultipartFiles(String savePath, List<MultipartFile> files) {
		files.forEach(file -> saveMultipartFile(savePath, file));
	}

	private void saveMultipartFile(String path, MultipartFile file) {
		try (InputStream is = file.getInputStream()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Path targetLocation = Paths.get(uploadDir + path).resolve(fileName);

			File targetDirectory = new File(targetLocation.toUri()).getParentFile();
			if (!targetDirectory.exists()) {
				if (targetDirectory.mkdirs()) {
					log.info("Target directory created.");
				} else {
					log.error("Failed to create target directory.");
				}
			}

			Files.copy(is, targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			log.error("Cannot save file : {}", e.getMessage());
			e.printStackTrace();
		}
	}

	public static String getFileType(Path path) {
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
