package com.bees360.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bees360.service.FileService;
import com.bees360.utils.FTPUtil;
import com.google.common.collect.Lists;

@Service("fileService")
public class FileServiceImpl implements FileService {
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	public String upload(MultipartFile file,String path) {
		String fileName = file.getOriginalFilename();
		//拓展名
		String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
		String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
		logger.info("开始上传文件，上传文件的文件名：{},上传的路径:{},新文件名:{}",fileName,path,uploadFileName);
		File fileDir = new File(path);
		if(!fileDir.exists()) {
			//对文件名可进行修改
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		//目标文件
		File targetFile = new File(path,uploadFileName);
		try {
			file.transferTo(targetFile);
			//文件上传成功
			//将targetFile上传到FTP服务器
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			//上传完成后，删除upload下面的文件
			targetFile.delete();
		} catch (IllegalStateException | IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		return targetFile.getName();
	}
}
