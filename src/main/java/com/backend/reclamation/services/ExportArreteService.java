package com.backend.reclamation.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.reclamation.entity.Arrete;
import com.backend.reclamation.repository.ArreteRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ExportArreteService {
	String const1 = Const.const1;
	String const2 = Const.const2;

	ClassLoader classLoader = getClass().getClassLoader();
	// font arial 
	File file_Font = new File(classLoader.getResource("arial/arial.ttf").getFile());
	public String FONT = file_Font.getAbsolutePath();

	public com.itextpdf.text.Font tnfont = FontFactory.getFont(FONT, 12, Font.NORMAL, BaseColor.BLACK);
	public com.itextpdf.text.Font titlefont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18,
			Font.UNDERLINE, BaseColor.BLUE);
	//arab
	public com.itextpdf.text.Font headFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14,
			Font.NORMAL, BaseColor.BLACK);
	public com.itextpdf.text.Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12,
			Font.NORMAL, BaseColor.BLACK);
	// public com.itextpdf.text.Font font = FontFactory.getFont(FONT, 12,Font.NORMAL,BaseColor.BLACK);
	public static BaseColor myColor = WebColors.getRGBColor("#99CCFF");

	@Autowired
	private ArreteRepository arreteRepository;

	public ByteArrayInputStream arretepdf(List<Arrete> arretes) {
		Document doc = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(doc, out);
			doc.open();

			// add text to pdf file
			PdfPTable tn = new PdfPTable(1);
			PdfPCell titleHeader_1 = new PdfPCell();
			Paragraph paragraph_1 = new Paragraph(const1 + "\r\n" + const2 + "\r\n" + "Commune de la Monastir", tnfont);
			paragraph_1.setAlignment(Element.ALIGN_RIGHT);
			titleHeader_1.addElement(paragraph_1);
			// date
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String today = formatter.format(new Date());
			Paragraph datepdf = new Paragraph("Date " + today, tnfont);
			datepdf.setAlignment(Element.ALIGN_LEFT);
			titleHeader_1.addElement(datepdf);

			titleHeader_1.setBorder(Rectangle.NO_BORDER);
			// titleHeader.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			tn.addCell(titleHeader_1);
			doc.add(tn);
			// add title
			PdfPTable title = new PdfPTable(1);
			PdfPCell titleHeader_2 = new PdfPCell();
			Paragraph paragraph_2 = new Paragraph("جدول القرارات", titlefont);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			titleHeader_2.addElement(paragraph_2);
			titleHeader_2.setBorder(Rectangle.NO_BORDER);
			titleHeader_2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.addCell(titleHeader_2);
			doc.add(title);
			// Paragraph
			Paragraph para = new Paragraph(" ");
			para.setAlignment(Element.ALIGN_CENTER);
			doc.add(para);
			// NEWLINE
			doc.add(Chunk.NEWLINE);

			// table
			PdfPTable table = new PdfPTable(5);
			// make column title
			Stream.of("عدد القرار", "رمز القرار", "محتوى القرار", "موضوع القرار", "تاريخ القرار")
					.forEach(headerTitle -> {
						PdfPCell header = new PdfPCell();
						header.setBackgroundColor(myColor);
						header.setFixedHeight(35f);
						header.setHorizontalAlignment(Element.ALIGN_CENTER);
						header.setBorderWidth(1);
						header.setPhrase(new Phrase(headerTitle, headFont));
						header.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
						table.addCell(header);
						table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
					});

			for (Arrete arrete : arretes) {
				// n° arrete
				PdfPCell numCell = new PdfPCell(new Phrase("" + arrete.getNumArrete()));
				numCell.setPaddingLeft(1);
				numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				numCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				numCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(numCell);

				// ref arrete
				PdfPCell refCell = new PdfPCell(new Phrase(arrete.getRefArrete()));
				refCell.setPaddingLeft(1);
				refCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				refCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				refCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(refCell);

				// Description arrete
				PdfPCell nomRecCell = new PdfPCell(new Phrase(arrete.getDescriptionArrete()));
				nomRecCell.setPaddingLeft(1);
				nomRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomRecCell);

				// Objet arrete
				PdfPCell nomDesRecCell = new PdfPCell(new Phrase("" + arrete.getObjetArrete()));
				nomDesRecCell.setPaddingLeft(1);
				nomDesRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomDesRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomDesRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomDesRecCell);

				// date
				// date
				DateFormat formatter_ = new SimpleDateFormat("yyyy/MM/dd");
				String dateArr = formatter_.format(arrete.getDateArrete());
				PdfPCell dateRecCell = new PdfPCell(new Phrase(dateArr));
				dateRecCell.setPaddingLeft(1);
				dateRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				dateRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dateRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(dateRecCell);
			}
			doc.add(table);
			doc.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	// استخراج قرار
	public ByteArrayInputStream courtpdf(Arrete arretes) {

		Document doc = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(doc, out);
			doc.open();

			// add text to pdf file
			PdfPTable tn = new PdfPTable(1);
			PdfPCell titleHeader_1 = new PdfPCell();
			Paragraph paragraph_1 = new Paragraph(const1 + "\r\n" + const2 + "\r\n" + "Commune de la Monastir", tnfont);
			paragraph_1.setAlignment(Element.ALIGN_RIGHT);
			titleHeader_1.addElement(paragraph_1);
			// date
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String today = formatter.format(new Date());
			Paragraph datepdf = new Paragraph("Date " + today, tnfont);
			datepdf.setAlignment(Element.ALIGN_LEFT);
			titleHeader_1.addElement(datepdf);

			titleHeader_1.setBorder(Rectangle.NO_BORDER);
			// titleHeader.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			tn.addCell(titleHeader_1);
			doc.add(tn);

			// add title
			PdfPTable title = new PdfPTable(1);
			PdfPCell titleHeader = new PdfPCell();
			Paragraph paragraph = new Paragraph(arretes.getRefArrete(), titlefont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			titleHeader.addElement(paragraph);
			titleHeader.setBorder(Rectangle.NO_BORDER);
			titleHeader.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.addCell(titleHeader);
			doc.add(title);
			// Paragraph
			Paragraph para = new Paragraph("", font);
			para.setAlignment(Element.ALIGN_CENTER);
			doc.add(para);
			// NEWLINE
			doc.add(Chunk.NEWLINE);

			// content
			PdfPTable content = new PdfPTable(1);

			// cell1
			PdfPCell contentCell = new PdfPCell();
			Paragraph contentParagraph = new Paragraph("إن رئيس البلدية ", font);
			contentParagraph.setAlignment(Element.ALIGN_LEFT);
			contentCell.addElement(contentParagraph);
			contentCell.setBorder(Rectangle.NO_BORDER);
			contentCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			content.addCell(contentCell);
			// cell2
			// DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			String today_1 = formatter.format(arretes.getDateExecution());

			PdfPCell dateInfraction = new PdfPCell(new Phrase("\r\n"
					+ "بعد إطلاعه؛ على القانون الأساسي عدد 29 لسنة 2018 المؤرخ في 09 ماي 2018 و المتعلق بإصدار\r\n"
					+ "مجلة الجماعات المحلية . ,\r\n" + "\r\n"
					+ "و على القانون عدد 30 لسنة 2016 المؤرخ في 05 أفريل 2016 و المتعلق بتنقيح و إتمام القانون\r\n"
					+ "أعدد 59 لسنة 2006 المؤرخ في 14 أوت 2006 و المتعلق بمخالفة تراتيب حفظ الصحة بالمناطق الراجعة\r\n"
					+ "للجماعات المحلية. ,\r\n" + "و بعد الإطلاع على محضر معاينة المخالفة المحرر بتاريخ " + today_1,
					font));
			dateInfraction.setBorder(Rectangle.NO_BORDER);
			dateInfraction.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			content.addCell(dateInfraction);
			// cell3
			PdfPCell commentaires = new PdfPCell();
			Paragraph commentairesParagraph = new Paragraph(" قرر مايلي ", titlefont);
			commentairesParagraph.setAlignment(Element.ALIGN_CENTER);
			commentaires.addElement(commentairesParagraph);
			commentaires.setBorder(Rectangle.NO_BORDER);
			commentaires.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			content.addCell(commentaires);
			// cell4
			PdfPCell comments = new PdfPCell(new Phrase(
					"الفصل الاول يتم ازالة المخالفة المتمثلة في :" + (arretes.getCommentairesExecution()), font));
			comments.setBorder(Rectangle.NO_BORDER);
			comments.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			content.addCell(comments);

			// add table to doc
			doc.add(content);
			doc.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

}
