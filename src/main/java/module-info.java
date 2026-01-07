module com.mycompany.carrentalfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires atlantafx.base;
    requires java.desktop;

    opens com.mycompany.carrentalfx to javafx.fxml;
    exports com.mycompany.carrentalfx;
}
