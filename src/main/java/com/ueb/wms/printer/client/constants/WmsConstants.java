package com.ueb.wms.printer.client.constants;

import java.nio.charset.Charset;

import org.apache.http.Consts;

public class WmsConstants {

	public static final Charset UTF8 = Consts.UTF_8;

	public static final String UEB_PRINT_VIEW_Title = "PrintViewer";

	public static final String UEB_PRINT_VIEW_ICON = "resources/icon/iwct.gif";
	public static final String UEB_REPORT_LOGO = "resources/logo/logo";
	public static final String UEB_REPORT_LOGO_SUFFIX = ".jpg";
	public static final String UEB_REPORT = "/report";

	public static final String DRIVERCLASS = "driverClass";
	public static final String CONNECTIONURL = "connectionURL";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String IDLETIMEOUT = "idleTimeout";
	public static final String TIMEOUT = "timeout";
	public static final String MAXPOOLSIZE = "maxPoolSize";

	// public static final String REPORT_TEMPLATE =
	// "Document_SO_快递面单_%_%_{CarrierID}_%.jasper";
	public static final String REPORT_TEMPLATE = "Document_WAVE_波次单品面单_%_%_{CarrierID}_%.jasper";

	public static final int RESPONSE_CODE_SUCCESS = 100;
	public static final int RESPONSE_CODE_FAILED = 101;

	public static final int ONE_K = 1024;
}
