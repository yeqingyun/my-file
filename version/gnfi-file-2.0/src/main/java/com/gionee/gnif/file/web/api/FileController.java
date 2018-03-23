package com.gionee.gnif.file.web.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.gionee.gnif.core.GnifException;
import com.gionee.gnif.file.constant.PropertiesConfig;
import com.gionee.gnif.file.util.RequestUtil;
import com.gionee.gnif.file.web.message.Message;

@Controller
public class FileController{
	
	@Autowired
	private PropertiesConfig propertiesConfig;
	
	@RequestMapping("/fileExist.html")
	public @ResponseBody Message fileExist(HttpServletRequest request) {
		Map<String, Object> paramsMap = RequestUtil.extractMap(request);
		Message message = propertiesConfig.getFileService().fileExist(paramsMap);
		return message;
	}

	@RequestMapping("/chunkFileExist.html")
	public @ResponseBody Message chunkFileExist(HttpServletRequest request) {
		Map<String, Object> paramsMap = RequestUtil.extractMap(request);
		Message message = propertiesConfig.getFileService().chunkFileExist(paramsMap);
		return message;
	}
	
	
	@RequestMapping("/chunkFileUpload.html")
	public @ResponseBody Message chunkFileUpload(HttpServletRequest request) {
		HttpServletRequest processedRequest = propertiesConfig.getMultipleReuqest(request);
		MultipartFile multipartFile = null;
		try {
			Map<String, MultipartFile> MultipartFileMap = ((DefaultMultipartHttpServletRequest)processedRequest).getFileMap();
			for (Map.Entry<String, MultipartFile> entry : MultipartFileMap.entrySet()) {
				 multipartFile = entry.getValue();
			}
		} 
		catch (Exception e) {
			throw new GnifException(JSON.toJSONString(new Message(false, e.getMessage())));
		}
		if (multipartFile==null) {
			throw new GnifException(JSON.toJSONString(new Message(false, "上传数据为空")));
		}
		Map<String, Object> paramsMap = RequestUtil.extractMap(processedRequest);
		Message message = propertiesConfig.getFileService().chunkFileUpload(paramsMap,multipartFile);
		return message;
	}
	
	@RequestMapping("/mergeChunkFile.html")
	public @ResponseBody Message mergeChunkFile(HttpServletRequest request) {
		Map<String, Object> paramsMap = RequestUtil.extractMap(request);
		Message message = propertiesConfig.getFileService().mergeChunkFile(paramsMap);
		return message;
	}
	
}
