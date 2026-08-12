package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class SecondaryController {

    @FXML
    private TextField nombreField;

    @FXML
    private TextField apellidoField;

    @FXML
    private TextField emailField;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void guardarPersona() {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String email = emailField.getText().trim();

        // Validar campos
        if (nombre.isEmpty() || apellido.isEmpty()) {
            mostrarAlerta("Error", "El nombre y apellido son obligatorios.");
            return;
        }

        ConexionDB.insertarPersona(nombre, apellido, email);

        // Limpiar campos
        nombreField.clear();
        apellidoField.clear();
        emailField.clear();

        mostrarAlerta("Éxito", "Persona guardada correctamente.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
