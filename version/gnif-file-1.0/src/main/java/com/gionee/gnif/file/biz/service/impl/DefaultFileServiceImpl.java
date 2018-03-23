package com.gionee.gnif.file.biz.service.impl;

import com.gionee.gnif.file.biz.service.FileService;
import com.gionee.gnif.file.constant.PropertiesConfig;
import com.gionee.gnif.file.util.FileUtil;
import com.gionee.gnif.file.web.message.Message;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class DefaultFileServiceImpl implements FileService {

    @Autowired
    private PropertiesConfig propertyConfig;

    @Override
    public Message fileExist(HttpServletRequest request) throws Exception {
        Message message = new Message();
        String fileMd5 = request.getParameter("fileMd5");
        String fileName = request.getParameter("fileName");
//		String fileSize = request.getParameter("fileSize");  //暂时不用，后续优化，可能会用
        if (fileMd5 == null || fileName == null) {
            throw new Exception("文件参数有误");
        } else {
            String value = propertyConfig.getProperty(fileMd5);
            if (value == null) {
                message.setIsSuccess(true);
                message.setIsOk(false);
                message.setMessage(fileName + " 文件不存在");
            } else {
                if (!value.contains(fileName)) {
                    propertyConfig.addProperty(fileMd5, fileName);
                }
                message.setIsSuccess(true);
                message.setIsOk(true);
                message.setMessage(fileName + " 文件存在");
            }
        }
        return message;
    }

    @Override
    public Message chunkFileExist(HttpServletRequest request) throws Exception {
        Message message = new Message();
        String fileName = request.getParameter("fileName");
        String fileMd5 = request.getParameter("fileMd5");
//		String fileSize = request.getParameter("fileSize");  //暂时不用，后续优化，可能会用
        String chunkOrder = request.getParameter("chunkOrder") == null ? null : request.getParameter("chunkOrder");
        long chunkSize = request.getParameter("chunkSize") == null ? 0l : Long.parseLong(request.getParameter("chunkSize"));

        String base = propertyConfig.getRootPath();
        if (fileName == null || fileMd5 == null || chunkSize == 0l || chunkOrder == null) {
            throw new Exception("文件参数有误");
        } else {
            String filePath = base + PropertiesConfig.fileSeparator + fileMd5;
            if (!FileUtil.exist(filePath)) {
                message.setIsSuccess(true);
                message.setIsOk(false);
                message.setMessage(fileName + " 文件 " + chunkOrder + "还没开始上传");
            } else {
                String cacheFilePath = base + PropertiesConfig.fileSeparator + fileMd5 + PropertiesConfig.fileSeparator + chunkOrder;
                if (!FileUtil.exist(cacheFilePath)) {
                    message.setIsSuccess(true);
                    message.setIsOk(false);
                    message.setMessage(fileName + " 文件 " + chunkOrder + "还没开始上传");
                } else {
                    File file = new File(cacheFilePath);
                    long currentLength = file.length();
                    if (currentLength == chunkSize) {
                        message.setIsSuccess(true);
                        message.setIsOk(true);
                        message.setMessage("文件已存在，不需再次上传");
                    } else {
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

    @SuppressWarnings("unused")
    @Override
    public Message chunkFileUpload(HttpServletRequest request) throws Exception {
        Message message = new Message();
        String fileMd5 = null;
        String chunkname = null;
        String filename = null;
        FileItem item = null;
        MultipartFile multipartFile = null;
        File fileDir = null;
        String base = propertyConfig.getRootPath();
        File file = new File(base);
        if (request instanceof DefaultMultipartHttpServletRequest) {
            Map<String, MultipartFile> MultipartFileMap = ((DefaultMultipartHttpServletRequest) request).getFileMap();
            for (Map.Entry<String, MultipartFile> entry : MultipartFileMap.entrySet()) {
                multipartFile = entry.getValue();
            }
            fileMd5 = ((DefaultMultipartHttpServletRequest) request).getParameter("fileMd5");
            chunkname = ((DefaultMultipartHttpServletRequest) request).getParameter("chunk");
            filename = ((DefaultMultipartHttpServletRequest) request).getParameter("name");

        } else {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置内存缓冲区，超过后写入临时文件
            factory.setSizeThreshold(propertyConfig.getSizeThreshold());
            // 设置临时文件存储位置
            factory.setRepository(file);
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置单个文件的最大上传值
            upload.setFileSizeMax(propertyConfig.getFileSizeMax());
            // 设置整个request的最大值
            upload.setSizeMax(propertyConfig.getRequestSizeMax());
            upload.setHeaderEncoding(propertyConfig.getEncoding());

            @SuppressWarnings("unchecked")
            List<FileItem> items = upload.parseRequest(request);
            for (FileItem tempItem : items) {
                if (tempItem.isFormField()) {
                    String fieldName = tempItem.getFieldName();
                    if (fieldName.equals("fileMd5")) {
                        fileMd5 = tempItem.getString(propertyConfig.getEncoding());
                    }
                    if (fieldName.equals("chunk")) {
                        chunkname = tempItem.getString(propertyConfig.getEncoding());
                    }
                    if (fieldName.equals("name")) {
                        filename = tempItem.getString(propertyConfig.getEncoding());
                    }
                } else {
                    item = tempItem;
                }
            }
        }

        if (fileMd5 != null && (item != null || multipartFile != null) && chunkname != null) {
            fileDir = new File(base + File.separator + fileMd5);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            File chunkFile = new File(base + File.separator + fileMd5 + File.separator + chunkname);
            if (item != null) {
                item.write(chunkFile);
            } else if (multipartFile != null) {
                multipartFile.transferTo(chunkFile);
            }
            message.setIsSuccess(true);
            message.setIsOk(true);
            message.setMessage("上传成功");
        } else {
            message.setIsSuccess(true);
            message.setIsOk(false);
            message.setMessage("上传失败，文件内容为空");
        }
        return message;
    }

	/*@Override
    public Message mergeChunkFile(HttpServletRequest request) throws exception {
		Message message = new Message();
		String fileName = request.getParameter("fileName");
		String fileMd5 = request.getParameter("fileMd5");
//		String fileSize = request.getParameter("fileSize");  //暂时不用，后续优化，可能会用
		String base = propertyConfig.getRootPath();
		if(fileName==null||fileMd5==null){
			throw new exception("上传文件名参数空");
		}else{
			String targetFilePath = base+PropertiesConfig.fileSeparator+fileName;
			File targetFile = new File(targetFilePath);

			String srcDirFilePath = base+PropertiesConfig.fileSeparator+fileMd5;
			File srcDirFile = new File(srcDirFilePath);
			if(srcDirFile.isDirectory()&&srcDirFile.exists()){
				RandomAccessFile wraf = new RandomAccessFile(targetFile, "rw");
				int count = 0;

					File[] files = srcDirFile.listFiles();
                long start = System.currentTimeMillis();

                if(files.length==1){
                        files[0].renameTo(targetFile);
                }else{
                    try {
                        Arrays.sort(files, new Comparator<File>() {
                            public int compare(File o1, File o2) {
                                long o1Index = Long.parseLong(o1.getName());
                                long o2Index = Long.parseLong(o2.getName());
                                long value = o1Index - o2Index;
                                return value<0?-1:1;
                            }
                        });

                        for(File file : files){
                            ByteBuffer buf = ByteBuffer.allocate(1024);
                            RandomAccessFile rraf = new RandomAccessFile(file, "r");
                            try {
                                while(rraf.getChannel().read(buf)>0){
                                    buf.flip();
                                    wraf.getChannel().write(buf);
                                    buf.rewind();
                                }
                            } finally{
                                rraf.close();
                                file.delete();
                            }
                            if(count!=0&&count%10==0){
                                wraf.getChannel().force(true);
                            }
                        }
                    } finally{
                        wraf.close();
                        srcDirFile.delete();
                    }
                }

                System.out.println((System.currentTimeMillis()-start)/ 1000);

				//检测MD5码是否一致
				String targetFilemd5 = FileUtil.getFileMd5(targetFilePath);
				if(!fileMd5.equalsIgnoreCase(targetFilemd5)){
					targetFile.delete();
					message.setIsSuccess(true);
					message.setIsOk(false);
					message.setMessage(fileName+"文件上传内容有误,需重新上传");
				}else{
					propertyConfig.addProperty(fileMd5, fileName);
					message.setIsSuccess(true);
					message.setIsOk(true);
					message.setMessage(fileName+" 上传成功");
				}
			}else{
				message.setIsSuccess(true);
				message.setIsOk(false);
				message.setMessage(fileMd5+"/"+fileName+"文件不存在");
			}
		}
		return message;
	}*/


    @Override
    public Message mergeChunkFile(HttpServletRequest request) throws Exception {
        Message message = new Message();
        String fileName = request.getParameter("fileName");
        String fileMd5 = request.getParameter("fileMd5");
//		String fileSize = request.getParameter("fileSize");  //暂时不用，后续优化，可能会用
        String base = propertyConfig.getRootPath();
        if (fileName == null || fileMd5 == null) {
            throw new Exception("上传文件名参数空");
        } else {
            String targetFilePath = base + PropertiesConfig.fileSeparator + fileName;
            File targetFile = new File(targetFilePath);
            String srcDirFilePath = base + PropertiesConfig.fileSeparator + fileMd5;
            File srcDirFile = new File(srcDirFilePath);
            long start = System.currentTimeMillis();
            if (srcDirFile.isDirectory() && srcDirFile.exists()) {
                int count = 0;
                FileChannel resultFileChannel = null;
                File[] files = srcDirFile.listFiles();
                long strart = System.currentTimeMillis();
                if (files.length == 1) {
                    files[0].renameTo(targetFile);
                } else {
                    try {

                        Arrays.sort(files, new Comparator<File>() {
                            public int compare(File o1, File o2) {
                                long o1Index = Long.parseLong(o1.getName());
                                long o2Index = Long.parseLong(o2.getName());
                                long value = o1Index - o2Index;
                                return value < 0 ? -1 : 1;
                            }
                        });
                        resultFileChannel = new FileOutputStream(targetFile, true).getChannel();
                        for (File file : files) {
                            FileChannel blk = new FileInputStream(file).getChannel();
                            resultFileChannel.transferFrom(blk, resultFileChannel.size(), blk.size());
                            blk.close();
                            file.delete();
                        }
                    } finally{
                        resultFileChannel.close();
                        srcDirFile.delete();
                    }
                }


                System.out.println("耗时"+(System.currentTimeMillis()-start)/1000+"秒");
                //检测MD5码是否一致
                String targetFilemd5 = FileUtil.getFileMd5(targetFilePath);
                if (!fileMd5.equalsIgnoreCase(targetFilemd5)) {
                    targetFile.delete();
                    message.setIsSuccess(true);
                    message.setIsOk(false);
                    message.setMessage(fileName + "文件上传内容有误,需重新上传");
                } else {
                    propertyConfig.addProperty(fileMd5, fileName);
                    message.setIsSuccess(true);
                    message.setIsOk(true);
                    message.setMessage(fileName + " 上传成功");
                }
            } else {
                message.setIsSuccess(true);
                message.setIsOk(false);
                message.setMessage(fileMd5 + "/" + fileName + "文件不存在");
            }
        }

        return message;
    }

}
