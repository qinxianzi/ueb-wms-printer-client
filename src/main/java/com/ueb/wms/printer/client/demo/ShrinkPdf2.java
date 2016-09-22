package com.ueb.wms.printer.client.demo;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShrinkPdf2 {
	private static final String SRC = "D:/pdf/PK160912039918.pdf", DEST = "D:/pdf/PK160912039918_out.pdf";

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ShrinkPdf2().manipulatePdf(SRC, DEST);
	}

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		int n = reader.getNumberOfPages();
		float percentage = 0.9f;

		PdfArray media;
		PdfDictionary page;
		for (int p = 1; p <= n; p++) {
			float offsetX = (reader.getPageSize(p).getWidth() * (1 - percentage)) / 2;
			float offsetY = (reader.getPageSize(p).getHeight() * (1 - percentage)) / 2;

			stamper.getUnderContent(p)
					.setLiteral(String.format("\nq %s 0 0 %s %s %s cm\nq\n", percentage, percentage, offsetX, offsetY));
			// stamper.getOverContent(p).setLiteral(String.format("\nq %s 0 0 %s
			// %s %s cm\nq\n", percentage, percentage, offsetX, offsetY));
			// stamper.getOverContent(p).setLiteral("\nQ\nQ\n");

			// page = reader.getPageN(p);
			// media = page.getAsArray(PdfName.CROPBOX);
			// if (media == null) {
			// media = page.getAsArray(PdfName.MEDIABOX);
			// }
			// PdfArray pdfArray = new PdfArray(getCropbox(media));
			// page.put(PdfName.CROPBOX, pdfArray);
			// page.put(PdfName.MEDIABOX, pdfArray);
		}
		stamper.close();
		reader.close();
	}

	private static float[] getCropbox(PdfArray cropbox) {
		float[] rslt = new float[cropbox.size()];
		for (int k = 0; k < rslt.length; ++k) {
			if (3 == k) {
				rslt[k] = cropbox.getAsNumber(k).floatValue() + 10f;
				continue;
			}
			rslt[k] = cropbox.getAsNumber(k).floatValue();
		} // [90.7087, 141.732, 382.402, 436.646] ==> [90.7087, 141.732,
			// 382.402, 437.146]
			// rslt[0] = 0.0f;
			// rslt[0] = rslt[1] - 5;
		rslt[1] = rslt[1] + 10;
		return rslt;
	}
}
