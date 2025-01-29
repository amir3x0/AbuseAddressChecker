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
    public String getReportLink() { return reportLink.get(); }
}