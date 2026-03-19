package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URL;

import java.io.IOException;

public class Main extends Application {

    private static StackPane root;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        root = new StackPane();

        Scene scene = new Scene(root);
        stage.setTitle("Smart Billet");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);

        naviguerVers("/fxml/SelectionView.fxml");
        stage.show();
    }

    public static void naviguerVers(String fxmlPath) {
        try {
            URL location = Main.class.getResource(fxmlPath);
            if (location == null) {
                System.err.println("FXML introuvable : " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Object vue = loader.load();
            root.getChildren().clear();
            root.getChildren().add((javafx.scene.Node) vue);
        } catch (IOException e) {
            System.err.println("Erreur navigation : " + e.getMessage());
        }
    }

    public static void naviguerVers(String fxmlPath, java.util.function.Consumer<Object> callback) {
        try {
            URL location = Main.class.getResource(fxmlPath);
            if (location == null) {
                System.err.println("FXML introuvable : " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Object vue = loader.load();
            callback.accept(loader.getController());
            root.getChildren().clear();
            root.getChildren().add((javafx.scene.Node) vue);
        } catch (IOException e) {
            System.err.println("Erreur navigation : " + e.getMessage());
        }
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}