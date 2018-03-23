package com.gionee.gnif.file.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

	public static Map<String, Object> extractMap(HttpServletRequest request) {
		Map<String,Object> paramsMap = new HashMap<String, Object>(); 
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			Object value = request.getParameter(key);
			paramsMap.put(key, value);
		}
		return paramsMap;
	}
	
}
