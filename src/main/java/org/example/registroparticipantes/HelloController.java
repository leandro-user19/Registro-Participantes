package org.example.registroparticipantes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class HelloController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnSalir;


    // Controles del Formulario
    @FXML private TextField txtId;
    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEdad;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<String> cmbEstadoCivil;
    @FXML private ToggleGroup tgJornada;
    @FXML private RadioButton rbMatutina, rbVespertina, rbNocturna;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextArea txtObservaciones;

    // Tabla e Historial
    @FXML private TableView<Object> tblParticipantes; // Cambiaremos Object por el Modelo más adelante
    @FXML private TableColumn<Object, Integer> colId;
    @FXML private TableColumn<Object, String> colCedula, colNombre, colApellido, colCorreo, colEstadoCivil, colJornada, colCategoria;
    @FXML private TableColumn<Object, Integer> colEdad;

    @FXML
    public void initialize() {
        // Cargar las opciones obligatorias del Estado Civil descritas en la rúbrica
        cmbEstadoCivil.setItems(FXCollections.observableArrayList("Soltero", "Casado", "Divorciado", "Viudo"));

        // Cargar opciones lógicas para las Categorías de Natación
        cmbCategoria.setItems(FXCollections.observableArrayList("Infantil (5-12 años)", "Juvenil (13-18 años)", "Adultos (19-49 años)", "Master (50+ años)"));
    }

    @FXML
    private void Ingresar(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                throw new IllegalArgumentException("Todos los campos son obligatorios. Por favor, rellene el formulario.");
            }

            if (usuario.equals("admin") && contrasena.equals("1234")) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Inicio de Sesión Correcto", "Bienvenido");

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

    @FXML
    private void Limpiar() {
        txtId.clear();
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtEdad.clear();
        txtCorreo.clear();
        cmbEstadoCivil.getSelectionModel().clearSelection();
        if (tgJornada.getSelectedToggle() != null) {
            tgJornada.getSelectedToggle().setSelected(false);
        }
        cmbCategoria.getSelectionModel().clearSelection();
        txtObservaciones.clear();
    }

}
