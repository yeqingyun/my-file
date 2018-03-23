package com.gionee.gnif.file.biz.service.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.gionee.gnif.core.GnifException;
import com.gionee.gnif.file.biz.service.FileService;
import com.gionee.gnif.file.constant.PropertiesConfig;
import com.gionee.gnif.file.util.FileUtil;
import com.gionee.gnif.file.web.message.Message;

@Service
public class DefaultFileServiceImpl implements FileService {

	@Autowired
	private PropertiesConfig propertyConfig;
	
	@Override
	public Message fileExist(Map<String,Object> paramMaps) {
		Message message = new Message();
		String fileMd5 = (String) paramMaps.get("fileMd5");
		String fileName = (String) paramMaps.get("fileName");
		if (fileMd5==null||fileName==null) {
			throw new GnifException(JSON.toJSONString(new Message(false, "文件参数有误")));
		}
		else {
			String value = propertyConfig.getProperty(fileMd5);
			if (value==null){
				message.setIsSuccess(true);
				message.setIsOk(false);
				message.setMessage(fileName+" 文件不存在");
			}
			else {
				if (!value.contains(fileName)) {
					propertyConfig.addProperty(fileMd5, fileName);
				}
				message.setIsSuccess(true);
				message.setIsOk(true);
				message.setMessage(fileName+" 文件存在");
			}
		}
		return message;
	}

	@Override
	public Message chunkFileExist(Map<String,Object> paramMaps) {
		Message message = new Message();
		String fileName = (String) paramMaps.get("fileName");
		String fileMd5 = (String) paramMaps.get("fileMd5");
		String chunkOrder = (String) (paramMaps.get("chunkOrder")==null?null:paramMaps.get("chunkOrder"));
		long chunkSize = paramMaps.get("chunkSize")==null?0l:Long.parseLong((String) paramMaps.get("chunkSize"));
		String base = propertyConfig.getRootPath();
		if (fileName==null||fileMd5==null||chunkSize==0l||chunkOrder==null) {
			throw new GnifException(JSON.toJSONString(new Message(false, "文件参数有误")));
		}
		else {
			String filePath = base+PropertiesConfig.fileSeparator+fileMd5;
			if (!FileUtil.exist(filePath)) {
				message.setIsSuccess(true);
				message.setIsOk(false);
				message.setMessage(fileName+" 文件 "+chunkOrder+"还没开始上传");
			}
			else {
				String cacheFilePath = base+PropertiesConfig.fileSeparator+fileMd5+PropertiesConfig.fileSeparator+chunkOrder;
				if (!FileUtil.exist(cacheFilePath)) {
					message.setIsSuccess(true);
					message.setIsOk(false);
					message.setMessage(fileName+" 文件 "+chunkOrder+"还没开始上传");
				}
				else {
					File file = new File(cacheFilePath);
					long currentLength = file.length();
					if (currentLength==chunkSize) {
						message.setIsSuccess(true);
						message.setIsOk(true);
						message.setMessage("文件已存在，不需再次上传");
					}
					else {
						file.delete();
						message.setIsSuccess(true);
						message.setIsOk(false);
						message.setMessage("文件已存在，但长度不一致，需重新上传");
					}
				}
			}
		}
		return message;
	}

	@Override
	public Message chunkFileUpload(Map<String,Object> paramMaps, MultipartFile multipartFile) {
		Message message = new Message();
		String fileMd5 = (String) paramMaps.get("fileMd5");
        String chunkname = (String) paramMaps.get("chunk");;
        String base = propertyConfig.getRootPath();
		try {
			if (fileMd5!=null&&multipartFile!=null&&chunkname!=null) {
				File fileDir = new File(base+File.separator+fileMd5);  
			    if (!fileDir.exists()) {  
			    	fileDir.mkdir();  
			    }  
			    File chunkFile = new File(base+File.separator+fileMd5+File.separator+chunkname);  
			    multipartFile.transferTo(chunkFile);
			    message.setIsSuccess(true);
				message.setIsOk(true);
				message.setMessage("上传成功");
			} 
			else {
				throw new GnifException(JSON.toJSONString(new Message(true, "上传数据为空",false)));
			}
		} 
		catch (Exception e) {
			throw new GnifException(JSON.toJSONString(new Message(false, e.getMessage())));
		}
		return message;
	}

	@Override
	public Message mergeChunkFile(Map<String,Object> paramMaps) {
		Message message = new Message();
		String fileName = (String) paramMaps.get("fileName");
		String fileMd5 = (String) paramMaps.get("fileMd5");
		String base = propertyConfig.getRootPath();
		if (fileName==null||fileMd5==null) {
			throw new GnifException(JSON.toJSONString(new Message(false, "文件参数有误")));
		}
		else {
			String targetFilePath = base+PropertiesConfig.fileSeparator+fileName;
			File targetFile = new File(targetFilePath);
			String srcDirFilePath = base+PropertiesConfig.fileSeparator+fileMd5;
			File srcDirFile = new File(srcDirFilePath);
			try {
				if (srcDirFile.isDirectory()&&srcDirFile.exists()) {
					RandomAccessFile wraf = new RandomAccessFile(targetFile, "rw");
					int count = 0;
					try {
						File[] files = srcDirFile.listFiles();
						Arrays.sort(files, new Comparator<File>() {
							public int compare(File o1, File o2) {
								long o1Index = Long.parseLong(o1.getName());
								long o2Index = Long.parseLong(o2.getName());
								long value = o1Index - o2Index;
								return value<0?-1:1;
							}
						});
						
						for (File file : files) {
							ByteBuffer buf = ByteBuffer.allocate(1024);
							RandomAccessFile rraf = new RandomAccessFile(file, "r");
							try {
								while (rraf.getChannel().read(buf)>0) {
									buf.flip();
									wraf.getChannel().write(buf);
									buf.rewind();
								}
							} 
							finally {
								rraf.close();
								file.delete();
							}
							if (count!=0&&count%10==0) {
								wraf.getChannel().force(true);
							}
						}
					} finally {
						wraf.close();
						srcDirFile.delete();
					}
					//检测MD5码是否一致
					String targetFilemd5 = FileUtil.getFileMd5(targetFilePath);
					if (!fileMd5.equalsIgnoreCase(targetFilemd5)) {
						targetFile.delete();
						message.setIsSuccess(true);
						message.setIsOk(false);
						message.setMessage(fileName+"文件上传内容有误,需重新上传");
					}
					else {
						propertyConfig.addProperty(fileMd5, fileName);
						message.setIsSuccess(true);
						message.setIsOk(true);
						message.setMessage(fileName+" 上传成功");
					}
				}
				else {
					message.setIsSuccess(true);
					message.setIsOk(false);
					message.setMessage(fileMd5+"/"+fileName+"文件不存在");
				}
			} catch (Exception e) {
				throw new GnifException(JSON.toJSONString(new Message(false, e.getMessage())));
			}
		}
		return message;
	}

}
