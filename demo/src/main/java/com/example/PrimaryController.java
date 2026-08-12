package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {

    @FXML
    private TableView<Persona> tablaPersonas;

    @FXML
    private TableColumn<Persona, Integer> colId;

    @FXML
    private TableColumn<Persona, String> colNombre;

    @FXML
    private TableColumn<Persona, String> colApellido;

    @FXML
    private TableColumn<Persona, String> colEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar las columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Cargar datos
        cargarPersonas();
    }

    private void cargarPersonas() {
        ObservableList<Persona> personas = ConexionDB.obtenerTodasLasPersonas();
        tablaPersonas.setItems(personas);
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void editarPersona() {
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();

        if (personaSeleccionada == null) {
            mostrarAlerta("Selecciona una persona", "Por favor selecciona una persona para editar.");
            return;
        }

        // Diálogos para editar
        String nuevoNombre = pedirInput("Nuevo nombre", personaSeleccionada.getNombre());
        if (nuevoNombre == null) return;

        String nuevoApellido = pedirInput("Nuevo apellido", personaSeleccionada.getApellido());
        if (nuevoApellido == null) return;

        String nuevoEmail = pedirInput("Nuevo email", personaSeleccionada.getEmail());
        if (nuevoEmail == null) return;

        // Actualizar en la base de datos
        ConexionDB.actualizarPersona(personaSeleccionada.getId(), nuevoNombre, nuevoApellido, nuevoEmail);

        // Recargar la tabla
        cargarPersonas();
        mostrarAlerta("Éxito", "Persona actualizada correctamente.");
    }

    @FXML
    private void eliminarPersona() {
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();

        if (personaSeleccionada == null) {
            mostrarAlerta("Selecciona una persona", "Por favor selecciona una persona para eliminar.");
            return;
        }

        // Confirmar eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Eliminar a " + personaSeleccionada.getNombre() + " " + personaSeleccionada.getApellido() + "?");

        if (confirmacion.showAndWait().orElse(null) == javafx.scene.control.ButtonType.OK) {
            ConexionDB.eliminarPersona(personaSeleccionada.getId());
            cargarPersonas();
            mostrarAlerta("Éxito", "Persona eliminada correctamente.");
        }
    }

    private String pedirInput(String titulo, String valorActual) {
        TextInputDialog dialogo = new TextInputDialog(valorActual);
        dialogo.setTitle(titulo);
        dialogo.setHeaderText(titulo);
        dialogo.setContentText("Ingresa el nuevo valor:");

        Optional<String> resultado = dialogo.showAndWait();
        return resultado.orElse(null);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
