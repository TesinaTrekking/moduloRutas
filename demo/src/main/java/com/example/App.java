package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static Ruta rutaEnEdicion;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("rutas"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void prepararEdicion(Ruta ruta) throws IOException {
        rutaEnEdicion = ruta;
        setRoot("ruta-form");
    }

    static Ruta consumirRutaEnEdicion() {
        Ruta ruta = rutaEnEdicion;
        rutaEnEdicion = null;
        return ruta;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        ConexionDB.crearTabla();
        launch();
    }

}
