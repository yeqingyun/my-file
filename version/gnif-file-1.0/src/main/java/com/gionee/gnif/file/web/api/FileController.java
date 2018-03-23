package com.gionee.gnif.file.web.api;

import com.gionee.gnif.file.constant.PropertiesConfig;
import com.gionee.gnif.file.web.message.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("ALL")
@Controller
public class FileController {

    private final static Logger logging = Logger.getLogger(FileController.class);
    @Autowired
    private PropertiesConfig propertiesConfig;

    @RequestMapping("/fileExist.html")
    public @ResponseBody
    Message fileExist(HttpServletRequest request) {
        Message message = null;
        try {
            message = propertiesConfig.getFileService().fileExist(request);
        } catch (Exception e) {
            logging.error(e);
            message = new Message();
            message.setIsSuccess(false);
            message.setMessage(e.getMessage());
        }
        return message;
    }

    @RequestMapping("/chunkFileExist.html")
    public @ResponseBody
    Message chunkFileExist(HttpServletRequest request) {
        Message message = null;
        try {
            message = propertiesConfig.getFileService().chunkFileExist(request);
        } catch (Exception e) {
            logging.error(e);
            message = new Message();
            message.setIsSuccess(false);
            message.setMessage(e.getMessage());
        }
        return message;
    }


    @RequestMapping("/chunkFileUpload.html")
    public @ResponseBody
    Message chunkFileUpload(HttpServletRequest request) {
        Message message = null;
        try {
            message = propertiesConfig.getFileService().chunkFileUpload(request);
        } catch (Exception e) {
            logging.error(e);
            message = new Message();
            message.setIsSuccess(false);
            message.setMessage(e.getMessage());
        }
        return message;
    }

    @RequestMapping("/mergeChunkFile.html")
    public @ResponseBody
    Message mergeChunkFile(HttpServletRequest request) throws Exception {
        Message message = null;
        try {
            message = propertiesConfig.getFileService().mergeChunkFile(request);
        } catch (Exception e) {
            logging.error(e);
            message = new Message();
            message.setIsSuccess(false);
            message.setMessage(e.getMessage());
        }
        return message;
        /*File targetFile = new File("tmp"+File.separator+request.getParameter("filename"));
        MultipartFile multipartFile = null;
        FileItem item = null;
        if (request instanceof DefaultMultipartHttpServletRequest) {
            Map<String, MultipartFile> MultipartFileMap = ((DefaultMultipartHttpServletRequest) request).getFileMap();
            for (Map.Entry<String, MultipartFile> entry : MultipartFileMap.entrySet()) {
                multipartFile = entry.getValue();
            }
        }else {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置内存缓冲区，超过后写入临时文件
            factory.setSizeThreshold(10240);
            // 设置临时文件存储位置
            factory.setRepository(new File("tmp"));
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 设置单个文件的最大上传值
            upload.setFileSizeMax(10240);
            // 设置整个request的最大值
            upload.setSizeMax(10240);
            upload.setHeaderEncoding("UTF-8");

            List<FileItem> items = upload.parseRequest(request);
            for (FileItem tempItem : items) {
                if (tempItem.isFormField()) {
                    String fieldName = tempItem.getFieldName();
                } else {
                    item = tempItem;
                }
            }
        }



        if (item != null) {
            item.write(targetFile);
        } else if (multipartFile != null) {
            multipartFile.transferTo(targetFile);
        }
        return null;*/
    }
}
