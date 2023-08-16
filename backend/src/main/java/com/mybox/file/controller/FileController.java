package com.mybox.file.controller;

import com.mybox.file.model.response.FilesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.mybox.file.component.UploadFileUtils.getFilesAndDirectory;
import static com.mybox.file.component.UploadFileUtils.saveMultipartFile;

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
	public ResponseEntity<FilesResponse> getFiles(@RequestParam(defaultValue = "", required = false) String path) {
		return ResponseEntity.ok(getFilesAndDirectory(path));
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
