package com.gionee.gnif.file.biz.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.gionee.gnif.file.web.message.Message;

public interface FileService {

	Message fileExist(Map<String,Object> paramMaps);

	Message chunkFileExist(Map<String,Object> paramMaps);

	Message chunkFileUpload(Map<String,Object> paramMaps, MultipartFile multipartFile);

	Message mergeChunkFile(Map<String,Object> paramMaps);

}
