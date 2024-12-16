package org.example.newbrow1;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

public class historyController extends Application {

    @FXML
    private TextArea textArea1;

    @FXML
    private Button buttonShowHistory;

    @FXML
    private Button buttonClearHistory;

    @FXML
    public void initialize() {


    }


    @FXML
    protected void onButtonClickShowHistory() {


        String historyText = readHistoryFromFile();
        textArea1.setText(historyText);
        if (Objects.equals(textArea1.toString(), "")){
            textArea1.setText("История пуста");
        }

    }

    @FXML
    protected void onButtonClearHistory() {
        clearHistoryFile();
        textArea1.clear();
    }

    private String readHistoryFromFile() {
        StringBuilder historyContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("browser_history.txt"))) {
            historyContent.append(reader.lines().collect(Collectors.joining("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return historyContent.toString();
    }

    private void clearHistoryFile() {
        Path filePath = Paths.get("browser_history.txt");
        try {
            Files.writeString(filePath, ""); // Overwrites the file with an empty string
        } catch (IOException e) {
            e.printStackTrace(); // Better error handling below
            // More robust error handling: Log the error or show a user-friendly message
            System.err.println("Error clearing history file: " + e.getMessage());
        }
    }



    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("browser_history.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.getIcons().add(new Image("/icon.jpg"));
        stage.setScene(scene);
        stage.show();
    }
}

