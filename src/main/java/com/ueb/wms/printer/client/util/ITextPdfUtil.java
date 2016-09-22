package com.ueb.wms.printer.client.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
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
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ITextPdfUtil {
	private static final Logger logger = LoggerFactory.getLogger(ITextPdfUtil.class);

	public static void manipulatePdf(InputStream srcInput, String dest, List<String> contents) throws Exception {
		PdfReader reader = null;
		PdfStamper stamper = null;
		try {
			reader = new PdfReader(srcInput);
			stamper = new PdfStamper(reader, new FileOutputStream(dest));

			// 只处理一页，目前一张面单只有一页
			PdfDictionary firstPage = reader.getPageN(1);
			Rectangle pagesize = reader.getPageSize(1);
			PdfArray media = firstPage.getAsArray(PdfName.CROPBOX);
			if (null == media) {
				media = firstPage.getAsArray(PdfName.MEDIABOX);
			}
			firstPage.put(PdfName.CROPBOX, new PdfArray(ITextPdfUtil.getCropbox(media)));

			ITextPdfUtil.appendContent(pagesize, stamper, contents);
		} catch (Exception e) {
			logger.error("处理pdf文件出现异常，异常信息是：{}" + e.getMessage());
			throw e;
		} finally {
			if (null != stamper) {
				stamper.close();
			}
			if (null != reader) {
				reader.close();
			}
		}
	}

	private static void appendContent(Rectangle pagesize, PdfStamper stamper, List<String> contents) throws Exception {
		float x = (pagesize.getLeft() + pagesize.getRight()) / 7 + 12;
		float y = (pagesize.getTop() + pagesize.getBottom()) / 6;
		PdfContentByte over = stamper.getOverContent(1);
		over.saveState();

		// 透明度
		PdfGState gs1 = new PdfGState();
		// gs1.setFillOpacity(0.5f);
		gs1.setFillOpacity(1.0f);
		over.setGState(gs1);

		Font font = getFont();
		for (Iterator<String> it = contents.iterator(); it.hasNext();) {
			Phrase phrase = getPhrase(it.next(), font);
			ColumnText.showTextAligned(over, Element.ALIGN_LEFT, phrase, x, y, 0);
			x += 60;
		}
	}

	private static Phrase getPhrase(String content, Font font) throws Exception {
		Phrase phrase = new Phrase(content, font);
		return phrase;
	}

	/**
	 * 使用系统字体
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Font getFont() throws Exception {
		// 使用微软雅黑字体显示中文
		// String yaHeiFontName = "resources/font/MSYH.TTF";
		String yaHeiFontName = "resources/font/COUR.TTF";
		Font yaHeiFont = new Font(BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));// 中文简体
		yaHeiFont.setSize(7);
		yaHeiFont.setStyle(Font.BOLD);
		yaHeiFont.setColor(BaseColor.BLACK);
		return yaHeiFont;
	}

	/**
	 * 使用itext-asian包中的字体
	 * 
	 * @return
	 */
	public static Font getFont2() {
		BaseFont bfChinese = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			logger.error("加载中文字体出现异常，异常信息是：{}" + e.getMessage());
		}
		Font fontChinese = new Font(bfChinese, 7, Font.NORMAL);
		fontChinese.setColor(BaseColor.BLACK);
		return fontChinese;
	}

	private static float[] getCropbox(PdfArray cropbox) {
		float[] rslt = new float[cropbox.size()];
		for (int k = 0; k < rslt.length; ++k) {
			// if (3 == k) {
			// rslt[k] = cropbox.getAsNumber(k).floatValue() + 20f;
			// continue;
			// }
			rslt[k] = cropbox.getAsNumber(k).floatValue();
		}
		rslt[1] = rslt[1] - 10; // 高度增加10
		return rslt;
	}
}
