module org.example.registroparticipantes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.registroparticipantes to javafx.fxml;
    exports org.example.registroparticipantes;
}