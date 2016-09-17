package com.ueb.wms.printer.client.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ApiMappingUtil {

	public static String readApiMappingFile() throws IOException {
		byte[] bytes = ApiMappingUtil.readApiMappingFile2Byte();
		return new String(bytes, "UTF-8");
	}

	public static byte[] readApiMappingFile2Byte() throws IOException {
		Resource resource = new ClassPathResource("/config/api_mapping.js");
		InputStream input = resource.getInputStream();
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while (-1 != (len = input.read(b, 0, 1024))) {
				output.write(b, 0, len);
			}
			return output.toByteArray();
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}
}
