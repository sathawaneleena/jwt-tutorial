package com.example.usermanagement.exporter;

import com.example.usermanagement.entity.UserM;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;


public class UserPDFExporter {
    private List<UserM> listUsers;

    public UserPDFExporter(List<UserM> listUsers) {
        this.listUsers = listUsers;
    }


        private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(6);

        com.lowagie.text.Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

       /* cell.setPhrase(new Phrase("UserID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase(("UserName"), font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Designation",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Address", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Verified", font));
        table.addCell(cell);*/
    }

    private void writeTableData(PdfPTable table) {
       if(!CollectionUtils.isEmpty(listUsers)){
           for (UserM user : listUsers) {
               table.addCell(String.valueOf(user.getId()));
               table.addCell(user.getUserName());
               table.addCell(user.getEmail());
               table.addCell(user.getDesignation());
               table.addCell(user.getAddress());
               table.addCell(String.valueOf(user.isVerified()));
           }
       }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

       document.open();
       Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 2.0f, 4.5f, 2.0f, 1.5f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}

