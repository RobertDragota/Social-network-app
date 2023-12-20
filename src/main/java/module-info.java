module com.socialnetwork.map_toysocialnetwork {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    opens com.socialnetwork.map_toysocialnetwork.Domain to javafx.base;
    opens com.socialnetwork.map_toysocialnetwork to javafx.fxml;
    exports com.socialnetwork.map_toysocialnetwork;
    exports com.socialnetwork.map_toysocialnetwork.Controllers;
    opens com.socialnetwork.map_toysocialnetwork.Controllers to javafx.fxml;
}