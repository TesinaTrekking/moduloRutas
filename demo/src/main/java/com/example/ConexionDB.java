package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:trekking.db";


    // =========================================================
    // CONEXIÓN
    // =========================================================

    public static Connection conectar() {

        try {

            return DriverManager.getConnection(URL);

        } catch (SQLException e) {

            System.out.println(
                    "Error al conectar con la base de datos: "
                            + e.getMessage()
            );

            return null;
        }
    }


    // =========================================================
    // CREACIÓN DE TABLA
    // =========================================================

    public static void crearTabla() {

        String sql = """
            CREATE TABLE IF NOT EXISTS rutas (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL COLLATE NOCASE UNIQUE,
                latitud_inicial REAL NOT NULL,
                longitud_inicial REAL NOT NULL,
                altitud_maxima REAL NOT NULL,
                tipo_terreno TEXT NOT NULL,
                dificultad_tecnica TEXT NOT NULL,
                dificultad_fisica TEXT NOT NULL
            )
            """;

        try (
            Connection conexion = conectar();
            Statement statement = conexion.createStatement()
        ) {

            statement.execute(sql);
            normalizarDatosExistentes(conexion);
            statement.executeUpdate(
                    "DROP INDEX IF EXISTS idx_rutas_nombre_unique");
            statement.executeUpdate(
                    "CREATE UNIQUE INDEX idx_rutas_nombre_unique "
                            + "ON rutas(nombre COLLATE NOCASE)");

            System.out.println("Tabla 'rutas' lista.");

        } catch (SQLException e) {

            System.out.println(
                    "Error al crear la tabla: "
                            + e.getMessage()
            );
        }
    }

        private static void normalizarDatosExistentes(Connection conexion)
                        throws SQLException {

                Set<String> nombresUsados = new HashSet<>();
                String consulta = "SELECT id, nombre FROM rutas ORDER BY id";

                try (
                        Statement statement = conexion.createStatement();
                        ResultSet resultSet = statement.executeQuery(consulta);
                        PreparedStatement actualizar = conexion.prepareStatement(
                                        "UPDATE rutas SET nombre = ? WHERE id = ?")
                ) {
                        while (resultSet.next()) {
                                int id = resultSet.getInt("id");
                                String nombre = Ruta.normalizarNombre(resultSet.getString("nombre"));

                                if (!Ruta.nombreValido(nombre)) {
                                        nombre = "Ruta " + id;
                                }

                                String nombreBase = nombre;
                                int sufijo = 2;
                                while (!nombresUsados.add(Ruta.claveNombre(nombre))) {
                                        nombre = nombreBase + " " + sufijo++;
                                }

                                actualizar.setString(1, nombre);
                                actualizar.setInt(2, id);
                                actualizar.executeUpdate();
                        }
                }
        }


    // =========================================================
    // INSERTAR
    // =========================================================

        public static boolean existeNombre(String nombre, int idExcluido) {

                String sql = "SELECT 1 FROM rutas "
                        + "WHERE nombre = ? COLLATE NOCASE AND id <> ? LIMIT 1";

                try (
                        Connection conexion = conectar();
                        PreparedStatement statement = conexion.prepareStatement(sql)
                ) {
                        statement.setString(1, nombre);
                        statement.setInt(2, idExcluido);
                        return statement.executeQuery().next();
                } catch (SQLException e) {
                        System.out.println("Error al comprobar el nombre: " + e.getMessage());
                        return true;
                }
        }

        public static boolean insertarRuta(Ruta ruta) {

        String sql = """
            INSERT INTO rutas
            (
                nombre,
                latitud_inicial,
                longitud_inicial,
                altitud_maxima,
                tipo_terreno,
                dificultad_tecnica,
                dificultad_fisica
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conexion = conectar();
            PreparedStatement statement =
                    conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    ruta.getNombre()
            );

            statement.setDouble(
                    2,
                    ruta.getLatitudInicial()
            );

            statement.setDouble(
                    3,
                    ruta.getLongitudInicial()
            );

            statement.setDouble(
                    4,
                    ruta.getAltitudMaxima()
            );

            statement.setString(
                    5,
                    ruta.getTipoTerreno()
            );

            statement.setString(
                    6,
                    ruta.getDificultadTecnica()
            );

            statement.setString(
                    7,
                    ruta.getDificultadFisica()
            );

            statement.executeUpdate();

            System.out.println(
                    "Ruta guardada correctamente."
            );
            return true;

        } catch (SQLException e) {

            System.out.println(
                    "Error al guardar la ruta: "
                            + e.getMessage()
            );
            return false;
        }
    }


    // =========================================================
    // ACTUALIZAR
    // =========================================================

        public static boolean actualizarRuta(Ruta ruta) {

        String sql = """
            UPDATE rutas
            SET
                nombre = ?,
                latitud_inicial = ?,
                longitud_inicial = ?,
                altitud_maxima = ?,
                tipo_terreno = ?,
                dificultad_tecnica = ?,
                dificultad_fisica = ?
            WHERE id = ?
            """;

        try (
            Connection conexion = conectar();
            PreparedStatement statement =
                    conexion.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    ruta.getNombre()
            );

            statement.setDouble(
                    2,
                    ruta.getLatitudInicial()
            );

            statement.setDouble(
                    3,
                    ruta.getLongitudInicial()
            );

            statement.setDouble(
                    4,
                    ruta.getAltitudMaxima()
            );

            statement.setString(
                    5,
                    ruta.getTipoTerreno()
            );

            statement.setString(
                    6,
                    ruta.getDificultadTecnica()
            );

            statement.setString(
                    7,
                    ruta.getDificultadFisica()
            );

            statement.setInt(
                    8,
                    ruta.getId()
            );

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println(
                        "Ruta actualizada correctamente."
                );
                return true;

            } else {

                System.out.println(
                        "No se encontró la ruta."
                );
                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar la ruta: "
                            + e.getMessage()
            );
            return false;
        }
    }


    // =========================================================
    // ELIMINAR
    // =========================================================

    public static void eliminarRuta(int id) {

        String sql =
                "DELETE FROM rutas WHERE id = ?";

        try (
            Connection conexion = conectar();
            PreparedStatement statement =
                    conexion.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            int filasAfectadas =
                    statement.executeUpdate();

            if (filasAfectadas > 0) {

                System.out.println(
                        "Ruta eliminada correctamente."
                );

            } else {

                System.out.println(
                        "No se encontró la ruta."
                );
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar la ruta: "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // CONSULTAR TODAS
    // =========================================================

    public static ObservableList<Ruta> obtenerTodasLasRutas() {

        ObservableList<Ruta> rutas =
                FXCollections.observableArrayList();

        String sql = """
            SELECT
                id,
                nombre,
                latitud_inicial,
                longitud_inicial,
                altitud_maxima,
                tipo_terreno,
                dificultad_tecnica,
                dificultad_fisica
            FROM rutas
            """;

        try (
            Connection conexion = conectar();
            Statement statement =
                    conexion.createStatement();
            ResultSet resultSet =
                    statement.executeQuery(sql)
        ) {

            while (resultSet.next()) {

                int id =
                        resultSet.getInt("id");

                String nombre =
                        resultSet.getString("nombre");

                double latitudInicial =
                        resultSet.getDouble("latitud_inicial");

                double longitudInicial =
                        resultSet.getDouble("longitud_inicial");

                double altitudMaxima =
                        resultSet.getDouble("altitud_maxima");

                String tipoTerreno =
                        resultSet.getString("tipo_terreno");

                String dificultadTecnica =
                        resultSet.getString(
                                "dificultad_tecnica"
                        );

                String dificultadFisica =
                        resultSet.getString(
                                "dificultad_fisica"
                        );


                Ruta ruta = new Ruta(
                        id,
                        nombre,
                        latitudInicial,
                        longitudInicial,
                        altitudMaxima,
                        tipoTerreno,
                        dificultadTecnica,
                        dificultadFisica
                );


                rutas.add(ruta);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al obtener las rutas: "
                            + e.getMessage()
            );
        }

        return rutas;
    }
}