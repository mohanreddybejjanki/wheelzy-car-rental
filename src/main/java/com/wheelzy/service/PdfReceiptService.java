package com.wheelzy.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.wheelzy.model.Booking;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfReceiptService {

    private static final DeviceRgb LIGHT_BLUE = new DeviceRgb(66, 165, 245);
    private static final DeviceRgb DARK_BLUE = new DeviceRgb(25, 118, 210);
    private static final DeviceRgb WHITE = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(240, 248, 255);

    public byte[] generateReceipt(Booking booking) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);
            doc.setMargins(30, 40, 30, 40);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
            DateTimeFormatter dtfFull = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

            // ---- HEADER ----
            Table header = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
            Cell headerCell = new Cell();
            headerCell.setBackgroundColor(DARK_BLUE);
            headerCell.setPadding(20);

            Paragraph logo = new Paragraph("🚗 Wheelzy Rentals")
                    .setFontSize(26)
                    .setBold()
                    .setFontColor(WHITE)
                    .setTextAlignment(TextAlignment.CENTER);
            headerCell.add(logo);

            Paragraph tagline = new Paragraph("Peer to Peer Car Rental | Hyderabad")
                    .setFontSize(11)
                    .setFontColor(new DeviceRgb(200, 230, 255))
                    .setTextAlignment(TextAlignment.CENTER);
            headerCell.add(tagline);
            header.addCell(headerCell);
            doc.add(header);

            doc.add(new Paragraph(" "));

            // ---- RECEIPT TITLE ----
            Paragraph receiptTitle = new Paragraph("BOOKING RECEIPT")
                    .setFontSize(18)
                    .setBold()
                    .setFontColor(DARK_BLUE)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(receiptTitle);

            Paragraph refLine = new Paragraph("Ref: " + booking.getBookingReference())
                    .setFontSize(11)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(refLine);

            doc.add(new Paragraph(" "));

            // Status badge
            String statusText = "Status: " + booking.getStatus().name();
            DeviceRgb statusColor = booking.getStatus() == Booking.BookingStatus.APPROVED
                    ? new DeviceRgb(46, 125, 50)
                    : booking.getStatus() == Booking.BookingStatus.PENDING
                    ? new DeviceRgb(230, 81, 0) : new DeviceRgb(198, 40, 40);

            Paragraph statusPara = new Paragraph(statusText)
                    .setFontSize(12).setBold().setFontColor(statusColor)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(statusPara);

            doc.add(new Paragraph(" "));

            // ---- RENTER INFO ----
            addSectionTitle(doc, "Renter Information");
            Table renterTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            renterTable.setBackgroundColor(LIGHT_GRAY);
            addRow(renterTable, "Name", booking.getRenter().getName());
            addRow(renterTable, "Email", booking.getRenter().getEmail());
            addRow(renterTable, "Phone", booking.getRenter().getPhone());
            if (booking.getRenter().getLicenseNumber() != null) {
                addRow(renterTable, "License No.", booking.getRenter().getLicenseNumber());
            }
            doc.add(renterTable);

            doc.add(new Paragraph(" "));

            // ---- CAR INFO ----
            addSectionTitle(doc, "Vehicle Information");
            Table carTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            carTable.setBackgroundColor(LIGHT_GRAY);
            addRow(carTable, "Vehicle", booking.getCar().getDisplayName());
            addRow(carTable, "Color", booking.getCar().getColor());
            addRow(carTable, "RC Number", booking.getCar().getRegistrationNumber());
            addRow(carTable, "PUC Number", booking.getCar().getPucNumber());
            addRow(carTable, "Fuel Type", booking.getCar().getFuelType().name());
            addRow(carTable, "Transmission", booking.getCar().getTransmission().name());
            addRow(carTable, "Seats", String.valueOf(booking.getCar().getSeats()));
            addRow(carTable, "Pickup Location", booking.getCar().getLocation() + ", Hyderabad");
            doc.add(carTable);

            doc.add(new Paragraph(" "));

            // ---- OWNER INFO ----
            addSectionTitle(doc, "Owner Information");
            Table ownerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            ownerTable.setBackgroundColor(LIGHT_GRAY);
            addRow(ownerTable, "Owner Name", booking.getCar().getOwner().getName());
            addRow(ownerTable, "Owner Phone", booking.getCar().getOwner().getPhone());
            doc.add(ownerTable);

            doc.add(new Paragraph(" "));

            // ---- BOOKING DETAILS ----
            addSectionTitle(doc, "Booking Details");
            Table bookingTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            bookingTable.setBackgroundColor(LIGHT_GRAY);
            addRow(bookingTable, "Start Date", booking.getStartDate().format(dtf));
            addRow(bookingTable, "End Date", booking.getEndDate().format(dtf));
            addRow(bookingTable, "Total Days", booking.getTotalDays() + " day(s)");
            addRow(bookingTable, "Booked On", booking.getBookedAt().format(dtfFull));
            if (booking.getApprovedAt() != null) {
                addRow(bookingTable, "Approved On", booking.getApprovedAt().format(dtfFull));
            }
            doc.add(bookingTable);

            doc.add(new Paragraph(" "));

            // ---- PRICE BREAKDOWN ----
            addSectionTitle(doc, "Price Breakdown");
            Table priceTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
            addRow(priceTable, "Price Per Day", "₹" + booking.getPricePerDay());
            addRow(priceTable, "Number of Days", String.valueOf(booking.getTotalDays()));

            // Total row - highlighted
            Cell totalLabel = new Cell().add(new Paragraph("TOTAL AMOUNT").setBold().setFontSize(14).setFontColor(WHITE));
            totalLabel.setBackgroundColor(DARK_BLUE).setPadding(10);
            Cell totalValue = new Cell().add(new Paragraph("₹" + booking.getTotalAmount()).setBold().setFontSize(14).setFontColor(WHITE));
            totalValue.setBackgroundColor(DARK_BLUE).setPadding(10).setTextAlignment(TextAlignment.RIGHT);
            priceTable.addCell(totalLabel);
            priceTable.addCell(totalValue);
            doc.add(priceTable);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(" "));

            // ---- FOOTER ----
            Paragraph footer = new Paragraph("Thank you for choosing Wheelzy Rentals! | Hyderabad, Telangana\nFor support: support@wheelzyrentals.com | +91-9876543210")
                    .setFontSize(9)
                    .setFontColor(ColorConstants.GRAY)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(footer);

            Paragraph terms = new Paragraph("Terms: Minimum rental is 1 day. Valid DL required at pickup. Fuel not included.")
                    .setFontSize(8)
                    .setFontColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER);
            doc.add(terms);

            doc.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private void addSectionTitle(Document doc, String title) {
        Paragraph p = new Paragraph(title)
                .setFontSize(13)
                .setBold()
                .setFontColor(DARK_BLUE)
                .setMarginBottom(4);
        doc.add(p);
    }

    private void addRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setBold().setFontSize(10))
                .setPadding(6)
                .setBackgroundColor(new DeviceRgb(227, 242, 253));
        Cell valueCell = new Cell()
                .add(new Paragraph(value != null ? value : "-").setFontSize(10))
                .setPadding(6);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
