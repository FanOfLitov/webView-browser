package org.example.newbrow1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class browser extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Загружаем FXML файл
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//
//        FXMLLoader settings = new FXMLLoader(getClass().getResource("settings.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setTitle("Web Browser");
//
//        stage.getIcons().add(new Image("/icon.jpg"));
//        stage.setMaximized(true);
//        stage.setScene(scene);
//        stage.show();

        FXMLLoader helloLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = helloLoader.load();
        HelloController helloController = helloLoader.getController();

        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Parent settingsRoot = settingsLoader.load();
        SettingsController settingsController = settingsLoader.getController();


        if (helloController != null) {
            settingsController.setHelloController(helloController);
            System.out.println("HelloController successfully set!"); //Debugging message
        } else {
            System.err.println("ERROR: helloController is NULL! Check hello-view.fxml loading.");
        }


        if (settingsController == null) {
            System.err.println("ERROR: settingsController is NULL! Check settings.fxml loading.");
        }

        Scene scene = new Scene(root);
                stage.getIcons().add(new Image("/icon.jpg"));
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("Web Browser");
        stage.show();
    }






    }

