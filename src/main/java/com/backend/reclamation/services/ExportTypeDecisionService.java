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

import org.springframework.stereotype.Service;

import com.backend.commun.entity.Employe;
import com.backend.reclamation.entity.TypeDecision;
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
public class ExportTypeDecisionService {
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
	
	public  ByteArrayInputStream pdf(List<TypeDecision> typeDecisions) {
		
		Document doc = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(doc, out);
			doc.open();
			 
			//add text to pdf file
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
			//add title
			PdfPTable title=new PdfPTable(1);
			PdfPCell titleHeader_1 = new PdfPCell();
			Paragraph paragraph_1 = new Paragraph(" نوع القرار ",titlefont);
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
			PdfPTable table=new PdfPTable(2);
			//make column title
			Stream.of("عدد القرار","نوع القرار ").forEach(headerTitle -> {
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
			
			for(TypeDecision inf: typeDecisions) {
				//n° type
				PdfPCell nomCell = new PdfPCell(new Phrase(""+inf.getId(),font));
				nomCell.setPaddingLeft(1);
				nomCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomCell);
				
				//nom type  
				PdfPCell nomRecCell = new PdfPCell(new Phrase(inf.getNomType(),font));
				nomRecCell.setPaddingLeft(1);
				nomRecCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				nomRecCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				nomRecCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
				table.addCell(nomRecCell);
			}
			doc.add(table);
			doc.close();
		}catch(DocumentException e){
			e.printStackTrace();
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

}
