package com.gionee.gnif.file.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

    public static void makeDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static Boolean exist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isImg(String fileName) {
        return fileName.endsWith("bmp") || fileName.endsWith("jpg")
                || fileName.endsWith("jpeg") || fileName.endsWith("png") || fileName.endsWith("gif");
    }

    public static String getFileMd5(MultipartFile file) throws Exception {
        return getFileMd5(file.getInputStream());
    }

    public static String getFileMd5(String filePath) throws NoSuchAlgorithmException, IOException {
        File file = new File(filePath);
        MessageDigest md5;
        md5 = MessageDigest.getInstance("MD5");
        ByteBuffer buf = ByteBuffer.allocate(1048576);
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        try {
            while (raf.getChannel().read(buf) > 0) {
                buf.flip();
                md5.update(buf);
                buf.rewind();
            }
            return MD5Util.byteHEX(md5.digest());
        } finally {
            buf = null;
            raf.close();
        }
        /*MessageDigest md5;
        RandomAccessFile raf = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            int bufferSize = 1024;
            long fileLength = new File(filePath).length();
            int bufferCount = 1 + (int) (fileLength / bufferSize);
            long remain = fileLength;
            raf = new RandomAccessFile(filePath, "r");
            for (int i = 0; i < bufferCount; i++) {
                md5.update(raf.getChannel().map(FileChannel.MapMode.READ_ONLY, i * bufferSize, (int) Math.min(remain, bufferSize)));
                remain -= bufferSize;
            }
        } finally {
            if (raf != null)
                raf.close();
        }

        return MD5Util.byteHEX(md5.digest());*/
    }

    public static String getFileMd5(InputStream is) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5;
        md5 = MessageDigest.getInstance("MD5");
        byte[] bytes;
        if (is.available() <= 1048576) {
            bytes = new byte[5120];
        } else if (is.available() <= 10485760) {
            bytes = new byte[51200];
        } else {
            bytes = new byte[1048576];
        }

        int i;
        try {
            while ((i = is.read(bytes)) != -1) {
                md5.update(bytes, 0, i);
            }

            return MD5Util.byteHEX(md5.digest());
        } finally {
            bytes = null;
            is.close();
        }
    }

    public static String getFileMd5(byte[] bytes, int start, int end) throws NoSuchAlgorithmException {
        MessageDigest md5;
        md5 = MessageDigest.getInstance("MD5");
        md5.update(bytes, start, end);
        return MD5Util.byteHEX(md5.digest());
    }


}
