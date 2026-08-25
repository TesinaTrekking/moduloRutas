package com.example;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RutaFormController {

        @FXML
        private Label tituloForm;

        private Ruta rutaEnEdicion;

        @FXML
        private TextField nombreField;

        @FXML
        private TextField latitudInicialField;

        @FXML
        private TextField longitudInicialField;

        @FXML
        private TextField altitudMaximaField;

        @FXML
        private ComboBox<String> tipoTerrenoCombo;

        @FXML
        private ComboBox<String> dificultadTecnicaCombo;

        @FXML
        private ComboBox<String> dificultadFisicaCombo;

        @FXML
        private void volverListado() throws IOException {
                App.setRoot("rutas");
        }

        @FXML
        public void initialize() {

                tipoTerrenoCombo.getItems().addAll(
                                "Rocoso",
                                "Boscoso",
                                "Sendero",
                                "Mixto");

                dificultadTecnicaCombo.getItems().addAll(
                                "Baja",
                                "Media",
                                "Alta");

                dificultadFisicaCombo.getItems().addAll(
                                "Baja",
                                "Media",
                                "Alta");

                rutaEnEdicion = App.consumirRutaEnEdicion();
                if (rutaEnEdicion != null) {
                        tituloForm.setText("Editar ruta");
                        cargarRuta(rutaEnEdicion);
                }
        }

        private boolean validarCampos() {
                if (nombreField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error", "El nombre es obligatorio.");
                        return false;
                }

                if (latitudInicialField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error", "La latitud es obligatoria.");
                        return false;
                }

                if (longitudInicialField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error", "La longitud es obligatoria.");
                        return false;
                }

                if (altitudMaximaField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error", "La altitud máxima es obligatoria.");
                        return false;
                }

                if (tipoTerrenoCombo.getValue() == null) {
                        mostrarAlerta("Error", "Debes seleccionar un tipo de terreno.");
                        return false;
                }

                if (dificultadTecnicaCombo.getValue() == null) {
                        mostrarAlerta("Error", "Debes seleccionar una dificultad técnica.");
                        return false;
                }

                if (dificultadFisicaCombo.getValue() == null) {
                        mostrarAlerta("Error", "Debes seleccionar una dificultad física.");
                        return false;
                }

                String nombre = Ruta.normalizarNombre(nombreField.getText());
                if (!Ruta.nombreValido(nombre)) {
                        mostrarAlerta(
                                        "Error",
                                        "El nombre solo puede contener letras, números y espacios.");
                        return false;
                }

                int idExcluido = rutaEnEdicion == null ? -1 : rutaEnEdicion.getId();
                if (ConexionDB.existeNombre(nombre, idExcluido)) {
                        mostrarAlerta("Error", "Ya existe una ruta con ese nombre.");
                        return false;
                }

                return true;
        }

        private boolean coordenadasValidas(double latitud, double longitud) {

        return Double.isFinite(latitud)
                && Double.isFinite(longitud)
                && latitud >= -90
                && latitud <= 90
                && longitud >= -180
                && longitud <= 180;
        }

        @FXML
        private void guardarRuta() {
                if (!validarCampos()) {
                        return;
                }
                try {

                        String nombre = Ruta.normalizarNombre(nombreField.getText());

                        double latitudInicial = Double.parseDouble(latitudInicialField.getText().trim());

                        double longitudInicial = Double.parseDouble(longitudInicialField.getText().trim());

                        double altitudMaxima = Double.parseDouble(altitudMaximaField.getText().trim());

                        String tipoTerreno = tipoTerrenoCombo.getValue();

                        String dificultadTecnica = dificultadTecnicaCombo.getValue();

                        String dificultadFisica = dificultadFisicaCombo.getValue();

                        if (!coordenadasValidas(latitudInicial, longitudInicial)) {
                                mostrarAlerta(
                                                "Error",
                                                "La latitud debe estar entre -90 y 90 y la longitud entre -180 y 180.");
                                return;
                        }

                        Ruta ruta = rutaEnEdicion == null
                                        ? new Ruta(nombre, latitudInicial, longitudInicial, altitudMaxima,
                                                        tipoTerreno, dificultadTecnica, dificultadFisica)
                                        : new Ruta(rutaEnEdicion.getId(), nombre, latitudInicial, longitudInicial,
                                                        altitudMaxima, tipoTerreno, dificultadTecnica, dificultadFisica);

                        boolean guardada = rutaEnEdicion == null
                                        ? ConexionDB.insertarRuta(ruta)
                                        : ConexionDB.actualizarRuta(ruta);

                        if (!guardada) {
                                mostrarAlerta("Error", "No se pudo guardar la ruta. Comprueba que el nombre no esté repetido.");
                                return;
                        }

                        limpiarCampos();

                        mostrarAlerta(
                                        "Éxito",
                                        rutaEnEdicion == null
                                                        ? "Ruta guardada correctamente."
                                                        : "Ruta actualizada correctamente.");

                } catch (NumberFormatException e) {

                        mostrarAlerta(
                                        "Error",
                                        "Latitud, longitud y altitud deben ser valores numéricos.");
                }
        }

        private void limpiarCampos() {

                nombreField.clear();
                latitudInicialField.clear();
                longitudInicialField.clear();
                altitudMaximaField.clear();
                tipoTerrenoCombo.setValue(null);
                dificultadTecnicaCombo.setValue(null);
                dificultadFisicaCombo.setValue(null);
        }

        private void cargarRuta(Ruta ruta) {
                nombreField.setText(ruta.getNombre());
                latitudInicialField.setText(String.valueOf(ruta.getLatitudInicial()));
                longitudInicialField.setText(String.valueOf(ruta.getLongitudInicial()));
                altitudMaximaField.setText(String.valueOf(ruta.getAltitudMaxima()));
                tipoTerrenoCombo.setValue(ruta.getTipoTerreno());
                dificultadTecnicaCombo.setValue(ruta.getDificultadTecnica());
                dificultadFisicaCombo.setValue(ruta.getDificultadFisica());
        }

        private void mostrarAlerta(String titulo, String mensaje) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);

                alerta.setTitle(titulo);
                alerta.setHeaderText(null);
                alerta.setContentText(mensaje);

                alerta.showAndWait();
        }
}