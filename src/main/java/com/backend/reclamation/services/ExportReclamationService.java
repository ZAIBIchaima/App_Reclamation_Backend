package com.backend.reclamation.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.backend.reclamation.entity.Reclamation;
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
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.interfaces.PdfRunDirection;
import com.itextpdf.kernel.colors.ColorConstants;

@Service
public class ExportReclamationService {
	String const1 = Const.const1;
	String const2 = Const.const2;
	
	ClassLoader classLoader = getClass().getClassLoader();
	//font
	File file_Font = new File(classLoader.getResource("arial/arial.ttf").getFile());
	public  final String FONT = file_Font.getAbsolutePath();
	
	public  com.itextpdf.text.Font tnfont = FontFactory.getFont(FONT, 12, Font.NORMAL,
			BaseColor.BLACK);
	public  com.itextpdf.text.Font titlefont = FontFactory.getFont(FONT,BaseFont.IDENTITY_H,18,Font.UNDERLINE,BaseColor.BLUE);
	public  com.itextpdf.text.Font headFont = FontFactory.getFont(FONT,BaseFont.IDENTITY_H,14,Font.NORMAL,BaseColor.BLACK);
	public  com.itextpdf.text.Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, 12, Font.NORMAL,
			BaseColor.BLACK);
	public static BaseColor myColor = WebColors.getRGBColor("#99CCFF");

	public ByteArrayInputStream reclamationPdf(List<Reclamation> reclamations) {
		
		Document doc = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(doc, out);
			doc.open();

			// add text to pdf file
			PdfPTable tn = new PdfPTable(1);
			PdfPCell titleHeader = new PdfPCell();
			Paragraph paragraph = new Paragraph(const1+"\r\n" + const2+"\r\n" + "Commune de la Monastir",
					tnfont);
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			titleHeader.addElement(paragraph);
			// date
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String today = formatter.format(new Date());
			Paragraph datepdf = new Paragraph("Date " + today, tnfont);
			datepdf.setAlignment(Element.ALIGN_LEFT);
			titleHeader.addElement(datepdf);

			titleHeader.setBorder(Rectangle.NO_BORDER);
			//titleHeader.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			tn.addCell(titleHeader);
			doc.add(tn);
			// add title
			PdfPTable title = new PdfPTable(1);
			PdfPCell titleHeader_1 = new PdfPCell();
			Paragraph paragraph_1 = new Paragraph("جدول الشكاوي", titlefont);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			titleHeader_1.addElement(paragraph_1);
			titleHeader_1.setBorder(Rectangle.NO_BORDER);
			titleHeader_1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.addCell(titleHeader_1);
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
			Stream.of("عدد الشكوى", "رمز الشكوى", "الشاكي", "المشتكي به", "تاريخ الشكوى").forEach(headerTitle -> {
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

			for (Reclamation rec : reclamations) {
				// n° reclamation
				PdfPCell numCell = new PdfPCell(new Phrase("" + rec.getNumReclamation(),font));
				numCell.setPaddingLeft(1);
				numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				numCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				numCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(numCell);

				// ref reclamation
				PdfPCell refCell = new PdfPCell(new Phrase(rec.getRefReclamation(),font));
				refCell.setPaddingLeft(1);
				//refCell.setFixedHeight(70f);
				refCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				refCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				refCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(refCell);

				// nom source reclamation
				PdfPCell nomRecCell = new PdfPCell(new Phrase(rec.getPrenomNomSourceReclamation(),font));
				nomRecCell.setPaddingLeft(1);
				//nomRecCell.setFixedHeight(70f);
				nomRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomRecCell);

				// nom destinataire reclamation
				PdfPCell nomDesRecCell = new PdfPCell(new Phrase(rec.getPrenomNomSourceDestinataire(),font));
				nomDesRecCell.setPaddingLeft(1);
				//nomDesRecCell.setFixedHeight(70f);
				nomDesRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomDesRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomDesRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomDesRecCell);

				// date
				DateFormat formatter_ = new SimpleDateFormat("dd/MM/yyyy");
				String dateRec = formatter_.format(rec.getDateReclamation());
				
				PdfPCell dateRecCell = new PdfPCell(new Phrase(dateRec));
				dateRecCell.setPaddingLeft(1);
				dateRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				dateRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dateRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(dateRecCell);
			}
			doc.add(table);

			/*
			 * int numberOfPages = pdfDoc.getNumberOfPages(); for (int i = 1; i <=
			 * numberOfPages; i++) {
			 * 
			 * // Write aligned text to the specified by parameters point
			 * doc.showTextAligned(new Paragraph(String.format("page %s of %s", i,
			 * numberOfPages)), 559, 806, i, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
			 * }
			 */
			doc.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

	public static ByteArrayInputStream reclamationCSV(List<Reclamation> reclamations) throws IOException {
		// String[] columns = {"عدد الشكوى","رمز الشكوى","الشاكي","المشتكي به","تاريخ
		// الشكوى"};
		String[] columns = { "", "", "", "", "" };
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			CreationHelper creationHelper = workbook.getCreationHelper();

			Sheet sheet = workbook.getSheet("reclamations");
			sheet.autoSizeColumn(columns.length);

			/*
			 * org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
			 * headerFont.setBold(true); headerFont.setColor(IndexedColors.BLUE.getIndex());
			 */

			CellStyle cellStyle = workbook.createCellStyle();
			// cellStyle.setFont(headerFont);

			// Row for header
			Row headerRow = sheet.createRow(0);

			// header
			for (int col = 0; col < columns.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(columns[col]);
				cell.setCellStyle(cellStyle);
			}

			CellStyle cellStyle1 = workbook.createCellStyle();
			cellStyle1.setDataFormat(creationHelper.createDataFormat().getFormat("a"));

			int rowIndex = 1;
			for (Reclamation rec : reclamations) {
				Row row = sheet.createRow(rowIndex++);

				row.createCell(0).setCellValue(rec.getNumReclamation());
				row.createCell(1).setCellValue(rec.getRefReclamation());
				row.createCell(2).setCellValue(rec.getPrenomNomSourceReclamation());
				row.createCell(3).setCellValue(rec.getPrenomNomSourceDestinataire());
				row.createCell(4).setCellValue(rec.getDateReclamation().toString());
			}
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}
