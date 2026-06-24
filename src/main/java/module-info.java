module org.example.registroparticipantes {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.registroparticipantes to javafx.fxml;
    exports org.example.registroparticipantes;
}