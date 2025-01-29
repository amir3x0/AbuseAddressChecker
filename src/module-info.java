module AbuseAddressChecker {
	requires javafx.controls;
	requires java.net.http;
	requires javafx.fxml;
	requires com.google.gson; 
    requires org.apache.poi.ooxml; 
	
	opens application to javafx.graphics, javafx.fxml;
	opens server to com.google.gson;
}
