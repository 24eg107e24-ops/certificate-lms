package com.lms.certsystem.util;

import com.lms.certsystem.entity.Certificate;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

public class PDFCertificateGenerator {

    public static byte[] generateCertificatePdf(Certificate cert) {
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Font Styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, Color.DARK_GRAY);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Color.GRAY);
            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new Color(13, 71, 161)); // Deep Blue
            Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Color.BLACK);
            Font courseFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(46, 125, 50)); // Dark Green
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);

            // Header Space
            document.add(new Paragraph(" "));

            // Title
            Paragraph pTitle = new Paragraph("CERTIFICATE OF COMPLETION", titleFont);
            pTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(pTitle);

            document.add(new Paragraph("\n"));

            // Subtitle
            Paragraph pSub = new Paragraph("This is proudly presented to", subtitleFont);
            pSub.setAlignment(Element.ALIGN_CENTER);
            document.add(pSub);

            document.add(new Paragraph("\n"));

            // Student Name
            Paragraph pName = new Paragraph(cert.getStudent().getName(), nameFont);
            pName.setAlignment(Element.ALIGN_CENTER);
            document.add(pName);

            document.add(new Paragraph("\n"));

            // Completion details
            Paragraph pBody = new Paragraph("for successfully completing the course and passing the examination for", bodyFont);
            pBody.setAlignment(Element.ALIGN_CENTER);
            document.add(pBody);

            document.add(new Paragraph("\n"));

            // Course Title
            Paragraph pCourse = new Paragraph(cert.getCourse().getTitle(), courseFont);
            pCourse.setAlignment(Element.ALIGN_CENTER);
            document.add(pCourse);

            document.add(new Paragraph("\n"));

            // Score and Date
            Paragraph pScore = new Paragraph("Achieved Score: " + String.format("%.1f", cert.getScorePercentage()) + "%  |  Date: " + cert.getIssueDate().toString(), bodyFont);
            pScore.setAlignment(Element.ALIGN_CENTER);
            document.add(pScore);

            document.add(new Paragraph("\n\n"));

            // Bottom section with QR code and Cert ID using PdfPTable & PdfPCell (non-deprecated)
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 1f});

            // QR Code cell
            PdfPCell qrCell = new PdfPCell();
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            byte[] qrBytes = QRCodeGenerator.generateQRCodeImageBytes(cert.getVerificationUrl(), 120, 120);
            Image qrImage = Image.getInstance(qrBytes);
            qrCell.addElement(qrImage);

            Paragraph pCertId = new Paragraph("Certificate ID: " + cert.getCertificateId() + "\nScan QR to verify authenticity online", metaFont);
            qrCell.addElement(pCertId);
            table.addCell(qrCell);

            // Signature cell
            PdfPCell sigCell = new PdfPCell();
            sigCell.setBorder(Rectangle.NO_BORDER);
            sigCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sigCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            
            Paragraph pSig = new Paragraph("________________________\nAuthorized Registrar / LMS Director", metaFont);
            pSig.setAlignment(Element.ALIGN_RIGHT);
            sigCell.addElement(pSig);
            table.addCell(sigCell);

            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
