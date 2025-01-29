package server;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.List;

public class DataSaver {
    // שמור ל-log.txt
    public void saveToLog(List<Report> reports) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            for (Report r : reports) {
                writer.println(r.getAddress() + " | " + r.getAbuseCount());
            }
        }
    }

    // שמור לאקסל
    public void saveToExcel(List<Report> reports, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reports");

        int rowNum = 0;
        for (Report r : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(r.getAddress());
            row.createCell(1).setCellValue(r.getAbuseCount());
            row.createCell(2).setCellValue(r.getReportLink());
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        }
        workbook.close();
    }
}