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
    private CheckBox disableHistoryCheckBox;
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
        disableHistoryCheckBox.setSelected(!settings.writeHistory); // Инициализация состояния
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
        if (helloController != null) {
            helloController.updateJavaScriptState();
        }
        System.out.println(settings.jsEnabled ? "JavaScript включен." : "JavaScript отключен.");
    }
    @FXML
    protected void onBlackThemeButtonClick(ActionEvent event) {
         settings.darkTheme = !settings.darkTheme;
        blackThemeButton.setText(settings.darkTheme ? "Светлая тема" : "Темная тема");
        if (helloController != null) {
            helloController.updateTheme();
            helloController.setVBoxBackgroundColor(settings.darkTheme ? " lightgrey" : "white");
        }
        System.out.println(settings.darkTheme ? "Темная тема активирована." : "Светлая тема активирована.");
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
        settings.writeHistory = !disableHistoryCheckBox.isSelected();
        System.out.println(settings.writeHistory ? "История записывается." : "История не будет записываться.");
    }



}

