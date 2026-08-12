package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:crud.db";

    public static Connection conectar() {
        try {
            Connection conexion = DriverManager.getConnection(URL);
            return conexion;
        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            return null;
        }
    }

    public static void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS personas (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nombre TEXT NOT NULL," +
                     "apellido TEXT NOT NULL," +
                     "email TEXT)";

        try (Connection conexion = conectar();
             Statement statement = conexion.createStatement()) {

            statement.execute(sql);
            System.out.println("Tabla 'personas' lista.");

        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    public static void insertarPersona(String nombre, String apellido, String email) {
        String sql = "INSERT INTO personas (nombre, apellido, email) VALUES (?, ?, ?)";

        try (Connection conexion = conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setString(3, email);

            statement.executeUpdate();
            System.out.println("Persona guardada con éxito.");

        } catch (SQLException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    public static void actualizarPersona(int id, String nombre, String apellido, String email) {
        String sql = "UPDATE personas SET nombre = ?, apellido = ?, email = ? WHERE id = ?";

        try (Connection conexion = conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setString(3, email);
            statement.setInt(4, id);

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Persona actualizada con éxito.");
            } else {
                System.out.println("No se encontró ninguna persona con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public static void eliminarPersona(int id) {
        String sql = "DELETE FROM personas WHERE id = ?";

        try (Connection conexion = conectar();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setInt(1, id);

            int filasAfectadas = statement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Persona eliminada con éxito.");
            } else {
                System.out.println("No se encontró ninguna persona con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public static ObservableList<Persona> obtenerTodasLasPersonas() {
        ObservableList<Persona> personas = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre, apellido, email FROM personas";

        try (Connection conexion = conectar();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String email = resultSet.getString("email");
                personas.add(new Persona(id, nombre, apellido, email));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener personas: " + e.getMessage());
        }

        return personas;
    }
    }
