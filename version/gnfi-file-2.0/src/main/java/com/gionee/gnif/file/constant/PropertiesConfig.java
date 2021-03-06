package com.gionee.gnif.file.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.gionee.gnif.core.GnifException;
import com.gionee.gnif.file.biz.service.FileService;
import com.gionee.gnif.file.biz.service.impl.DefaultFileServiceImpl;

/**
 * 过渡型的配置文件，用于向controller提供service，向service提供一些配置功能，
 * 便于在没有配置的情况下，该组件可以提供默认的配置
 * @author Administrator
 *
 */
@Component
public class PropertiesConfig implements InitializingBean,ApplicationContextAware {

	//文件分割符
	public final static String fileSeparator = File.separator;
	//默认情况下的本地的属性，用于处理文件传入记录
	public final static String md5RecordeFile = "md5RecordeFile.properties";
	private ApplicationContext applicationContext;
	private Properties props = new Properties();
	//缺省配置的存储文件得治
	private String rootPath = "tmp";
	//缺省配置下的默认缓存区的接受大小，超过了写入临时文件
	private Integer sizeThreshold = 15*1024*1024;
	//缺省配置下单个文件大小
	private Long fileSizeMax = 20*1024*1024l;
	//每个请求的大小
	private String encoding = "UTF-8";
	
	private CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
	

	@Autowired(required=true) @Qualifier(value="defaultFileServiceImpl")
	private FileService fileService;
	
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public FileService getFileService() {
		return fileService;
	}
	
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public Integer getSizeThreshold() {
		return sizeThreshold;
	}
	public void setSizeThreshold(Integer sizeThreshold) {
		this.sizeThreshold = sizeThreshold;
	}
	public Long getFileSizeMax() {
		return fileSizeMax;
	}
	public void setFileSizeMax(Long fileSizeMax) {
		this.fileSizeMax = fileSizeMax;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public HttpServletRequest getMultipleReuqest(HttpServletRequest request){
		if (!(request instanceof DefaultMultipartHttpServletRequest)){
			if(commonsMultipartResolver.isMultipart(request)){
				return commonsMultipartResolver.resolveMultipart(request);
			}
		}
		return request;
	}
	

	@Override
	public void afterPropertiesSet() {
		File fileDir = new File(rootPath);
		if(!fileDir.exists()){
			fileDir.mkdir();
		}
		File file = new File(rootPath+fileSeparator+md5RecordeFile);
		InputStream is = null;
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			is = new FileInputStream(file);
			props.load(is);
		} catch (Exception e) {
			throw new GnifException(e);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					throw new GnifException(e);
				}
			}
		}
		commonsMultipartResolver.setDefaultEncoding(encoding);
		commonsMultipartResolver.setMaxInMemorySize(sizeThreshold);
		commonsMultipartResolver.setMaxUploadSize(fileSizeMax);
		try {
			commonsMultipartResolver.setUploadTempDir(new FileSystemResource(fileDir));
		} catch (IOException e) {
			throw new GnifException(e);
		}
	}
	
	public String getProperty(String md5){
		return props.getProperty(md5);
	}
	
	public void addProperty(String md5,String fileName){
		String existMd5File = getProperty(md5);
		if(existMd5File!=null){
			existMd5File = existMd5File + " , " + fileName;
		}else{
			existMd5File = fileName;
		}
		props.setProperty(md5, existMd5File);
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(rootPath+fileSeparator+md5RecordeFile));
			props.store(os, "");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
