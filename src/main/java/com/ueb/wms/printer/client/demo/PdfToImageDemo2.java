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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class PdfToImageDemo2 {
	public static void main(String[] args) throws Exception {
		manipulatePdf("D:/pdf/PK160812000186.pdf", "D:/pdf/PK160912039918_out.pdf");
	}

	public static Font getFont() {
		// 如果不设置中文处理，不会被显示出来
		BaseFont bfChinese = null;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Font fontChinese = new Font(bfChinese, 8, Font.NORMAL);
		// 设置字体颜色
		fontChinese.setColor(BaseColor.BLACK);
		return fontChinese;
		// // 页眉
		// ColumnText.showTextAligned(writer.getDirectContent(),
		// Element.ALIGN_CENTER, new Phrase("我是页眉", fontChinese),
		// rect.getLeft(), rect.getTop() + 35, 0);
		// 页脚
		// ColumnText.showTextAligned(writer.getDirectContent(),
		// Element.ALIGN_CENTER,
		// new Phrase(String.format("第 %d 页", writer.getPageNumber()),
		// fontChinese),
		// (rect.getLeft() + rect.getRight()) / 2 + 120, rect.getBottom() - 40,
		// 0);
	}

	/**
	 * http://blog.csdn.net/lh806732/article/details/44309721
	 * 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Font getFont2() throws IOException, DocumentException {
		// 使用微软雅黑字体显示中文
		// String yaHeiFontName = getResources().getString(R.raw.msyhl);
		String yaHeiFontName = "resources/font/MSYHBD.TTF";
		// yaHeiFontName += ",1";
		Font yaHeiFont = new Font(BaseFont.createFont(yaHeiFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));// 中文简体
		// yaHeiFont.setColor(BaseColor.BLACK);
		yaHeiFont.setSize(8);
		// yaHeiFont.setColor(BaseColor.BLUE);
		// yaHeiFont.setStyle(Font.BOLD);
		return yaHeiFont;
	}

	public static void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		// stamper.setRotateContents(false); //自动调整内容，默认为true

		// 文字水印
		Phrase phrase = new Phrase("W1609190303", getFont2());

		// // 图片水印
		// Image img = Image.getInstance("d:/test_img.jpg");
		// float w = img.getScaledWidth();
		// float h = img.getScaledHeight();

		// 透明度
		PdfGState gs1 = new PdfGState();
		gs1.setFillOpacity(0.5f);

		// properties
		PdfContentByte over;
		Rectangle pagesize;
		float x, y;
		// loop over every page
		for (int i = 1, n = reader.getNumberOfPages(); i <= n; i++) {
			pagesize = reader.getPageSize(i);
			x = (pagesize.getLeft() + pagesize.getRight()) / 6; // (左下角x值 +
																// 右上角x值) / 2
			y = (pagesize.getTop() + pagesize.getBottom()) / 4; // (右上角度y值 +
																// 左小角y值) / 2
			over = stamper.getOverContent(i);
			over.saveState();
			over.setGState(gs1);
			// over.setColorFill(BaseColor.BLACK);
			// over.resetRGBColorFill();
			// if (i % 2 == 1) {

			/**
			 * phrase：文字水印 rotation：逆时针旋转角度
			 */
			ColumnText.showTextAligned(over, Element.ALIGN_LEFT, phrase, x, y, 0);
			for (int j = 0; j < 3; j++) {
				x += 78;
				ColumnText.showTextAligned(over, Element.ALIGN_LEFT, getPhrase(j), x, y, 0);
			}
			// } else {
			// over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
			// }
			over.restoreState();
		}
		stamper.close();
		reader.close();

	}

	private static Phrase getPhrase(int i) {
		Font font = getFont();
		Phrase phrase3 = null;
		if (i == 0) {
			phrase3 = new Phrase("0--8", font);
		} else if (i == 1) {
			phrase3 = new Phrase("CW:", font);
		} else if (i == 2) {
			phrase3 = new Phrase("ALI:", font);
		}

		// // When no parameters are passed, the default leading = 16
		// Phrase phrase0 = new Phrase();
		// Phrase phrase1 = new Phrase("this is a phrase");
		// // In this example the leading is passed as a parameter
		// Phrase phrase2 = new Phrase(16, "this is a phrase with leading 16");
		// // When a Font is passed (explicitly or embedded in a chunk), the
		// // default leading = 1.5 * size of the font
		// Phrase phrase4 = new Phrase(new Chunk("this is a phrase"));
		// Phrase phrase5 = new Phrase(18, new Chunk("this is a phrase", font));
		return phrase3;
	}
}
