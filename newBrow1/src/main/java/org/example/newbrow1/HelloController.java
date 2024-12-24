package org.example.newbrow1;

import javafx.collections.ListChangeListener;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;








public class HelloController {
    @FXML
    public VBox myVBox;
    @FXML
    private Button blackThemeButton;


    private SettingsController settingsController;

    private WebEngine engine;
    @FXML
    private WebView welcomePage;
    @FXML
    private TextField searchField;
    @FXML
    private TabPane tabPageid;
    @FXML
    private Button plusButton;
    @FXML
    private Button backButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button noJsButton;
    @FXML
    private Button restartButton;

    private WebEngine webEngine;
    private WebHistory history;

    private boolean isJavaScriptEnabled = true;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss");
    private final String HISTORY_FILE = "browser_history.txt";
    private BrowserSettings settings = new BrowserSettings();


    public boolean theme;







    @FXML
    public void initialize() {


    theme =true;


        engine = welcomePage.getEngine();
        loadHomePage();
        history = engine.getHistory();





        // Listen for page loading completion AND history changes
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                saveHistoryToFile();
            }
        });

        history.currentIndexProperty().addListener((observable, oldValue, newValue) -> saveHistoryToFile());

        updateJavaScriptState();
        updateTheme();

        engine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            updateTabTitle(newLocation);
        });




        tabPageid.getTabs().addListener((ListChangeListener<Tab>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Tab tab : change.getAddedSubList()) {
                        setupTab(tab);
                    }
                }

            }
        });


        for (Tab tab : tabPageid.getTabs()) {
            setupTab(tab);
        }

    }

    private void setupTab(Tab tab) {


        if (tab.getContent() instanceof WebView) {
            WebEngine engine = ((WebView) tab.getContent()).getEngine();

            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED && settings.writeHistory) {
                    saveHistoryForEngine(engine);
                }
            });

            engine.getHistory().currentIndexProperty().addListener((observable, oldValue, newValue) -> {
                updateNavigationButtons();
                if (settings.writeHistory) {
                    saveHistoryForEngine(engine);
                }
            });

            engine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
                updateTabTitle(newLocation);
            });
        }


    }



    private void saveHistoryForEngine(WebEngine engine) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) { // Режим добавления
            for (WebHistory.Entry entry : engine.getHistory().getEntries()) {
                writer.write(entry.getUrl() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving history to file: " + e.getMessage());
        }
    }



    public void setVBoxBackgroundColor(String color) {
        myVBox.setStyle("-fx-background-color: " + color + ";");
    }



    private void updateNavigationButtons() {
        WebEngine engine = getActiveWebEngine();
        if (engine != null) {
            WebHistory history = engine.getHistory();
            int currentIndex = history.getCurrentIndex();
            backButton.setDisable(currentIndex <= 0);
            forwardButton.setDisable(currentIndex >= history.getEntries().size() - 1);
        } else {
            backButton.setDisable(true);
            forwardButton.setDisable(true);
        }
    }



    public void updateJavaScriptState() {
        for (Tab tab : tabPageid.getTabs()) {
            Object content = tab.getContent();
            if (content instanceof WebView) {
                ((WebView) content).getEngine().setJavaScriptEnabled(settings.jsEnabled);
            } else if (content instanceof AnchorPane) {
                WebView webView = findWebView((AnchorPane) content);
                if (webView != null) {
                    webView.getEngine().setJavaScriptEnabled(settings.jsEnabled);
                } else {
                    System.err.println("WebView not found in AnchorPane");
                }
            } else {
                System.err.println("Unexpected content type in Tab: " + content.getClass().getName());
            }
        }
    }

    private WebView findWebView(AnchorPane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof WebView) {
                return (WebView) node;
            }
        }
        return null;
    }

    public void updateTheme() {
        Scene scene = tabPageid.getScene();
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

    @FXML
    protected void showSettings(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Parent root = loader.load();
        SettingsController settingsController = loader.getController();
        settingsController.setSettings(settings);
        settingsController.setHelloController(this);
        showNewStage(root, "Settings");

    }

    private void loadURL(String url) {
        WebEngine engine = getActiveWebEngine();
        if (engine != null && !url.isEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            engine.load(url);
        }
    }

    private void loadHomePage() {
        URL url = getClass().getResource("/org/example/newbrow1/homePage.html");
        if (url != null) {
            engine.load(url.toExternalForm());
        } else {
            System.err.println("Error: homePage.html not found.");
        }
    }

    @FXML
    protected void onSearchButtonClick() {

        String url = searchField.getText();
        if (!url.isEmpty()) {
            loadURL(url);
        }




    }



    private void updateTabTitle(String url) {
        Tab selectedTab = tabPageid.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            String title = url;
            if (title.length() > 20) {
                title = title.substring(0, 20) + "...";
            }
            selectedTab.setText(title);
        }
    }

    @FXML
    protected void onPlusButtonClick() {
        createAndSelectNewTab("https://www.google.com", "Google");
    }

    @FXML
    protected void onMinusButtonClick() {
        removeActiveTab();
    }

    @FXML
    protected void onHistoryButtonClick() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("history.fxml"));
        Parent root = loader.load();

        showNewStage(root, "History");
    }



    @FXML
    protected void onSettingsButtonClick() throws IOException {
        showSettings(null);
    }


    private void createAndSelectNewTab(String url, String title) {

                WebView newWebView = new WebView();
        WebEngine newEngine = newWebView.getEngine();

        String customUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"; // Replace with your desired User-Agent
        newEngine.setUserAgent(customUserAgent);


        newEngine.load(url);
        newEngine.locationProperty().addListener((obs, oldLocation, newLocation) -> {
            updateTabTitle(newLocation);
            updateNavigationButtons();
        });

        newEngine.getHistory().currentIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updateNavigationButtons();
        });


        Tab newTab = new Tab(title, newWebView);
        tabPageid.getTabs().add(newTab);
        tabPageid.getSelectionModel().select(newTab);
        logEvent(title);


    }

    private void removeActiveTab() {
        Tab selectedTab = tabPageid.getSelectionModel().getSelectedItem();
        if (selectedTab != null && tabPageid.getTabs().size() > 1) {
            tabPageid.getTabs().remove(selectedTab);
            logEvent("Tab removed");
        }
    }

    private void showNewStage(Parent root, String title) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.getIcons().add(new Image("/icon.jpg"));
        stage.show();
    }


    private WebEngine getActiveWebEngine() {
        Tab selectedTab = tabPageid.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            Node content = selectedTab.getContent();
            if (content instanceof WebView) {
                return ((WebView) content).getEngine();
            } else if (content instanceof AnchorPane) {
                WebView webView = findWebView((AnchorPane) content);

                return webView != null ? webView.getEngine() : null;

            }
        }
        return null;
    }

    private void toggleJavaScript() {
        settings.jsEnabled = !settings.jsEnabled;
        updateJavaScriptState();
        logEvent("JavaScript enabled: " + settings.jsEnabled);
    }

    private void logEvent(String event) {
        System.out.println(dateFormat.format(Calendar.getInstance().getTime()) + "  " + event);
    }


    @FXML
    public void onMouseExited(MouseEvent mouseEvent) { }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) {
        WebEngine engine = getActiveWebEngine();
        if (engine != null && engine.getHistory().getCurrentIndex() > 0) {
            engine.getHistory().go(-1);
        }
    }

    @FXML
    public void onForwardButtonClick(ActionEvent actionEvent) {
        WebEngine engine = getActiveWebEngine();
        if (engine != null && engine.getHistory().getCurrentIndex() < engine.getHistory().getEntries().size() - 1) {
            engine.getHistory().go(1);
        }
    }

    @FXML
    public void onNoJs(ActionEvent actionEvent) {
        toggleJavaScript();
    }

    @FXML
    public void onRestartBUttonClick(ActionEvent actionEvent) {
        WebEngine currentEngine = getActiveWebEngine();
        if (currentEngine != null) {
            currentEngine.reload();
        }
    }

    private void saveHistoryToFile() {
        if (!settings.writeHistory) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE,true))) {
            WebEngine engine = getActiveWebEngine();
            if (engine != null) {
                for (WebHistory.Entry entry : engine.getHistory().getEntries()) {
                    writer.write(entry.getUrl() + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving history to file: " + e.getMessage());
        }
    }


    public void settVBoxBackgroudColor(ActionEvent actionEvent) {
    if(theme == true) {
        myVBox.setStyle("-fx-background-color: lightgrey");
        theme = false;
    }else {
        myVBox.setStyle("-fx-background-color: white");
        theme = true;
    }
    }
}























