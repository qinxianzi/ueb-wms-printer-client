package com.ueb.wms.printer.client.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ShrinkPdf {
	private static final String SRC = "D:/pdf/PK160912039918.pdf", DEST = "D:/pdf/PK160912039918_out.pdf";

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ShrinkPdf().manipulatePdf(SRC, DEST);
	}

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		int n = reader.getNumberOfPages();
		PdfDictionary page;
		PdfArray crop;
		PdfArray media;
		for (int p = 1; p <= n; p++) {
			page = reader.getPageN(p);
			media = page.getAsArray(PdfName.CROPBOX);
			if (media == null) {
				media = page.getAsArray(PdfName.MEDIABOX);
			}
			crop = new PdfArray();
			crop.add(new PdfNumber(media.getAsNumber(0).floatValue()));
			crop.add(new PdfNumber(media.getAsNumber(2).floatValue()));
			crop.add(new PdfNumber(media.getAsNumber(2).floatValue() / 2));
			crop.add(new PdfNumber(media.getAsNumber(3).floatValue() / 2));
			page.put(PdfName.MEDIABOX, crop);
			page.put(PdfName.CROPBOX, crop);
			stamper.getUnderContent(p).setLiteral("\nq 0.5 0 0 0.5 0 0 cm\nq\n");
			stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
		}
		stamper.close();
		reader.close();
	}
}
