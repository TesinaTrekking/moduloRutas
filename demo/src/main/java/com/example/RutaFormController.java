package com.example;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import javafx.css.PseudoClass;

public class RutaFormController {

        private static final int DECIMALES_COORDENADAS = 4;
        private final RutaDAO rutaDAO = new RutaDAO();

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

                configurarCampos();

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
                String nombre = Ruta.normalizarNombre(nombreField.getText());
                if (nombre.isEmpty()) {
                        mostrarAlerta("Error", "El nombre es obligatorio.");
                        return false;
                }

                if (!Ruta.nombreValido(nombre)) {
                        mostrarAlerta("Error", "El nombre solo puede contener letras, números y espacios.");
                        return false;
                }

                if (rutaDAO.existeNombre(nombre, rutaEnEdicion == null ? -1 : rutaEnEdicion.getId())) {
                        mostrarAlerta("Error", "Ya existe una ruta con ese nombre.");
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

                return true;
        }

        @FXML
        private void guardarRuta() throws IOException {
                if (!validarCampos()) {
                        return;
                }
                try {

                        String nombre = Ruta.formatearNombre(nombreField.getText());

                        double latitudInicial = parsearLatitud();
                        double longitudInicial = parsearLongitud();
                        double altitudMaxima = parsearAltitud();

                        String tipoTerreno = tipoTerrenoCombo.getValue();

                        String dificultadTecnica = dificultadTecnicaCombo.getValue();

                        String dificultadFisica = dificultadFisicaCombo.getValue();

                        Ruta ruta = rutaEnEdicion == null
                                        ? new Ruta(nombre, latitudInicial, longitudInicial, altitudMaxima,
                                                        tipoTerreno, dificultadTecnica, dificultadFisica)
                                        : new Ruta(rutaEnEdicion.getId(), nombre, latitudInicial, longitudInicial,
                                                        altitudMaxima, tipoTerreno, dificultadTecnica,
                                                        dificultadFisica);

                        boolean guardada = rutaEnEdicion == null
                                        ? rutaDAO.insertar(ruta)
                                        : rutaDAO.actualizar(ruta);

                        if (!guardada) {
                                mostrarAlerta("Error",
                                                "No se pudo guardar la ruta. Comprueba que el nombre no esté repetido.");
                                return;
                        }

                        volverListado();

                        mostrarAlerta(
                                        "Éxito",
                                        rutaEnEdicion == null
                                                        ? "Ruta guardada correctamente."
                                                        : "Ruta actualizada correctamente.");

                } catch (NumberFormatException e) {
                        return;
                }
        }

        private double parsearLatitud() {
                if (latitudInicialField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error de latitud", "La latitud es obligatoria.");
                        throw new NumberFormatException("latitud vacía");
                }

                double latitud;
                try {
                        latitud = Double.parseDouble(latitudInicialField.getText().trim());
                } catch (NumberFormatException e) {
                        mostrarAlerta("Error de latitud", "La latitud debe ser un número válido.");
                        throw e;
                }
                if (!Double.isFinite(latitud) || latitud < -90 || latitud > 90) {
                        mostrarAlerta("Error de latitud", "La latitud debe ser un número entre -90 y 90.");
                        throw new NumberFormatException("latitud fuera de rango");
                }
                return latitud;
        }

        private double parsearLongitud() {
                if (longitudInicialField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error de longitud", "La longitud es obligatoria.");
                        throw new NumberFormatException("longitud vacía");
                }

                double longitud;
                try {
                        longitud = Double.parseDouble(longitudInicialField.getText().trim());
                } catch (NumberFormatException e) {
                        mostrarAlerta("Error de longitud", "La longitud debe ser un número válido.");
                        throw e;
                }
                if (!Double.isFinite(longitud) || longitud < -180 || longitud > 180) {
                        mostrarAlerta("Error de longitud", "La longitud debe ser un número entre -180 y 180.");
                        throw new NumberFormatException("longitud fuera de rango");
                }
                return longitud;
        }

        private double parsearAltitud() {
                if (altitudMaximaField.getText().trim().isEmpty()) {
                        mostrarAlerta("Error de altitud", "La altitud máxima es obligatoria.");
                        throw new NumberFormatException("altitud vacía");
                }

                double altitud;
                try {
                        altitud = Double.parseDouble(altitudMaximaField.getText().trim());
                } catch (NumberFormatException e) {
                        mostrarAlerta("Error de altitud", "La altitud máxima debe ser un número válido.");
                        throw e;
                }
                if (!Double.isFinite(altitud) || altitud < 0) {
                        mostrarAlerta("Error de altitud", "La altitud máxima debe ser un número mayor o igual que 0.");
                        throw new NumberFormatException("altitud no válida");
                }
                return altitud;
        }

        private void configurarCampos() {
                nombreField.setTextFormatter(crearFormatter(
                                nombreField,
                                "[\\p{L}\\p{N} ]*",
                                "El nombre solo admite letras, números y espacios."));

                latitudInicialField.setTextFormatter(crearFormatterNumerico(
                                latitudInicialField, true, DECIMALES_COORDENADAS,
                                "La latitud solo admite números, signo negativo y punto decimal."));
                longitudInicialField.setTextFormatter(crearFormatterNumerico(
                                longitudInicialField, true, DECIMALES_COORDENADAS,
                                "La longitud solo admite números, signo negativo y punto decimal."));
                altitudMaximaField.setTextFormatter(crearFormatterNumerico(
                                altitudMaximaField, false, -1,
                                "La altitud solo admite números y punto decimal."));
        }

        private TextFormatter<String> crearFormatter(
                        TextField campo,
                        String patron,
                        String mensaje) {
                return new TextFormatter<>(change -> {
                        if (change.getControlNewText().matches(patron)) {
                                limpiarError(campo);
                                return change;
                        }

                        marcarError(campo, mensaje);
                        return null;
                });
        }

        private TextFormatter<String> crearFormatterNumerico(
                        TextField campo,
                        boolean admiteNegativo,
                        int maxDecimales,
                        String mensaje) {

                String patron = admiteNegativo
                                ? "-?[0-9]*\\.?[0-9]*"
                                : "[0-9]*\\.?[0-9]*";

                return new TextFormatter<>(change -> {

                        String texto = change.getControlNewText();

                        if (!texto.matches(patron)) {
                                marcarError(campo, mensaje);
                                return null;
                        }

                        if (maxDecimales >= 0 && texto.contains(".")) {
                                String parteDecimal = texto.substring(texto.indexOf('.') + 1);

                                if (parteDecimal.length() > maxDecimales) {
                                        marcarError(
                                                        campo,
                                                        "La cantidad máxima de decimales permitida es "
                                                                        + maxDecimales
                                                                        + ".");
                                        return null;
                                }
                        }

                        limpiarError(campo);
                        return change;
                });
        }

        private void marcarError(TextField campo, String mensaje) {
                campo.pseudoClassStateChanged(
                                PseudoClass.getPseudoClass("error"),
                                true);

                Tooltip tooltip = new Tooltip(mensaje);
                tooltip.setShowDelay(Duration.millis(200));
                tooltip.setShowDuration(Duration.seconds(5));
                tooltip.setHideDelay(Duration.millis(100));

                campo.setTooltip(tooltip);
        }


        private void limpiarError(TextField campo) {
                campo.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false);
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