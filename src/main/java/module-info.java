module org.medicamentos.medicamento {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.medicamentos.medicamento to javafx.fxml;
    exports org.medicamentos.medicamento;
}