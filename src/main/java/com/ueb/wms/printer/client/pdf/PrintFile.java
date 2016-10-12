package com.ueb.wms.printer.client.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PrinterName;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintFile {

	private static final Logger logger = LoggerFactory.getLogger(PrintFile.class);

	public static void printPdf(String pdfFilePath) throws Exception {
		if (StringUtils.isBlank(pdfFilePath)) {
			return;
		}
		File pdfFile = new File(pdfFilePath);
		if (!pdfFile.exists()) {
			logger.info("待打印的文件不存在，文件路径是：{}", pdfFilePath);
			throw new Exception("待打印的文件不存");
		}

		String jobName = pdfFile.getName();
		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new JobName(jobName, null));

		// 选择AUTOSENSE，即自动选择文件类型
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;

		PrintService printService = findDefaultPrintService();
		DocPrintJob printJob = printService.createPrintJob();
		DocAttributeSet das = new HashDocAttributeSet();

		// 待打印的文件流
		InputStream input = new FileInputStream(pdfFile);

		//  建立打印文件格式
		Doc doc = new SimpleDoc(input, flavor, das);

		// 打印文件
		printJob.print(doc, pras);
	}

	private static PrintService findDefaultPrintService() {
		PrintService ps = PrintServiceLookup.lookupDefaultPrintService();
		return ps;
	}

	public static void printPdf(String pdfFilePath, DocFlavor flavor, String printer) throws Exception {
		if (StringUtils.isBlank(pdfFilePath)) {
			return;
		}
		File pdfFile = new File(pdfFilePath);
		if (!pdfFile.exists()) {
			logger.info("待打印的文件不存在，文件路径是：{}", pdfFilePath);
			throw new Exception("待打印的文件不存");
		}
		String jobName = pdfFile.getName();
		int len = StringUtils.indexOf(jobName, ".");
		if (-1 != len) {
			jobName = StringUtils.substring(jobName, 0, len);
		}

		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new JobName(jobName, null));

		if (null == flavor) {
			// flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;// 这是流类型，不能打印文件
			flavor = DocFlavor.INPUT_STREAM.PDF; // 需要支持PDF文件类型的打印机，市面上普通的打印机一般不支持PDF打印
		}

		HashAttributeSet has = new HashAttributeSet();
		if (StringUtils.isNotBlank(printer)) {
			has.add(new PrinterName(printer, null));// 添加打印机名称
		}

		//  查找所有的可用打印服务
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, has);
		FileInputStream fileinput = null;
		try {
			if (null != printService && printService.length > 0) {
				PrintService defaultPrinter = printService[0];
				defaultPrinter.getName();
				fileinput = new FileInputStream(pdfFile);//  构造待打印文件流

				DocAttributeSet das = new HashDocAttributeSet();
				Doc doc = new SimpleDoc(fileinput, flavor, das);

				DocPrintJob job = defaultPrinter.createPrintJob(); // 创建打印任务
				job.print(doc, pras);//  打印文件
			}
		} finally {
			if (null != fileinput) {
				fileinput.close();
			}
		}
	}

	/**
	 * http://blog.csdn.net/u012345283/article/details/41011977
	 * 
	 * @param pdfFilePath
	 * @throws Exception
	 */
	public static void pluginPrintPdf(String pdfFilePath) throws Exception {
		if (StringUtils.isBlank(pdfFilePath)) {
			return;
		}
		File pdfFile = new File(pdfFilePath);
		if (!pdfFile.exists()) {
			logger.info("待打印的文件不存在，文件路径是：{}", pdfFilePath);
			throw new Exception("待打印的文件不存");
		}

		// 调用Adobe Reader打印pdf
		try {
			PrintService printService = findDefaultPrintService();

			// StringBuffer cmd = new StringBuffer("cmd.exe /C start acrord32 /t
			// path printername drivername portname ");
			String cmd = String.format("cmd.exe /C start acrord32 /s /t %s %s", pdfFile.getAbsolutePath(),
					printService.getName());
			// Runtime.getRuntime().exec("cmd.exe /C start acrord32 /P /h " +
			// pdfFile.getAbsolutePath());
			// Runtime.getRuntime().exec("cmd.exe /C start acrord32 /P " +
			// pdfFile.getAbsolutePath());
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			throw e;
		}
	}

	// public static void main(String[] args) {
	// try {
	// pluginPrintPdf("d:/W1609230282_GS160923032333.pdf");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
