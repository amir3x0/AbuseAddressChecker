package server;

import java.util.ArrayList;
import java.util.List;

public class FraudDetectionFacade {
    private ChainAbuseAPIClient apiClient = ChainAbuseAPIClient.getInstance();
    private AddressValidator validator = new AddressValidator();
    private DataSaver dataSaver = new DataSaver();

    public List<Report> scanAddresses(List<String> addresses) throws Exception {
        List<Report> reports = new ArrayList<>();
        for (String addr : addresses) {
            if (validator.isValid(addr)) {
                reports.addAll(apiClient.checkAddress(addr));
            }
        }
        dataSaver.saveToLog(reports);
        return reports;
    }

    public void saveToExcel(List<Report> reports, String path) {
        dataSaver.saveToExcel(reports, path);
    }
}