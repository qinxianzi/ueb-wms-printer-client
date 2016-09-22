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

public class ChangeZoomXYZDestination {

	private static String SRC = "D:/pdf/PK160912024098.pdf", DEST = "D:/pdf/PK160912039918_out.pdf";

	public static void main(String[] args) throws IOException, DocumentException {
		File file = new File(DEST);
		file.getParentFile().mkdirs();
		new ChangeZoomXYZDestination().manipulatePdf(SRC, DEST);
	}

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfDictionary page = reader.getPageN(1);
		PdfArray annots = page.getAsArray(PdfName.BOUNDS);
		for (int i = 0; i < annots.size(); i++) {
			PdfDictionary annotation = annots.getAsDict(i);
			if (PdfName.LINK.equals(annotation.getAsName(PdfName.SUBTYPE))) {
				PdfArray d = annotation.getAsArray(PdfName.DEST);
				if (d != null && d.size() == 5 && PdfName.XYZ.equals(d.getAsName(1))) {
					d.set(4, new PdfNumber(0));
				}
			}
		}
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
	}
}
