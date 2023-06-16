package com.backend.reclamation.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.backend.reclamation.entity.Infraction;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
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
public class ExportInfractionService {
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
	
	public  ByteArrayInputStream infpdf(List<Infraction> infractions) throws MalformedURLException, IOException {
		Document doc = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(doc, out);
			doc.open();
			 
			// Create the main table
			PdfPTable mainTable = new PdfPTable(1);

			// Create the nested table for the title, date, and image
			PdfPTable nestedTable = new PdfPTable(1);

			// Add the title
			PdfPCell titleCell = new PdfPCell();
			Paragraph titleParagraph = new Paragraph(const1 + "\r\n" + const2 + "\r\n" + "Commune de la Monastir", tnfont);
			titleParagraph.setAlignment(Element.ALIGN_RIGHT);
			titleCell.addElement(titleParagraph);
			titleCell.setBorder(Rectangle.NO_BORDER);
			nestedTable.addCell(titleCell);

			// Add the date
			DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			String today = formatter.format(new Date());
			PdfPCell dateCell = new PdfPCell(new Paragraph("Date " + today, tnfont));
			dateCell.setBorder(Rectangle.NO_BORDER);
			nestedTable.addCell(dateCell);

			// Add the image
			Image img = Image.getInstance(ClassLoader.getSystemResource("monatsir-removebg-preview.png"));
			//img.scaleAbsolute(10, 10);
			PdfPCell imageCell = new PdfPCell(img);
			imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			imageCell.setBorder(Rectangle.NO_BORDER);
			nestedTable.addCell(imageCell);

			// Add the nested table to the main table
			PdfPCell nestedTableCell = new PdfPCell(nestedTable);
			nestedTableCell.setBorder(Rectangle.NO_BORDER);
			mainTable.addCell(nestedTableCell);

			// Add the main table to the document
			doc.add(mainTable);
			
			
			
			
			//add title
			PdfPTable title=new PdfPTable(1);
			PdfPCell titleHeader_1 = new PdfPCell();
			Paragraph paragraph_1 = new Paragraph(" المحاضر",titlefont);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			titleHeader_1.addElement(paragraph_1);
			titleHeader_1.setBorder(Rectangle.NO_BORDER);
			titleHeader_1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			title.addCell(titleHeader_1);
			doc.add(title);
			//Paragraph
			Paragraph para = new Paragraph(" ");
			para.setAlignment(Element.ALIGN_CENTER);
			doc.add(para);
			//NEWLINE
			doc.add(Chunk.NEWLINE);
			
			//table
			PdfPTable table=new PdfPTable(4);
			//make column title
			Stream.of("عدد المحضر","الجهة ","محتوى المخالفة","تاريخ المخالفة").forEach(headerTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(myColor);
				header.setFixedHeight(35f);
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(headerTitle,headFont));
				header.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(header);
				table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
			});
			
			for(Infraction inf: infractions) {
				//n° infraction
				PdfPCell numCell = new PdfPCell(new Phrase(""+inf.getNumInfraction(),font));
				numCell.setPaddingLeft(1);
				numCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				numCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				numCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(numCell);
				
				//nom employe infraction
				PdfPCell nomRecCell = new PdfPCell(new Phrase(inf.getCodeEmploye(),font));
				nomRecCell.setPaddingLeft(1);
				nomRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomRecCell);
				
				//description infraction
				PdfPCell descriptionCell = new PdfPCell(new Phrase(inf.getDescriptionInfraction(),font));
				descriptionCell.setPaddingLeft(1);
				descriptionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				descriptionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				descriptionCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(descriptionCell);
				
				//date
				DateFormat formatter_ = new SimpleDateFormat("dd/MM/yyyy");
				String dateInf = formatter_.format(inf.getDateInfraction());
				
				PdfPCell dateRecCell = new PdfPCell(new Phrase(dateInf));
				dateRecCell.setPaddingLeft(1);
				dateRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				dateRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dateRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(dateRecCell);
			}
			doc.add(table);
			doc.close();
		}catch(DocumentException e){
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

}

