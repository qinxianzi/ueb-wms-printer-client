package com.ueb.wms.printer.client.util;

import java.util.HashMap;
import java.util.Map;

import com.ueb.wms.printer.client.constants.WmsConstants;

public class PrintUtil {

	private static Map<String, Object> imageParams;
	static {
		imageParams = new HashMap<String, Object>();
		imageParams.put("IAMGE_NAME", getReportLogoPath(0));
		imageParams.put("IAMGE_NAME1", getReportLogoPath(1));
		imageParams.put("IAMGE_NAME2", getReportLogoPath(2));
		imageParams.put("IAMGE_NAME3", getReportLogoPath(3));
		imageParams.put("IAMGE_NAME5", getReportLogoPath(5));
		imageParams.put("IAMGE_NAME6", getReportLogoPath(6));
		imageParams.put("IAMGE_NAME7", getReportLogoPath(7));
		imageParams.put("IAMGE_NAME8", getReportLogoPath(8));
		imageParams.put("IAMGE_NAME9", getReportLogoPath(9));
		imageParams.put("IAMGE_NAME10", getReportLogoPath(10));
		imageParams.put("IAMGE_NAME11", getReportLogoPath(11));
		imageParams.put("IAMGE_NAME12", getReportLogoPath(12));
		imageParams.put("IAMGE_NAME13", getReportLogoPath(13));
		imageParams.put("IAMGE_NAME14", getReportLogoPath(14));
		imageParams.put("IAMGE_NAME15", getReportLogoPath(15));
		imageParams.put("IAMGE_NAME16", getReportLogoPath(16));
	}

	public static String getReportLogoPath(int index) {
		StringBuffer path = new StringBuffer(WmsConstants.UEB_REPORT_LOGO);
		path.append(index).append(WmsConstants.UEB_REPORT_LOGO_SUFFIX);
		// String fullPath =
		// PrintUtil.class.getResource(path.toString()).getPath();

		// return fullPath.substring(1, fullPath.length());
		return path.toString();
	}

	public static Map<String, Object> getImageParams() {
		return imageParams;
	}
}
