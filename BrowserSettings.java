package org.example.newbrow1;

import javafx.beans.Observable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BrowserSettings {
    public boolean jsEnabled = true;
    public boolean darkTheme = false;
    public String historyFilePath = "browser_history.txt";

    public void clearHistory() throws IOException {
        Files.deleteIfExists(Paths.get(historyFilePath));
    }

    public Observable darkThemeProperty() {
        return null;
    }
}
