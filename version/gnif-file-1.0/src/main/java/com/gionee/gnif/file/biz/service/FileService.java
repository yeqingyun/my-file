package com.gionee.gnif.file.biz.service;

import com.gionee.gnif.file.web.message.Message;

import javax.servlet.http.HttpServletRequest;

public interface FileService {

    Message fileExist(HttpServletRequest request) throws Exception;

    Message chunkFileExist(HttpServletRequest request) throws Exception;

    Message chunkFileUpload(HttpServletRequest request) throws Exception;

    Message mergeChunkFile(HttpServletRequest request) throws Exception;

}
