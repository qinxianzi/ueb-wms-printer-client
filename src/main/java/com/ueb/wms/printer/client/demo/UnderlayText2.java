package com.ueb.wms.printer.client.demo;

import java.awt.Color;
import java.io.File;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentGroup;

public class UnderlayText2 {
	public static void main(String[] args) throws Exception {
		PDDocument doc = PDDocument.load(new File("D:/pdf//PK160912024098.pdf"));
		PDPage page = doc.getPage(0);
		doc.getNumberOfPages();

		// new PDPageContentStream(doc, page, true, true, true);
		// PDPageContentStream contentStream = new PDPageContentStream(doc,
		// page, AppendMode.OVERWRITE, false);
		PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, false);

		// BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
		// BaseFont.NOT_EMBEDDED);

		PDOptionalContentGroup enabled = new PDOptionalContentGroup("enabled");
		PDOptionalContentGroup disabled = new PDOptionalContentGroup("disabled");

		PDFont font = PDType1Font.TIMES_ROMAN;//PDType1Font.HELVETICA_BOLD;
		contentStream.beginMarkedContent(COSName.OC, enabled);
		contentStream.setNonStrokingColor(Color.BLACK);
		contentStream.beginText();
		contentStream.setFont(font, 12);
		contentStream.newLineAtOffset(90, 230);
		contentStream.showText(getContent());
		contentStream.endText();
		contentStream.endMarkedContent();

		// // Paint disabled layer
		// contentStream.beginMarkedContent(COSName.OC, disabled);
		// contentStream.setNonStrokingColor(Color.RED);
		// contentStream.beginText();
		// contentStream.setFont(font, 12);
		// contentStream.newLineAtOffset(80, 210);
		// // contentStream.showText("This is from a disabled layer. If you see
		// // this, that's NOT good!");
		// contentStream.showText("This is from a disabled layer. If you see
		// this, that's NOT good!");
		// contentStream.endText();
		// contentStream.endMarkedContent();

		// contentStream.saveGraphicsState();
		contentStream.closeAndFillAndStrokeEvenOdd();
		// contentStream.saveGraphicsState();
		contentStream.close();
		// doc.save("d:/ocg-generation.pdf");
		doc.save("D:/pdf//PK160912024098.pdf");
		doc.close();
	}

	private static String getContent() {
		return "送达方打算发的萨芬";
	}
}
