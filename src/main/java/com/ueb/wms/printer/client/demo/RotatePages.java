package com.ueb.wms.printer.client.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class RotatePages {
	/** The resulting PDF. */
	private static final String SRC = "D:/pdf/PK160912039918.pdf", DEST = "D:/pdf/PK160912039918_out.pdf";

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
		int rot;
		PdfDictionary pageDict;
		for (int i = 1; i <= n; i++) {
			rot = reader.getPageRotation(i);
			pageDict = reader.getPageN(i);

			// PdfArray mediabox = pageDict.getAsArray(PdfName.MEDIABOX);
			// PdfArray cropbox = pageDict.getAsArray(PdfName.CROPBOX);
			// pageDict.put(PdfName.ROTATE, new PdfNumber(rot + 90)); //旋转90度

			Rectangle srcPageSize = reader.getPageSize(i);
			int srcRotation = srcPageSize.getRotation();

			PdfArray cropbox = pageDict.getAsArray(PdfName.CROPBOX);
			// PdfNumber num = (PdfNumber) cropbox.getPdfObject(2);
			// num.floatValue();
			// // [90.7087, 141.732, 382.402, 436.646]
			// double[] cropboxNums = cropbox.asDoubleArray();
			// cropboxNums[2] = cropboxNums[2] * 0.9;
			//
			// // 左下方为起点(0,0)，右上方(x=pdf宽度,y=pdf高度)
			// float llx = srcPageSize.getLeft(); // 左下方x = 0
			// float urx = srcPageSize.getRight(); // 右上方x = pdf宽度
			// float lly = srcPageSize.getBottom(); // 左下方y = 0
			// float ury = srcPageSize.getTop(); // 右上方y = pdf高度

			// pageDict.put(PdfName.CROPBOX, new PdfArray(new float[] { llx,
			// lly, urx, ury * 0.9f }));
			pageDict.put(PdfName.CROPBOX, new PdfArray(this.getCropbox(cropbox)));
			// pageDict.put(PdfName.MEDIABOX, new
			// PdfArray(this.getCropbox(cropbox)));
			pageDict.put(PdfName.ROTATE, new PdfNumber(srcRotation));
		}
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		stamper.close();
		reader.close();
	}

	private float[] getCropbox(PdfArray cropbox) {
		float[] rslt = new float[cropbox.size()];
		for (int k = 0; k < rslt.length; ++k) {
			if (3 == k) {
				rslt[k] = cropbox.getAsNumber(k).floatValue() + 20;
				continue;
			}
			rslt[k] = cropbox.getAsNumber(k).floatValue();
		}
		return rslt;
	}

	/**
	 * Main method creating the PDF.
	 * 
	 * @param args
	 *            no arguments needed
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException, DocumentException {
		new RotatePages().manipulatePdf(SRC, DEST);
	}
}
