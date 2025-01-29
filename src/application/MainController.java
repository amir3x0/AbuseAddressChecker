package application;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import server.Report;
import server.FraudDetectionFacade;

import java.io.*;
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
    public void initialize() {
        loadLogFromFile();      
    }

    private void loadLogFromFile() {
        File logFile = new File("log.txt");
        if (!logFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                logList.getItems().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddAddress() {
        String addresses = addressInput.getText();
        processAddresses(Arrays.asList(addresses.split("\n")));
    }

    private void processAddresses(List<String> addresses) {
        try {
            List<Report> results = facade.scanAddresses(addresses);
            resultsTable.getItems().addAll(results);
            addLogEntry("Scanned " + addresses.size() + " addresses, found " + results.size() + " suspicious addresses.");
        } catch (Exception e) {
            e.printStackTrace();
            addLogEntry("Error scanning addresses: " + e.getMessage());
        }
    }

    @FXML
    private void handleClearTable() {
        resultsTable.getItems().clear();
        addLogEntry("Table cleared");
    }

    @FXML
    private void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            List<String> addresses = loadAddressesFromFile(file);
            processAddresses(addresses);
        }
    }

    private List<String> loadAddressesFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            return scanner.useDelimiter("\\Z").next().lines().toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @FXML
    private void handleExportExcel() {
        FileChooser fileChooser = new FileChooser();
        // Change the extension filter to CSV
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            facade.saveToExcel(resultsTable.getItems(), file.getPath());
            addLogEntry("Exported to CSV: " + file.getName());
        }
    }


    private void addLogEntry(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss"));
        logList.getItems().add(timestamp + " " + message);
    }
}
