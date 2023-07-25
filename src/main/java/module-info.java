module com.veike.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.veike.crud to javafx.fxml;
    exports com.veike.crud;
}