package com.ueb.wms.printer.client.util;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 * 打印机服务
 * 
 * @author liangxf
 *
 */
public class PrintServiceUtil {

	public static PrintService findDefaultPrintService() {
		PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
		return ps;
	}
}
