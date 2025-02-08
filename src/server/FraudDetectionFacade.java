// FraudDetectionFacade.java
package server;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class FraudDetectionFacade {
    private ChainAbuseAPIClient apiClient = ChainAbuseAPIClient.getInstance();
    private AddressValidator validator = new AddressValidator();
    private DataSaver dataSaver = new DataSaver();

    public List<Report> scanAddresses(List<String> addresses) throws Exception {
        List<Report> allReports = new ArrayList<>();
        for (String addr : addresses) {
            addr = addr.trim();
            if (!addr.isEmpty()) {
                allReports.addAll(scanAddress(addr));
            }
        }
        dataSaver.saveToLog(allReports);
        return allReports;
    }

    public List<Report> scanAddress(String address) throws Exception {
        if (!validator.isValid(address)) {
            throw new IllegalArgumentException("Invalid address format");
        }
        return apiClient.checkAddress(address);
    }

    public boolean isValidAddress(String address) {
        return validator.isValid(address);
    }

    public void saveToExcel(List<Report> reports, String path) throws IOException {
        dataSaver.saveToExcel(reports, path);
    }
}