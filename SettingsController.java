package org.example.newbrow1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.net.URL;

public class SettingsController {

    @FXML
    private VBox myVBox;
    @FXML
    private Button clearHistoryButton;
    @FXML
    private Button disableJs;

    @FXML
    private Button blackThemeButton;
    @FXML
    private TabPane tabPane;
    private BrowserSettings settings;

    private HelloController helloController;
    @FXML
    private boolean theme;
    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    public void setSettings(BrowserSettings settings) {
        this.settings = settings;
        disableJs.setText(settings.jsEnabled ? "Отключить JS" : "Включить JS");
        blackThemeButton.setText(settings.darkTheme ? "Светлая тема" : "Темная тема");
    }

    @FXML
    protected void onClearHistoryButton() throws Exception {
        settings.clearHistory();
        System.out.println("История очищена.");
    }

    @FXML
    protected void onDisableJSButton() {
        settings.jsEnabled = !settings.jsEnabled;
        disableJs.setText(settings.jsEnabled ? "Отключить JS" : "Включить JS");
        System.out.println(settings.jsEnabled ? "JavaScript включен." : "JavaScript отключен.");
    }

    @FXML
    protected void onBlackThemeButtonClick(ActionEvent event) {
//        settings.darkTheme = !settings.darkTheme;
//        String newColor = settings.darkTheme ? "black" : "white";
//        if (helloController != null) {
//            helloController.setVBoxBackgroundColor(newColor);
//        } else {
//            System.err.println("HelloController not set!");
//        }
//        blackThemeButton.setText(settings.darkTheme ? "Светлая тема" : "Темная тема");
//        System.out.println(settings.darkTheme ? "Темная тема активирована." : "Светлая тема активирована.");



        //this.theme = theme;

//        HelloController h = helloController;


        if(this.helloController.theme == true) {
            this.helloController.myVBox.setStyle("-fx-background-color: lightgrey");
            this.helloController.theme = false;
        }else {
            this.helloController.myVBox.setStyle("-fx-background-color: white");
            this.helloController.theme = true;
        }
    }

    private void updateTheme() {
        Scene scene = tabPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            String stylesheetPath = settings.darkTheme ? "/darkTheme.css" : "/lightTheme.css";
            URL stylesheetURL = getClass().getResource(stylesheetPath);
            if (stylesheetURL != null) {
                scene.getStylesheets().add(stylesheetURL.toExternalForm());
            } else {
                System.err.println("Error: Stylesheet not found: " + stylesheetPath);
            }
        }
    }

    public void onDontWriteHistoryClick(ActionEvent actionEvent) {

    }


}

