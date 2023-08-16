package com.mybox.file.model.response;

import com.mybox.file.model.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data @AllArgsConstructor
public class FilesResponse {

	private String currentPath;
	private List<UploadFile> uploadFiles = new ArrayList<>();

}
