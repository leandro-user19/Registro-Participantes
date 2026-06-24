package org.example.registroparticipantes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.RadioButton;

import java.util.Optional;
import javafx.scene.control.ButtonType;

public class HelloController {

    // Controles deLogin
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIngresar;
    @FXML private Button btnSalir;

    // Controles del CRUD
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

    // Componentes del TableView
    @FXML private TableView<Participante> tblParticipantes;
    @FXML private TableColumn<Participante, Integer> colId;
    @FXML private TableColumn<Participante, String> colCedula, colNombre, colApellido, colCorreo, colEstadoCivil, colJornada, colCategoria;
    @FXML private TableColumn<Participante, Integer> colEdad;

    private ObservableList<Participante> listaParticipantes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (cmbEstadoCivil != null) {
            cmbEstadoCivil.setItems(FXCollections.observableArrayList("Soltero", "Casado", "Divorciado", "Viudo"));
            cmbCategoria.setItems(FXCollections.observableArrayList("Infantil (5-12 años)", "Juvenil (13-18 años)", "Adultos (19-49 años)", "Master (50+ años)"));

            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
            colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
            colEstadoCivil.setCellValueFactory(new PropertyValueFactory<>("estadoCivil"));
            colJornada.setCellValueFactory(new PropertyValueFactory<>("jornada"));
            colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

            tblParticipantes.getSelectionModel().selectedItemProperty().addListener((obs, anteriorSeleccion, nuevaSeleccion) -> {
                if (nuevaSeleccion != null) {
                    txtId.setText(String.valueOf(nuevaSeleccion.getId()));
                    txtCedula.setText(nuevaSeleccion.getCedula());
                    txtNombre.setText(nuevaSeleccion.getNombre());
                    txtApellido.setText(nuevaSeleccion.getApellido());
                    txtEdad.setText(String.valueOf(nuevaSeleccion.getEdad()));
                    txtCorreo.setText(nuevaSeleccion.getCorreo());
                    cmbEstadoCivil.setValue(nuevaSeleccion.getEstadoCivil());
                    cmbCategoria.setValue(nuevaSeleccion.getCategoria());
                    txtObservaciones.setText(nuevaSeleccion.getObservaciones());

                    String jornada = nuevaSeleccion.getJornada();
                    if ("Matutina".equals(jornada)) rbMatutina.setSelected(true);
                    else if ("Vespertina".equals(jornada)) rbVespertina.setSelected(true);
                    else if ("Nocturna".equals(jornada)) rbNocturna.setSelected(true);
                }
            });

            Leer();
        } else {
            System.out.println("Cargando componentes de la interfaz de Login...");
        }
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
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Inicio de Sesión Correcto", "¡Bienvenido al Sistema de Natación!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("CRUD.fxml"));
                Parent root = loader.load();

                Scene escenaCrud = new Scene(root);
                Stage nuevoStage = new Stage();
                nuevoStage.setTitle("Sistema de Registro de Participantes");
                nuevoStage.setScene(escenaCrud);

                Stage stageLogin = (Stage) btnIngresar.getScene().getWindow();
                stageLogin.close();

                nuevoStage.show();

            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Autenticación", "Credenciales Incorrectas", "El usuario o la contraseña no son válidos.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Vacíos", "Advertencia de Validación", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Carga", "No se pudo abrir la ventana del CRUD", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void Salir(ActionEvent event) {
        Stage stage = (Stage) btnSalir.getScene().getWindow();
        stage.close();
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


    @FXML
    public void Crear(){
        try {
            String cedula = txtCedula.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String edadRaw = txtEdad.getText().trim();
            String correo = txtCorreo.getText().trim();
            String estadoCivil = cmbEstadoCivil.getValue();
            String categoria = cmbCategoria.getValue();
            String observaciones = txtObservaciones.getText().trim();

            String jornada = "";
            if (tgJornada.getSelectedToggle() != null) {
                RadioButton rbSeleccionado = (RadioButton) tgJornada.getSelectedToggle();
                jornada = rbSeleccionado.getText();
            }

            if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || edadRaw.isEmpty() ||
                    correo.isEmpty() || estadoCivil == null || jornada.isEmpty() || categoria == null) {
                throw new IllegalArgumentException("Todos los campos (excepto observaciones) son obligatorios.");
            }

            if (!cedula.matches("\\d+")) {
                throw new IllegalArgumentException("La cédula debe contener únicamente números.");
            }
            if (cedula.length() != 10) {
                throw new IllegalArgumentException("La cédula debe tener exactamente 10 dígitos.");
            }

            int edad;
            try {
                edad = Integer.parseInt(edadRaw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("La edad debe ser un número válido.");
            }
            if (edad <= 5) {
                throw new IllegalArgumentException("El participante debe ser mayor de 5 años.");
            }

            if (!correo.contains("@")) {
                throw new IllegalArgumentException("El correo electrónico no es válido (debe incluir '@').");
            }

            if (existeCorreoEnBD(correo)) {
                throw new IllegalArgumentException("El correo ya se encuentra registrado por otro participante.");
            }

            if (existeCedulaEnBD(cedula)) {
                throw new IllegalArgumentException("La cédula ya se encuentra registrada en el sistema.");
            }

            Connection conn = Conexion.getInstancia();
            String sql = "INSERT INTO participantes (cedula, nombre, apellido, edad, correo, estado_civil, jornada, categoria, observaciones) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, cedula);
                pstmt.setString(2, nombre);
                pstmt.setString(3, apellido);
                pstmt.setInt(4, edad);
                pstmt.setString(5, correo);
                pstmt.setString(6, estadoCivil);
                pstmt.setString(7, jornada);
                pstmt.setString(8, categoria);
                pstmt.setString(9, observaciones.isEmpty() ? null : observaciones);

                pstmt.executeUpdate();

                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro Guardado", "Participante registrado exitosamente.");

                Limpiar();
                Leer();
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia de Validación", "Datos Incorrectos", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo guardar el registro", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico", "Ocurrió un error inesperado", e.getMessage());
        }
    }

    private boolean existeCorreoEnBD(String correo) throws SQLException {
        Connection conn = Conexion.getInstancia();
        String sql = "SELECT COUNT(*) FROM participantes WHERE correo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private boolean existeCedulaEnBD(String cedula) throws SQLException {
        Connection conn = Conexion.getInstancia();
        String sql = "SELECT COUNT(*) FROM participantes WHERE cedula = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    @FXML
    private void Leer() {
        try {
            listaParticipantes.clear();

            Connection conn = Conexion.getInstancia();
            String sql = "SELECT id, cedula, nombre, apellido, edad, correo, estado_civil, jornada, categoria, observaciones "
                    + "FROM participantes ORDER BY id ASC";

            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Participante p = new Participante(
                            rs.getInt("id"),
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getInt("edad"),
                            rs.getString("correo"),
                            rs.getString("estado_civil"),
                            rs.getString("jornada"),
                            rs.getString("categoria"),
                            rs.getString("observaciones")
                    );
                    listaParticipantes.add(p);
                }
            }

            tblParticipantes.setItems(listaParticipantes);

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Lectura",
                    "No se pudieron cargar los datos de los participantes", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico",
                    "Ocurrió un error inesperado al procesar la lista", e.getMessage());
        }
    }


    @FXML
    private void Actualizar() {
        try {
            String idRaw = txtId.getText();
            if (idRaw == null || idRaw.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar primero un participante de la tabla para poder modificarlo.");
            }
            int id = Integer.parseInt(idRaw);

            String cedula = txtCedula.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String edadRaw = txtEdad.getText().trim();
            String correo = txtCorreo.getText().trim();
            String estadoCivil = cmbEstadoCivil.getValue();
            String categoria = cmbCategoria.getValue();
            String observaciones = txtObservaciones.getText().trim();

            String jornada = "";
            if (tgJornada.getSelectedToggle() != null) {
                RadioButton rbSeleccionado = (RadioButton) tgJornada.getSelectedToggle();
                jornada = rbSeleccionado.getText();
            }

            if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || edadRaw.isEmpty() ||
                    correo.isEmpty() || estadoCivil == null || jornada.isEmpty() || categoria == null) {
                throw new IllegalArgumentException("Todos los campos obligatorios deben estar llenos.");
            }

            if (!cedula.matches("\\d+") || cedula.length() != 10) {
                throw new IllegalArgumentException("La cédula debe contener exactamente 10 números.");
            }

            int edad = Integer.parseInt(edadRaw);
            if (edad <= 5) {
                throw new IllegalArgumentException("El participante debe ser mayor de 5 años.");
            }

            if (!correo.contains("@")) {
                throw new IllegalArgumentException("El correo electrónico debe ser válido (incluir '@').");
            }

            if (existeCorreoEnBDOtros(correo, id)) {
                throw new IllegalArgumentException("Este correo ya está registrado por otro participante.");
            }

            Connection conn = Conexion.getInstancia();
            String sql = "UPDATE participantes SET cedula = ?, nombre = ?, apellido = ?, edad = ?, "
                    + "correo = ?, estado_civil = ?, jornada = ?, categoria = ?, observaciones = ? WHERE id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, cedula);
                pstmt.setString(2, nombre);
                pstmt.setString(3, apellido);
                pstmt.setInt(4, edad);
                pstmt.setString(5, correo);
                pstmt.setString(6, estadoCivil);
                pstmt.setString(7, jornada);
                pstmt.setString(8, categoria);
                pstmt.setString(9, observaciones.isEmpty() ? null : observaciones);
                pstmt.setInt(10, id);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro Modificado", "Los datos del participante han sido actualizados de forma exitosa.");
                    Limpiar();
                    Leer();
                }
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia de Validación", "Datos Incorrectos", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo actualizar el registro", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico", "Ocurrió un error inesperado", e.getMessage());
        }
    }

    private boolean existeCorreoEnBDOtros(String correo, int idActual) throws SQLException {
        Connection conn = Conexion.getInstancia();
        String sql = "SELECT COUNT(*) FROM participantes WHERE correo = ? AND id <> ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            pstmt.setInt(2, idActual);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    @FXML
    private void Eliminar() {
        try {
            String idRaw = txtId.getText();
            if (idRaw == null || idRaw.isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar primero un participante de la tabla para poder eliminarlo.");
            }
            int id = Integer.parseInt(idRaw);
            String nombreCompleto = txtNombre.getText() + " " + txtApellido.getText();

            Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertaConfirmacion.setTitle("Confirmar Eliminación");
            alertaConfirmacion.setHeaderText("¿Está seguro de eliminar a este participante?");
            alertaConfirmacion.setContentText("Esta acción borrará permanentemente a: " + nombreCompleto + " (ID: " + id + ") del sistema.");

            Optional<ButtonType> resultado = alertaConfirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                Connection conn = Conexion.getInstancia();
                String sql = "DELETE FROM participantes WHERE id = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, id);
                    int filasAfectadas = pstmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Registro Eliminado", "El participante ha sido removido del sistema de forma correcta.");
                        Limpiar();
                        Leer();
                    }
                }
            } else {
                System.out.println("Eliminación cancelada por el usuario.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Selección Requerida", e.getMessage());
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo eliminar el registro", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error Crítico", "Ocurrió un error inesperado", e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}