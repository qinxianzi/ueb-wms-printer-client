package com.ueb.wms.printer.client.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfStamper;

public class CropPages {

	/**
	 * Manipulates a PDF file src with the file dest as result
	 * 
	 * @param src
	 *            the original PDF
	 * @param dest
	 *            the resulting PDF
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		int n = reader.getNumberOfPages();
		PdfDictionary pageDict;
		PdfRectangle rect = new PdfRectangle(55, 76, 560, 816);
		for (int i = 1; i <= n; i++) {
			pageDict = reader.getPageN(i);
			pageDict.put(PdfName.CROPBOX, rect);
		}
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
		reader.close();
	}

	private static final String SRC = "D:/pdf/PK160912039918.pdf", DEST = "D:/pdf/PK160912039918_out.pdf";

	/**
	 * Main method.
	 *
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		new CropPages().manipulatePdf(SRC, DEST);
	}
}
