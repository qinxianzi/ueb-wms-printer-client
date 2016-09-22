package com.ueb.wms.printer.client.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;

public class PdfToImageDemo3 {
	public static void main(String[] args) throws Exception {
		String srcpdf = "D:/pdf/PK160912039918.pdf", despdf = "D:/pdf/PK160912039918_out.pdf";
		compressPdf(srcpdf, despdf);
	}

	public static void pdfTester(String srcpdf, String despdf) throws IOException, DocumentException {
		PdfReader srcReader = new PdfReader(srcpdf);
		Rectangle srcPageSize = srcReader.getPageSize(1);

		// float width = srcPageSize.getWidth(); // return urx - llx;
		// float height = srcPageSize.getHeight();

		// this.llx = llx;
		// this.lly = lly;
		// this.urx = urx;
		// this.ury = ury;

		// Rectangle desPageSize = new Rectangle(0, 0, width * 90, height * 90);
		// Document desDoc = new Document(srcPageSize);
		// PdfStamper stamper = new PdfStamper(srcReader, new
		// FileOutputStream(despdf));
		//
		// stamper.close();
		// srcReader.close();

		PdfSmartCopy copy = new PdfSmartCopy(new Document(), new FileOutputStream(despdf));
		copy.addPage(srcPageSize, 0);

		copy.flush();
		copy.close();
	}

	public static void compressPdf(String srcpdf, String despdf) throws Exception {
		PdfReader reader = null;
		PdfSmartCopy copy = null;
		Document document = null;
		try {
			reader = new PdfReader(srcpdf);
			int n = reader.getNumberOfPages();

			Rectangle srcPageSize = reader.getPageSize(1);
			int srcRotation = srcPageSize.getRotation();

			// 左下方为起点(0,0)，右上方(x=pdf宽度,y=pdf高度)
			float llx = srcPageSize.getLeft(); // 左下方x = 0
			float urx = srcPageSize.getRight(); // 右上方x = pdf宽度
			float lly = srcPageSize.getBottom(); // 左下方y = 0
			float ury = srcPageSize.getTop(); // 右上方y = pdf高度

			/**
			 * Constructs a <CODE>Rectangle</CODE>-object.
			 *
			 * @param llx
			 *            lower left x 左下方x
			 * @param lly
			 *            lower left y 左下方y
			 * @param urx
			 *            upper right x 右上方x
			 * @param ury
			 *            upper right y 右上方y
			 * @param rotation
			 *            the rotation (0, 90, 180, or 270)
			 * @since iText 5.0.6
			 */

			Rectangle desPageSize = new Rectangle(llx, lly, urx, ury * 0.5f, srcRotation);
			document = new Document(desPageSize);
			copy = new PdfSmartCopy(document, new FileOutputStream(despdf));
			document.open();
			for (int i = 1; i <= n; i++) {
				document.newPage();
				PdfImportedPage imported = copy.getImportedPage(reader, 1);
				copy.addPage(imported);
				// copy.addPage(reader.getPageSize(i),
				// reader.getPageRotation(i));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (document != null)
				document.close();
			if (reader != null)
				reader.close();
			if (copy != null)
				copy.close();
		}
	}

	// public void createPdf(String[] src, String dest) throws IOException {
	// // create the resulting PdfDocument instance
	// PdfDocument pdfDoc = new PdfDocument(new
	// PdfWriter(dest).setSmartMode(true));
	// pdfDoc.initializeOutlines();
	// PdfDocument srcDoc;
	// PageSize ps;
	// for (int i = 0; i < src.length; i++) {
	// // read the source PDF
	// srcDoc = new PdfDocument(new PdfReader(src[i]));
	// // copy content to the resulting PDF
	// srcDoc.copyPagesTo(1, srcDoc.getNumberOfPages(), pdfDoc);
	// // if the source PDF has only 1 page, add a blank page to the
	// // resulting PDF
	// if (srcDoc.getNumberOfPages() == 1) {
	// ps = new PageSize(srcDoc.getFirstPage().getPageSize());
	// pdfDoc.addNewPage(pdfDoc.getNumberOfPages() + 1, ps);
	// }
	// }
	// pdfDoc.close();
	// }
}
