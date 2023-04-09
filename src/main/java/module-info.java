module dfamin {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens dfamin to javafx.fxml;
    opens model to javafx.fxml;
    exports dfamin;
    exports model;
}
