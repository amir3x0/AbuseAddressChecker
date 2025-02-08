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
 // DataSaver.java
    public void saveToExcel(List<Report> reports, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println("Address,Status,Abuse Count,Report Link");
            for (Report r : reports) {
                String status = r.getAbuseCount() > 0 ? "Suspicious" : 
                              r.getAbuseCount() == -1 ? "Error" : "Clean";
                writer.println(String.join(",",
                    r.getAddress(),
                    status,
                    String.valueOf(r.getAbuseCount()),
                    r.getReportLink()
                ));
            }
        }
    }
}
