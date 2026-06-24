package org.example.registroparticipantes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnSalir;

    @FXML
    private void Ingresar(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios. Por favor, rellene el formulario.");
            }

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Inicio de Sesión Correcto", "¡Bienvenido al Sistema de Natación!");

                System.out.println("Cargando Ventana CRUD...");

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Autenticación", "Credenciales Incorrectas", "El usuario o la contraseña no son válidos.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Advertencia de Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Inesperado", "Ocurrió un error en el sistema", e.getMessage());
        }
    }

    @FXML
    private void Salir(ActionEvent event) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}
