package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class DataSaver {

    // Save to log.txt
    public void saveToLog(List<Report> reports) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            for (Report r : reports) {
                writer.println(r.getAddress() + " | " + r.getAbuseCount());
            }
        }
    }

    // "Export to Excel" but actually create a CSV file for simplicity
    public void saveToExcel(List<Report> reports, String filePath) throws IOException {
        // We'll generate CSV content here
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Optionally write a header row:
            writer.println("Address,AbuseCount,ReportLink");

            // Write the data rows:
            for (Report r : reports) {
                writer.println(r.getAddress() + "," + r.getAbuseCount() + "," + r.getReportLink());
            }
        }
    }
}
