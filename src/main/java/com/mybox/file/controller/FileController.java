package com.mybox.file.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api")
@RestController
public class FileController {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
		model.addAttribute("files", Files.walk(Paths.get(uploadDir))
				.filter(path -> !path.equals(Paths.get(uploadDir)))
				.map(path -> path.toFile().getName())
				.collect(Collectors.toList()));
		return "uploadForm";
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
								   RedirectAttributes redirectAttributes) {
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:/";
		}

		try {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Path targetLocation = Paths.get(uploadDir).resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded " + fileName + "!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
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
