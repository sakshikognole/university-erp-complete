package com.example.demo.student.service;

import com.example.demo.student.model.LetterHead;
import com.example.demo.student.model.Student;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CertificateService {

    private final StudentService     studentService;
    private final LetterHeadService  letterHeadService;

    public CertificateService(StudentService studentService,
                               LetterHeadService letterHeadService) {
        this.studentService    = studentService;
        this.letterHeadService = letterHeadService;
    }

    public String resolvePrefix(String gender) {
        if (gender == null) return "";
        return switch (gender.trim().toLowerCase()) {
            case "female" -> "Miss";
            case "male"   -> "Mr.";
            default       -> "";
        };
    }

    /** Default body — used by HandoutService */
    public byte[] generatePdf(String studentId) throws DocumentException {
        Student student = studentService.getStudentModelById(studentId);
        String  prefix  = resolvePrefix(student.getGender());
        String  fullName = prefix.isEmpty()
                ? student.getStudentName()
                : prefix + " " + student.getStudentName();
        String body =
            "        This is to certify that " + fullName +
            " is a bonafide student of " + student.getDegreeProgramName() +
            ", currently studying in " + student.getStudyingYear() +
            " during the Academic Year " + student.getAcademicYear() + ".\n\n" +
            "        This certificate is issued upon the student's request for official purposes.";
        return buildPdf(student, body, null);
    }

    /** Custom body — used by DocumentController */
    public byte[] generatePdfWithContent(String studentId, String customContent, String docType)
            throws DocumentException {
        Student student = studentService.getStudentModelById(studentId);
        return buildPdf(student, customContent, docType);
    }

    // ── Core PDF builder ──────────────────────────────────────────
    private byte[] buildPdf(Student student, String bodyContent, String docType) throws DocumentException {

        LetterHead lh      = letterHeadService.get();
        String     dateStr = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));

        // Use the document type name as the title, fallback to "Certificate"
        String certTitle = (docType != null && !docType.isBlank())
                ? docType.trim()
                : "Certificate";

        Document          document = new Document(PageSize.A4, 50, 50, 40, 50);
        ByteArrayOutputStream out  = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();

        // Fonts
        Font trustFont     = new Font(Font.FontFamily.TIMES_ROMAN,  9, Font.ITALIC,  BaseColor.DARK_GRAY);
        Font collegeFont   = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLD,    new BaseColor(0, 51, 102));
        Font addressFont   = new Font(Font.FontFamily.TIMES_ROMAN,  8, Font.NORMAL,  BaseColor.DARK_GRAY);
        Font certTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD,    new BaseColor(0, 51, 102));
        Font bodyFont      = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL,  BaseColor.BLACK);
        Font boldBodyFont  = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD,    BaseColor.BLACK);
        Font sigFont       = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL,  BaseColor.BLACK);
        Font sigBoldFont   = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,    BaseColor.BLACK);

        // ── HEADER ────────────────────────────────────────────────
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{ 70f, 430f });

        // Logo box
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPaddingRight(8f);
        PdfPTable logoBox   = new PdfPTable(1);
        logoBox.setWidthPercentage(100);
        String    logoText  = nvl(lh.getLogoText(), "LOGO");
        PdfPCell  logoInner = new PdfPCell(new Phrase(logoText,
                new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE)));
        logoInner.setBackgroundColor(new BaseColor(0, 51, 102));
        logoInner.setBorder(Rectangle.BOX);
        logoInner.setBorderColor(new BaseColor(0, 51, 102));
        logoInner.setHorizontalAlignment(Element.ALIGN_CENTER);
        logoInner.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoInner.setPadding(10f);
        logoInner.setFixedHeight(64f);
        logoBox.addCell(logoInner);
        logoCell.addElement(logoBox);
        headerTable.addCell(logoCell);

        // Text column
        PdfPCell textCell = new PdfPCell();
        textCell.setBorder(Rectangle.NO_BORDER);
        textCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        textCell.setPaddingLeft(4f);

        if (!nvl(lh.getTrustName()).isEmpty())
            addCentered(textCell, lh.getTrustName(),   trustFont,   2f);
        if (!nvl(lh.getCollegeName()).isEmpty())
            addCentered(textCell, lh.getCollegeName(), collegeFont, 3f);
        if (!nvl(lh.getAddress()).isEmpty())
            addCentered(textCell, lh.getAddress(),     addressFont, 2f);

        String contactLine = buildContactLine(lh);
        if (!contactLine.isEmpty())
            addCentered(textCell, contactLine, addressFont, 2f);

        String webLine = buildWebLine(lh);
        if (!webLine.isEmpty())
            addCentered(textCell, webLine, addressFont, 0f);

        headerTable.addCell(textCell);
        document.add(headerTable);

        // ── DIVIDER ───────────────────────────────────────────────
        document.add(Chunk.NEWLINE);
        addDoubleBorder(writer, document);
        document.add(Chunk.NEWLINE);

        // ── CERTIFICATE TITLE ─────────────────────────────────────
        Paragraph certTitlePara = new Paragraph(certTitle, certTitleFont);
        certTitlePara.setAlignment(Element.ALIGN_CENTER);
        certTitlePara.setSpacingBefore(18f);
        certTitlePara.setSpacingAfter(24f);
        document.add(certTitlePara);
        addThinLine(writer, document);
        document.add(Chunk.NEWLINE);

        // ── BODY ──────────────────────────────────────────────────
        Paragraph toWhom = new Paragraph("To Whomsoever It May Concern", boldBodyFont);
        toWhom.setAlignment(Element.ALIGN_LEFT);
        toWhom.setSpacingAfter(14f);
        document.add(toWhom);

        for (String para : bodyContent.split("\n")) {
            if (para.trim().isEmpty()) {
                document.add(Chunk.NEWLINE);
            } else {
                Paragraph p = new Paragraph(para, bodyFont);
                p.setAlignment(Element.ALIGN_JUSTIFIED);
                p.setLeading(22f);
                p.setSpacingAfter(6f);
                document.add(p);
            }
        }

        // ── SIGNATURE ────────────────────────────────────────────
        PdfPTable sigTable = new PdfPTable(2);
        sigTable.setWidthPercentage(100);
        sigTable.setWidths(new float[]{ 1f, 1f });
        sigTable.setSpacingBefore(40f);

        PdfPCell dateCell = new PdfPCell();
        dateCell.setBorder(Rectangle.NO_BORDER);
        Paragraph dateLabel = new Paragraph("Date:", sigBoldFont);
        dateLabel.setSpacingAfter(4f);
        dateCell.addElement(dateLabel);
        dateCell.addElement(new Paragraph(dateStr, sigFont));
        sigTable.addCell(dateCell);

        PdfPCell sigCell = new PdfPCell();
        sigCell.setBorder(Rectangle.NO_BORDER);
        Paragraph sigLine = new Paragraph("_______________________________", sigFont);
        sigLine.setAlignment(Element.ALIGN_RIGHT);
        sigLine.setSpacingAfter(4f);
        sigCell.addElement(sigLine);
        Paragraph sigLabel = new Paragraph("Authorized Signatory", sigBoldFont);
        sigLabel.setAlignment(Element.ALIGN_RIGHT);
        sigCell.addElement(sigLabel);
        Paragraph principalLine = new Paragraph("Principal / Head of Department", sigFont);
        principalLine.setAlignment(Element.ALIGN_RIGHT);
        sigCell.addElement(principalLine);
        if (!nvl(lh.getCollegeName()).isEmpty()) {
            Paragraph instituteLine = new Paragraph(lh.getCollegeName(), sigFont);
            instituteLine.setAlignment(Element.ALIGN_RIGHT);
            sigCell.addElement(instituteLine);
        }
        sigTable.addCell(sigCell);

        document.add(sigTable);
        document.add(Chunk.NEWLINE);
        addDoubleBorder(writer, document);

        document.close();
        return out.toByteArray();
    }

    // ── Helpers ───────────────────────────────────────────────────
    private void addCentered(PdfPCell cell, String text, Font font, float after) {
        Paragraph p = new Paragraph(nvl(text), font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(after);
        cell.addElement(p);
    }

    private String buildContactLine(LetterHead lh) {
        StringBuilder sb = new StringBuilder();
        if (!nvl(lh.getPhone()).isEmpty())    sb.append("Tel: ").append(lh.getPhone());
        if (!nvl(lh.getTollFree()).isEmpty()) { if (sb.length() > 0) sb.append("  |  "); sb.append("Toll Free: ").append(lh.getTollFree()); }
        if (!nvl(lh.getFax()).isEmpty())      { if (sb.length() > 0) sb.append("  |  "); sb.append("Fax: ").append(lh.getFax()); }
        return sb.toString();
    }

    private String buildWebLine(LetterHead lh) {
        StringBuilder sb = new StringBuilder();
        if (!nvl(lh.getWebsite()).isEmpty()) sb.append("Web: ").append(lh.getWebsite());
        if (!nvl(lh.getEmail()).isEmpty())   { if (sb.length() > 0) sb.append("  |  "); sb.append("Email: ").append(lh.getEmail()); }
        return sb.toString();
    }

    private String nvl(String s)             { return s != null ? s : ""; }
    private String nvl(String s, String def) { return (s != null && !s.isEmpty()) ? s : def; }

    private void addDoubleBorder(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        float left  = document.left();
        float right = document.right();
        float y     = writer.getVerticalPosition(true) - 2f;
        canvas.setLineWidth(1.5f);
        canvas.setColorStroke(new BaseColor(0, 51, 102));
        canvas.moveTo(left, y); canvas.lineTo(right, y); canvas.stroke();
        canvas.setLineWidth(0.5f);
        canvas.setColorStroke(new BaseColor(0, 51, 102));
        canvas.moveTo(left, y - 3.5f); canvas.lineTo(right, y - 3.5f); canvas.stroke();
    }

    private void addThinLine(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        float left  = document.left();
        float right = document.right();
        float y     = writer.getVerticalPosition(true) - 2f;
        canvas.setLineWidth(0.75f);
        canvas.setColorStroke(new BaseColor(100, 100, 100));
        canvas.moveTo(left, y); canvas.lineTo(right, y); canvas.stroke();
    }
}
