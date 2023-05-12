package com.invoice.invoice.service;


import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceService {


    public ByteArrayInputStream createInvoice() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(outputStream);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        float threeCol = 190f;
        float twocol = 285f;
        float twocol150 = twocol + 150f;
        float[] twoColumnWidth = {twocol150, twocol};
        float[] twoColumnNestedTable = {twocol / 2, twocol / 2};
        float[] fullWidth = {threeCol * 3};
        float[] oneColumnWidth = {twocol150};
        float[] threeColumnWidth = {threeCol, threeCol, threeCol};

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Paragraph onespace = new Paragraph("\n");

        Table table = new Table(twoColumnWidth);
        table.addCell(new Cell().add(new Paragraph("Invoice")).setFontSize(20f).setBorder(Border.NO_BORDER).setBold());

        Table nestedTable = new Table(twoColumnNestedTable);

        nestedTable.addCell(getHeaderTextCell("Invoice No."));
        nestedTable.addCell(getHeaderTextCellValue(UUID.randomUUID().toString().substring(0, 8)));

        nestedTable.addCell(getHeaderTextCell("Invoice Date"));
        nestedTable.addCell(getHeaderTextCellValue(LocalDate.now().format(dateTimeFormatter)));

        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));
        Border grayBorder = new SolidBorder(ColorConstants.GRAY, 2f);

        Table divider = new Table(fullWidth);
        divider.setBorder(grayBorder);

        document.add(table);
        document.add(onespace);
        document.add(divider);
        document.add(onespace);

        Table twoColTable = new Table(twoColumnWidth);

        twoColTable.addCell(getBillingAndShippingCell("Billing Information"));
        twoColTable.addCell(getBillingAndShippingCell("Shipping Information"));

        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twoColumnWidth);
        twoColTable2.addCell(getCell10fLeft("Company", true));
        twoColTable2.addCell(getCell10fLeft("Name", true));
        twoColTable2.addCell(getCell10fLeft("Coding Error", false));
        twoColTable2.addCell(getCell10fLeft("Coding", false));

        document.add(twoColTable2);

        Table twoColTable3 = new Table(twoColumnWidth);
        twoColTable3.addCell(getCell10fLeft("Name", true));
        twoColTable3.addCell(getCell10fLeft("Address", true));
        twoColTable3.addCell(getCell10fLeft("John Doe", false));
        twoColTable3.addCell(getCell10fLeft("Av Siempre viva,\n Springfield", false));

        document.add(twoColTable3);

        Table oneColumnTable1 = new Table(oneColumnWidth);
        oneColumnTable1.addCell(getCell10fLeft("Address", true));
        oneColumnTable1.addCell(getCell10fLeft("Av Siempre viva,\n Springfield", false));
        oneColumnTable1.addCell(getCell10fLeft("Email", true));
        oneColumnTable1.addCell(getCell10fLeft("example@example.com", false));

        document.add(oneColumnTable1.setMarginBottom(10f));

        Table tableDivider2 = new Table(fullWidth);

        Border grayBorderDivider = new DashedBorder(ColorConstants.GRAY, 0.5f);
        document.add(tableDivider2.setBorder(grayBorderDivider));

        Paragraph productParagraph = new Paragraph("Products");

        document.add(productParagraph.setBold());

        Table threeColTable1 = new Table(threeColumnWidth);

        threeColTable1.setBackgroundColor(ColorConstants.BLACK, 0.7f);

        threeColTable1.addCell(new Cell().add(new Paragraph("Description").setBold().setFontColor(ColorConstants.WHITE))).setBorder(Border.NO_BORDER);
        threeColTable1.addCell(new Cell().add(new Paragraph("Quantity").setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER))).setBorder(Border.NO_BORDER);
        threeColTable1.addCell(new Cell().add(new Paragraph("Price").setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.RIGHT))).setBorder(Border.NO_BORDER);

        document.add(threeColTable1);

        List<Product> productList = new ArrayList<>();

        productList.add(new Product("apple", 2, 159));
        productList.add(new Product("mango", 4, 205));
        productList.add(new Product("banana", 2, 90));
        productList.add(new Product("grapes", 3, 10));

        productList.add(new Product("coconut", 2, 61));
        productList.add(new Product("cherry", 1, 1000));
        productList.add(new Product("kiwi", 3, 30));


        Table threeColTable2 = new Table(threeColumnWidth);

        float totalSum = 0f;

        for (Product product : productList) {
            float total = product.getQuantity() * product.getPriceperpiece();
            totalSum += total;

            threeColTable2.addCell(new Cell().add(new Paragraph(product.getPname())).setBorder(Border.NO_BORDER).setMarginLeft(10f));
            threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(product.getQuantity())).setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
            threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(product.getPriceperpiece())).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER).setMarginLeft(15f));
        }

        document.add(threeColTable2.setMarginBottom(20f));

        float[] onetwo = {threeCol + 125f, threeCol * 2};
        Table threeColTable4 = new Table(onetwo);

        threeColTable4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        threeColTable4.addCell(new Cell().add(tableDivider2)).setBorder(Border.NO_BORDER);

        document.add(threeColTable4);

        Table threeColTable3 = new Table(threeColumnWidth);

        threeColTable3.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER)).setMarginLeft(10f);
        threeColTable3.addCell(new Cell().add(new Paragraph("Total")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(new Paragraph(String.valueOf(totalSum))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setMarginRight(15f);


        document.add(threeColTable3);
        document.add(new Cell().add(tableDivider2));
        document.add(new Paragraph("\n"));
        document.add(divider.setBorder(new SolidBorder(ColorConstants.GRAY, 1)).setMarginBottom(15f));

        Table tb = new Table(fullWidth);
        tb.addCell(new Cell().add(new Paragraph("TERMS AND CONDITIONS\n").setBold()).setBorder(Border.NO_BORDER));
        List<String> termsandConditionsList = new ArrayList<>();
        termsandConditionsList.add("1.- The seller shall not be liable");
        termsandConditionsList.add("2.- Whatever");

        for (String tnc : termsandConditionsList) {
            tb.addCell(new Cell().add(new Paragraph(tnc)).setBorder(Border.NO_BORDER));
        }

        document.add(tb);

        document.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    static Cell getHeaderTextCell(String textValue) {
        return new Cell().add(new Paragraph(textValue).setBold()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getHeaderTextCellValue(String textValue) {
        return new Cell().add(new Paragraph(textValue).setBold()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getBillingAndShippingCell(String textValue) {
        return new Cell().add(new Paragraph(textValue).setFontSize(12f).setBold()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(String textValue, Boolean isBold) {
        Cell myCell = new Cell().add(new Paragraph(textValue).setFontSize(10f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? myCell.setBold() : myCell;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static
    class Product {
        private String pname;
        private int quantity;
        private float priceperpiece;
    }


}
