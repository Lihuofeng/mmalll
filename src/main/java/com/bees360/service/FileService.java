package com.bees360.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	String upload(MultipartFile file,String path);
}