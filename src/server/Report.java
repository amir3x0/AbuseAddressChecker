package server;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class Report {
    private SimpleStringProperty address;
    private SimpleIntegerProperty abuseCount;
    private SimpleStringProperty reportLink;

    // Constructor
    public Report(String address, int abuseCount, String reportLink) {
        this.address = new SimpleStringProperty(address);
        this.abuseCount = new SimpleIntegerProperty(abuseCount);
        this.reportLink = new SimpleStringProperty(reportLink);
    }

    // Getters for JavaFX Properties
    public SimpleStringProperty addressProperty() { return address; }
    public SimpleIntegerProperty abuseCountProperty() { return abuseCount; }
    public SimpleStringProperty reportLinkProperty() { return reportLink; }

    // Standard getters
    public String getAddress() { return address.get(); }
    public int getAbuseCount() { return abuseCount.get(); }
    public String getReportLink() {
        if (reportLink.get().startsWith("http")) {
            return reportLink.get();
        } else {
            return "No report available";
        }
    }
    
    @Override
    public String toString() {
        if (abuseCount.get() == 0) return "Clean address";
        if (abuseCount.get() == -1) return "Error occurred";
        return "Suspicious activity detected";
    }

}