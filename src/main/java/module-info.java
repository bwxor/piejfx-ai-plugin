module piejfx.ai.plugin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.bwxor.plugin;
    requires javafx.markdown.preview;
    requires org.json;
    requires api;
    requires service;
    requires google.genai;
    requires java.net.http;

    opens com.bwxor.piejfxsdk to javafx.fxml;
    exports com.bwxor.piejfxsdk;
}