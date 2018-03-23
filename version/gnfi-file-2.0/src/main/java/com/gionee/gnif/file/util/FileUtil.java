package com.gionee.gnif.file.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import com.gionee.gnif.core.GnifException;

public class FileUtil {

	public static void makeDir(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
		}
	}
	
	public static Boolean exist(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		}
		return true;
	}
	
	
	public static String getFileMd5(String filePath) {
		File file = new File(filePath);
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			ByteBuffer buf = ByteBuffer.allocate(1024*10);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			try {
				while(raf.getChannel().read(buf)>0){
					buf.flip();
					md5.update(buf);
					buf.rewind();
				}
			} finally{
				raf.close();
			}
		} catch (Exception e) {
			throw new GnifException(e.getMessage());
		}
		String md5Str = MD5Util.byteHEX(md5.digest());
		return md5Str;
	}
	
	
	public static void main(String[] args) throws Exception{
//		GioneePasswordEncoder encoder = new GioneePasswordEncoder();
//		File file = new File(filepath);
//		ByteBuffer buf = ByteBuffer.allocate(16);
//		RandomAccessFile raf = new RandomAccessFile(file, "r");
//		try {
//			while(raf.getChannel().read(buf)>0){
//				buf.flip();
//				byte[] bytes = buf.array();
//				encoder.md5Update(bytes,bytes.length);
//				buf.rewind();
//			}
//		} finally{
//			raf.close();
//		}
//		String md5Str = encoder.getMD5();
//		System.out.println(md5Str);
		
		
//		System.out.println("175432a52eaf7ba67b288ef6148a2a08");
//		System.out.println("04427f4c324476f347fcf9e26fb3f8fd");
		System.out.println("6767d6144ddd0879cce99414da3a29a4");
	}
	
}
