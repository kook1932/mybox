package com.mybox.file.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api")
@RestController
public class FileController {

	@Value("${file.upload-dir}")
	private String uploadDir;

	/**
	 * Upload Root 폴더 하위 디렉토리의 파일 추출
	 * @return
	 */
	@GetMapping("/file")
	public ResponseEntity<Map<String, Object>> getFiles(@RequestParam(defaultValue = "", required = false) String path) {
		return ResponseEntity.ok(getUploadFiles(path));
	}

	private Map<String, Object> getUploadFiles(String subPath) {
		Map<String, Object> responseBody = new HashMap<>();
		final String targetPath = StringUtils.cleanPath(uploadDir + subPath);

		try {
			List<Map<String, String>> filePaths = Files.walk(Paths.get(targetPath), 1)
					.filter(path -> !path.equals(Paths.get(targetPath)))
					.map(FileController::getFileInfo)
					.collect(Collectors.toList());

			responseBody.put("result", "success");
			responseBody.put("files", filePaths);
			responseBody.put("path", targetPath);
		} catch (IOException e) {
			log.error("Not Found Files : {}", e.getMessage());
			responseBody.put("result", "fail");
		}

		return responseBody;
	}

	private static Map<String, String> getFileInfo(Path path) {
		Map<String, String> fileInfo = new HashMap<>();
		fileInfo.put("fileName", path.toFile().getName());
		fileInfo.put("contentType", getContentType(path));
		return fileInfo;
	}

	private static String getContentType(Path path) {
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

	@PostMapping("/file")
	public ResponseEntity<String> saveUploadFiles(@RequestParam List<MultipartFile> files,
												  @RequestParam String savePath) {
		if (files.isEmpty()) {
			return new ResponseEntity<>("file upload Fail : Empty File", new HttpHeaders(), HttpStatus.BAD_REQUEST);
		}

		files.forEach(file -> saveMultipartFile(uploadDir + savePath, file));
		return ResponseEntity.ok("file upload success");
	}

	private static void saveMultipartFile(String finalPath, MultipartFile file) {
		try (InputStream is = file.getInputStream()) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Path targetLocation = Paths.get(finalPath).resolve(fileName);

			File targetDirectory = new File(targetLocation.toUri()).getParentFile();
			if (!targetDirectory.exists()) {
				if (targetDirectory.mkdirs()) {
					log.error("Target directory created.");
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

	@GetMapping("/file/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(filename);
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
						.body(resource);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.notFound().build();
	}
}
