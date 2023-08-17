package com.mybox.file.component;

import com.mybox.file.model.UploadFile;
import com.mybox.file.model.response.FilesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
@Profile("local")
@Component
public class UploadFileUtilsLocalImpl implements UploadFileUtils {

	@Value("${file.upload-dir}")
	private String uploadDir;

	public FilesResponse getFiles(String subPath) {
		String currentPath = StringUtils.cleanPath(uploadDir + subPath);
		return new FilesResponse(subPath, getUploadFiles(currentPath));
	}

	private List<UploadFile> getUploadFiles(String fullPath) {
		List<UploadFile> uploadFiles = new ArrayList<>();
		try {
			uploadFiles = getFilesOnPath(fullPath);
		} catch (IOException e) {
			log.error("Not Found Files : {}", e.getMessage());
		}
		return uploadFiles;
	}

	private List<UploadFile> getFilesOnPath(String fullPath) throws IOException {
		return Files.walk(Paths.get(fullPath), 1)
				.filter(path -> !path.equals(Paths.get(fullPath)))
				.map(UploadFile::of)
				.collect(Collectors.toList());
	}

	public void saveFiles(String savePath, List<MultipartFile> files) {
		files.forEach(file -> saveFile(savePath, file));
	}

	private void saveFile(String path, MultipartFile file) {
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

}
