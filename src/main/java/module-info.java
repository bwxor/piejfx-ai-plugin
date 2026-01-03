module com.bwxor.piejfxsdk {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.bwxor.plugin;
    requires javafx.graphics;
    requires google.genai;
    requires org.json;
    requires api;
    requires service;
    requires java.net.http;

    opens com.bwxor.piejfxsdk to javafx.fxml;
    exports com.bwxor.piejfxsdk;
}