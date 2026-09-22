package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class RutaController implements Initializable {

        private final RutaDAO rutaDAO = new RutaDAO();
        private ObservableList<Ruta> rutas;
        private FilteredList<Ruta> rutasFiltradas;

        @FXML
        private TableView<Ruta> tablaRutas;

        @FXML
        private TableColumn<Ruta, Integer> colId;

        @FXML
        private TableColumn<Ruta, String> colNombre;

        @FXML
        private TableColumn<Ruta, Double> colLatitudInicial;

        @FXML
        private TableColumn<Ruta, Double> colLongitudInicial;

        @FXML
        private TableColumn<Ruta, Double> colAltitudMaxima;

        @FXML
        private TableColumn<Ruta, String> colTipoTerreno;

        @FXML
        private TableColumn<Ruta, String> colDificultadTecnica;

        @FXML
        private TableColumn<Ruta, String> colDificultadFisica;

        @FXML
        private TextField buscarField;

        @FXML
        private ComboBox<String> filtroTerrenoCombo;

        @FXML
        private ComboBox<String> filtroDificultadTecnicaCombo;

        @FXML
        private ComboBox<String> filtroDificultadFisicaCombo;

        @FXML
        private CheckBox mostrarInactivasCheckBox;

        @Override
        public void initialize(URL url, ResourceBundle rb) {

                colId.setCellValueFactory(
                                new PropertyValueFactory<>("id"));

                colNombre.setCellValueFactory(
                                new PropertyValueFactory<>("nombre"));
                colNombre.setComparator(String.CASE_INSENSITIVE_ORDER);
                colNombre.setSortType(TableColumn.SortType.ASCENDING);

                colLatitudInicial.setCellValueFactory(
                                new PropertyValueFactory<>("latitudInicial"));

                colLongitudInicial.setCellValueFactory(
                                new PropertyValueFactory<>("longitudInicial"));

                colAltitudMaxima.setCellValueFactory(
                                new PropertyValueFactory<>("altitudMaxima"));

                colTipoTerreno.setCellValueFactory(
                                new PropertyValueFactory<>("tipoTerreno"));

                colDificultadTecnica.setCellValueFactory(
                                new PropertyValueFactory<>("dificultadTecnica"));

                colDificultadFisica.setCellValueFactory(
                                new PropertyValueFactory<>("dificultadFisica"));

                configurarFiltros();
                cargarRutas();

                tablaRutas.getSortOrder().add(colNombre);
                tablaRutas.sort();

        }

        private void cargarRutas() {

                if (mostrarInactivasCheckBox.isSelected()) {
                        rutas = FXCollections.observableArrayList(
                                        rutaDAO.obtenerTodasIncluyendoInactivas());
                } else {
                        rutas = FXCollections.observableArrayList(
                                        rutaDAO.obtenerTodas());
                }

                rutasFiltradas = new FilteredList<>(rutas);

                tablaRutas.setItems(rutasFiltradas);

                aplicarFiltros();
        }

        @FXML
        private void switchToForm() throws IOException {

                App.consumirRutaEnEdicion();
                App.setRoot("ruta-form");
        }

        @FXML
        private void editarRuta() throws IOException {

                Ruta rutaSeleccionada = tablaRutas.getSelectionModel().getSelectedItem();

                if (rutaSeleccionada == null) {

                        mostrarAlerta(
                                        "Selecciona una ruta",
                                        "Selecciona una ruta para editar.");

                        return;
                }

                App.prepararEdicion(rutaSeleccionada);
        }

        @FXML
        private void eliminarRuta() {

                Ruta rutaSeleccionada = tablaRutas.getSelectionModel().getSelectedItem();

                if (rutaSeleccionada == null) {

                        mostrarAlerta(
                                        "Selecciona una ruta",
                                        "Selecciona una ruta para eliminar.");

                        return;
                }

                Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);

                confirmacion.setTitle("Confirmar eliminación");
                confirmacion.setHeaderText("¿Estás seguro?");
                confirmacion.setContentText(
                                "¿Eliminar la ruta \""
                                                + rutaSeleccionada.getNombre()
                                                + "\"?");

                if (confirmacion.showAndWait().orElse(null) == ButtonType.OK) {

                        if (rutaDAO.eliminar(rutaSeleccionada.getId())) {
                                cargarRutas();

                                mostrarAlerta(
                                                "Éxito",
                                                "Ruta eliminada correctamente.");
                        } else {
                                mostrarAlerta(
                                                "Error",
                                                "No se pudo eliminar la ruta.");
                        }
                }
        }

        private void configurarFiltros() {

                filtroTerrenoCombo.getItems().addAll(
                                "Todos",
                                "Rocoso",
                                "Boscoso",
                                "Sendero",
                                "Mixto");

                filtroDificultadTecnicaCombo.getItems().addAll(
                                "Todas",
                                "Baja",
                                "Media",
                                "Alta");

                filtroDificultadFisicaCombo.getItems().addAll(
                                "Todas",
                                "Baja",
                                "Media",
                                "Alta");

                filtroTerrenoCombo.setValue("Todos");
                filtroDificultadTecnicaCombo.setValue("Todas");
                filtroDificultadFisicaCombo.setValue("Todas");

                buscarField.textProperty().addListener(
                                (observable, anterior, actual) -> aplicarFiltros());

                filtroTerrenoCombo.valueProperty().addListener(
                                (observable, anterior, actual) -> aplicarFiltros());

                filtroDificultadTecnicaCombo.valueProperty().addListener(
                                (observable, anterior, actual) -> aplicarFiltros());

                filtroDificultadFisicaCombo.valueProperty().addListener(
                                (observable, anterior, actual) -> aplicarFiltros());

                mostrarInactivasCheckBox.selectedProperty().addListener(
                                (observable, anterior, actual) -> {
                                        System.out.println("CHECKBOX CAMBIÓ: " + actual);
                                        cargarRutas();
                                });
        }

        private void aplicarFiltros() {

                if (rutasFiltradas == null) {
                        return;
                }

                String texto = buscarField.getText().trim().toLowerCase();

                String terreno = filtroTerrenoCombo.getValue();

                String dificultadTecnica = filtroDificultadTecnicaCombo.getValue();

                String dificultadFisica = filtroDificultadFisicaCombo.getValue();

                rutasFiltradas.setPredicate(ruta -> {

                        boolean coincideNombre = texto.isEmpty()
                                        || ruta.getNombre()
                                                        .toLowerCase()
                                                        .contains(texto);

                        boolean coincideTerreno = terreno.equals("Todos")
                                        || ruta.getTipoTerreno()
                                                        .equals(terreno);

                        boolean coincideDificultadTecnica = dificultadTecnica.equals("Todas")
                                        || ruta.getDificultadTecnica()
                                                        .equals(dificultadTecnica);

                        boolean coincideDificultadFisica = dificultadFisica.equals("Todas")
                                        || ruta.getDificultadFisica()
                                                        .equals(dificultadFisica);

                        return coincideNombre
                                        && coincideTerreno
                                        && coincideDificultadTecnica
                                        && coincideDificultadFisica;
                });
        }

        private void mostrarAlerta(
                        String titulo,
                        String mensaje) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);

                alerta.setTitle(titulo);
                alerta.setHeaderText(null);
                alerta.setContentText(mensaje);

                alerta.showAndWait();
        }
}