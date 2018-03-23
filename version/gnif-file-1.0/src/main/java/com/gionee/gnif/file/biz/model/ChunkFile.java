package com.gionee.gnif.file.biz.model;

import java.io.Serializable;

public class ChunkFile implements Serializable {

    private static final long serialVersionUID = 4260551906910928011L;

    private byte[] bytes;
    private String fileMd5;
    private String chunkname;
    private String filename;
    private String chunksize;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getChunkname() {
        return chunkname;
    }

    public void setChunkname(String chunkname) {
        this.chunkname = chunkname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getChunksize() {
        return chunksize;
    }

    public void setChunksize(String chunksize) {
        this.chunksize = chunksize;
    }


}
