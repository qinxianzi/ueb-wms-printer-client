package com.ueb.wms.printer.client.demo;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScaleRotate {
	private static final String SRC = "D:/pdf/PK160912039918.pdf", DEST = "D:/pdf/PK160912039918_out.pdf";

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ScaleRotate().manipulatePdf(SRC, DEST);
	}

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		int n = reader.getNumberOfPages();
		PdfDictionary page;
		for (int p = 1; p <= n; p++) {
			page = reader.getPageN(p);
			if (page.getAsNumber(PdfName.USERUNIT) == null) {
				page.put(PdfName.USERUNIT, new PdfNumber(2.5f));
			}
//			page.remove(PdfName.ROTATE);
		}
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
		reader.close();
	}
}
