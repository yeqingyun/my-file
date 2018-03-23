package com.gionee.gnif.file.constant;

import com.gionee.gnif.file.biz.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;

public class FileSysOuterConfig {

    @Autowired
    private PropertiesConfig propertiesConfig;

    public void setFileService(FileService fileService) {
        this.propertiesConfig.setFileService(fileService);
    }

    public void setRootPath(String rootPath) {
        this.propertiesConfig.setRootPath(rootPath);
    }

    public void setSizeThreshold(Integer sizeThreshold) {
        this.propertiesConfig.setSizeThreshold(sizeThreshold);
    }

    public void setFileSizeMax(Long fileSizeMax) {
        this.propertiesConfig.setFileSizeMax(fileSizeMax);
    }

    public void setRequestSizeMax(Long requestSizeMax) {
        this.propertiesConfig.setRequestSizeMax(requestSizeMax);
    }

    public void setEncoding(String encoding) {
        this.propertiesConfig.setEncoding(encoding);
    }

}
