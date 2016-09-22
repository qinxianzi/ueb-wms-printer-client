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
		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new JobName(jobName, null));

		// 选择AUTOSENSE，即自动选择文件类型
		if (null == flavor) {
			flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		}

		HashAttributeSet has = new HashAttributeSet();
		if (StringUtils.isNotBlank(printer)) {
			has.add(new PrinterName(printer, null));// 添加打印机名称
		}

		//  查找所有的可用打印服务
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, has);
		if (null != printService[1]) {
			DocPrintJob job = printService[1].createPrintJob(); // 创建打印任务
			DocAttributeSet das = new HashDocAttributeSet();
			InputStream input = new FileInputStream(pdfFile);//  构造待打印文件流
			Doc doc = new SimpleDoc(input, flavor, das);//  创建打印文件格式
			job.print(doc, pras);//  打印文件
		}
	}
}
