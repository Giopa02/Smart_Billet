package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.Main;

public class SelectionController {

    @FXML private Button btnClient;
    @FXML private Button btnAdmin;

    @FXML
    private void handleClient() {
        Main.naviguerVers("/fxml/ClientLoginView.fxml");
    }

    @FXML
    private void handleAdmin() {
        Main.naviguerVers("/fxml/LoginView.fxml");
    }
}