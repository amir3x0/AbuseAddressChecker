package application;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import server.Report;
import server.FraudDetectionFacade;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class MainController {
	
	@FXML private TextArea addressInput;
	@FXML private TableView<Report> resultsTable;
	@FXML private ListView<String> logList;
	
    private FraudDetectionFacade facade = new FraudDetectionFacade();

    
    @FXML
    private void handleAddAddress() {
        String addresses = addressInput.getText();
        processAddresses(Arrays.asList(addresses.split("\n")));
    }
    
    @FXML
    private void handleClearTable() {
        resultsTable.getItems().clear();
        addLogEntry("Table cleared");
    }
    
    private void addLogEntry(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
        logList.getItems().add(timestamp + " " + message);
    }
    
    
    @FXML
    private void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            List<String> addresses = loadAddressesFromFile(file);
            try {
                List<Report> results = facade.scanAddresses(addresses);
                resultsTable.getItems().setAll(results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleExportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            facade.saveToExcel(resultsTable.getItems(), file.getPath());
        }
    }

    private List<String> loadAddressesFromFile(File file) {
        // טעינת כתובות מקובץ טקסט
        try (Scanner scanner = new Scanner(file)) {
            return scanner.useDelimiter("\\Z").next().lines().toList();
        } catch (Exception e) {
            return List.of();
        }
    }
}