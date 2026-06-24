package org.example.registroparticipantes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Connection instancia;

    private static final String URL = "jdbc:postgresql://localhost:5432/Participantes";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123456";

    private Conexion() {
    }

    public static Connection getInstancia() {
        try {
            // Si la conexión no existe o se cerró, la creamos por primera vez
            if (instancia == null || instancia.isClosed()) {
                synchronized (Conexion.class) { // Hilo seguro (opcional pero recomendado)
                    if (instancia == null || instancia.isClosed()) {
                        // Cargar el driver de PostgreSQL (opcional en versiones modernas de JDBC, pero asegura el funcionamiento)
                        Class.forName("org.postgresql.Driver");

                        // Establecer la conexión empleando el DriverManager
                        instancia = DriverManager.getConnection(URL, USER, PASSWORD);
                        System.out.println("Conexión exitosa a PostgreSQL."); // Mensaje requerido por la guía
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de PostgreSQL. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error crítico al intentar conectar a la base de datos: " + e.getMessage());
        }
        return instancia;
    }
}