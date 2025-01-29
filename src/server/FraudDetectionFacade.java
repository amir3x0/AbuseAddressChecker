package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FraudDetectionFacade {
    private ChainAbuseAPIClient apiClient = ChainAbuseAPIClient.getInstance();
    private AddressValidator validator = new AddressValidator();
    private DataSaver dataSaver = new DataSaver();

    public List<Report> scanAddresses(List<String> addresses) throws Exception{
        List<Report> reports = new ArrayList<>();
        for (String addr : addresses) {
            if (validator.isValid(addr)) {
                reports.addAll(apiClient.checkAddress(addr));  // throws Exception
            }
        }
        dataSaver.saveToLog(reports); // throws IOException, אבל IOException יורש מ-Exception
        return reports;
    }


    public void saveToExcel(List<Report> reports, String path) {
        try {
			dataSaver.saveToExcel(reports, path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}