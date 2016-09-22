package com.ueb.wms.printer.client.demo;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Tester {

	private static final String SRC = "D:/pdf/PK160812000186.pdf", DEST = "D:/pdf/PK160912039918_out1.pdf";

	public static void main(String[] args) throws IOException, DocumentException {
		manipulatePdf1(SRC, DEST);
	}

	public static void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		int n = reader.getNumberOfPages();
		float percentage = 0.5f;
		for (int p = 1; p <= n; p++) {
			float offsetX = (reader.getPageSize(p).getWidth() * (1 - percentage)) / 2;
			float offsetY = (reader.getPageSize(p).getHeight() * (1 - percentage)) / 2;

			stamper.getUnderContent(p)
					.setLiteral(String.format("\nq %s 0 0 %s %s %s cm\nq\n", percentage, percentage, offsetX, offsetY));
			// stamper.getOverContent(p).setLiteral(String.format("\nq %s 0 0 %s
			// %s %s cm\nq\n", percentage, percentage, offsetX, offsetY));
			stamper.getOverContent(p).setLiteral("\nQ\nQ\n");
		}
		stamper.close();
		reader.close();
	}

	public static Font getFont2() throws IOException, DocumentException {
		// 使用微软雅黑字体显示中文
		// String yaHeiFontName = getResources().getString(R.raw.msyhl);
		String yaHeiFontName = "resources/font/MSYH.TTF";
		// yaHeiFontName += ",1";
		Font yaHeiFont = new Font(BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));// 中文简体
		// yaHeiFont.setColor(BaseColor.BLACK);
		yaHeiFont.setSize(7);
		yaHeiFont.setColor(BaseColor.BLACK);
		// yaHeiFont.setStyle(Font.BOLD);
		return yaHeiFont;
	}

	public static void manipulatePdf1(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		int n = reader.getNumberOfPages();
		reader.getPageSize(1);
		PdfDictionary page;
		PdfArray crop;
		PdfArray media;

		PdfContentByte over;
		Rectangle pagesize;
		float x, y;

		Phrase phrase = new Phrase("W1609190303", getFont2());
		PdfGState gs1 = new PdfGState();
		gs1.setFillOpacity(0.5f);

		for (int p = 1; p <= n; p++) {
			page = reader.getPageN(p);
			media = page.getAsArray(PdfName.CROPBOX);
			if (media == null) {
				media = page.getAsArray(PdfName.MEDIABOX);
			}
			crop = new PdfArray();
			crop.add(new PdfNumber(0));
			crop.add(new PdfNumber(0));
			crop.add(new PdfNumber(media.getAsNumber(2).floatValue() / 2));
			crop.add(new PdfNumber(media.getAsNumber(3).floatValue() / 2));
			// PdfNumber(media.getAsNumber(3).floatValue()));
			// page.put(PdfName.MEDIABOX, crop);
			// page.put(PdfName.CROPBOX, crop);
			page.put(PdfName.CROPBOX, new PdfArray(getCropbox(media)));
			// stamper.getUnderContent(p).setLiteral("\nq 0.5 0 0 0.5 0 0
			// cm\nq\n");
			// stamper.getUnderContent(p).setLiteral("\nq 0.5 0 0 0.5 0
			// 0cm\nq\n");
			// stamper.getOverContent(p).setLiteral("\nQ\nQ\n");

			pagesize = reader.getPageSize(p);
			x = (pagesize.getLeft() + pagesize.getRight()) / 7 + 12;
			y = (pagesize.getTop() + pagesize.getBottom()) / 6;
			over = stamper.getOverContent(p);
			over.saveState();
			over.setGState(gs1);
			ColumnText.showTextAligned(over, Element.ALIGN_LEFT, phrase, x, y, 0);
			for (int j = 0; j < 3; j++) {
				x += 60;
				ColumnText.showTextAligned(over, Element.ALIGN_LEFT, getPhrase(j), x, y, 0);
			}
		}
		stamper.close();
		reader.close();
	}

	private static Phrase getPhrase(int i) throws IOException, DocumentException {
		Font font = getFont2();
		Phrase phrase3 = null;
		if (i == 0) {
			phrase3 = new Phrase("0--8", font);
		} else if (i == 1) {
			phrase3 = new Phrase("CW:", font);
		} else if (i == 2) {
			phrase3 = new Phrase("ALI:", font);
		}
		return phrase3;
	}

	public void manipulatePdf2(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		int n = reader.getNumberOfPages();
		PdfDictionary pageDict;
		for (int i = 1; i <= n; i++) {
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

	private static float[] getCropbox(PdfArray cropbox) {
		float[] rslt = new float[cropbox.size()];
		for (int k = 0; k < rslt.length; ++k) {
			// if (3 == k) {
			// rslt[k] = cropbox.getAsNumber(k).floatValue() + 20f;
			// continue;
			// }
			rslt[k] = cropbox.getAsNumber(k).floatValue();
		} // [90.7087, 141.732, 382.402, 436.646] ==> [90.7087, 141.732,
			// 382.402, 437.146]
			// rslt[0] = 0.0f;
		rslt[1] = rslt[1] - 10;
		return rslt;
	}
}
