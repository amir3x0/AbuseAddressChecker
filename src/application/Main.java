package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file from the same package.
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            
            primaryStage.setTitle("Checks invalid Bitcoin addresses");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
