package com.bwxor.piejfxsdk.factory;

import com.bwxor.piejfxsdk.service.ai.AIService;
import com.bwxor.piejfxsdk.service.ai.ChatGPTService;
import com.bwxor.piejfxsdk.service.ai.ClaudeService;
import com.bwxor.piejfxsdk.service.ai.GeminiService;
import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.piejfxsdk.type.Model;
import com.google.genai.types.CreateFileResponse;
import io.github.raghultech.markdown.javafx.preview.MarkdownWebView;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TabFactory {
    private static final String EMPTY_STRING = "";

    public static Tab createPromptTab() {
        ConfigurationState configurationState = ConfigurationState.instance;
        ServiceState serviceState = ServiceState.instance;

        Tab tab = new Tab("Prompt");

        VBox vbox = new VBox();
        tab.setContent(vbox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(5, 5, 5, 5));


        MarkdownWebView responseWebView = new MarkdownWebView("", serviceState.getHostServices());
        responseWebView.setDarkMode(true);
        responseWebView.setContent("The assistant's response will be displayed here.");
        vbox.getChildren().add(responseWebView.getWebView());

        VBox.setVgrow(responseWebView.getWebView(), Priority.ALWAYS);

        TextArea promptTextArea = new TextArea();
        promptTextArea.setWrapText(true);
        promptTextArea.setMinHeight(100);
        promptTextArea.setPrefRowCount(5);
        promptTextArea.setPromptText("Press ENTER to send the prompt, SHIFT + ENTER for new line.");
        promptTextArea.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (e.isShiftDown()) {
                    int caretPosition = promptTextArea.getCaretPosition();

                    promptTextArea.setText(promptTextArea.getText().substring(0, caretPosition) + System.lineSeparator() + promptTextArea.getText().substring(caretPosition));
                    promptTextArea.positionCaret(caretPosition + 1);
                } else {
                    AIService aiService = serviceState.getAiService();

                    if (aiService == null || aiService.getModel() == null) {
                        serviceState.getPluginNotificationService().showNotificationOk("Choose an AI model before sending any prompts.");
                    } else {
                        String prompt = promptTextArea.getText();

                        try {
                            responseWebView.setContent("Thinking...");
                            promptTextArea.clear();
                            promptTextArea.setEditable(false);

                            CompletableFuture.supplyAsync(() -> aiService.generateContent(configurationState.getKeys().getOrDefault(
                                                    aiService.getModel(), EMPTY_STRING
                                            ), prompt)
                                    ).orTimeout(15, TimeUnit.SECONDS)
                                    .thenAccept(
                                            responseWebView::setContent
                                    ).thenRun(() ->
                                            promptTextArea.setEditable(true)
                                    ).exceptionally(ex -> {
                                        responseWebView.setContent(ex.getMessage());
                                        promptTextArea.setEditable(true);
                                        return null;
                                    });

                        } catch (Exception ex) {
                            responseWebView.setContent(ex.getMessage());
                            promptTextArea.setEditable(true);
                        }
                    }

                    promptTextArea.clear();
                    e.consume();
                }
            }
        });
        vbox.getChildren().add(promptTextArea);

        return tab;
    }

    public static Tab createConfigurationTab() {
        ConfigurationState configurationState = ConfigurationState.instance;
        ServiceState serviceState = ServiceState.instance;

        Tab tab = new Tab("Configuration");

        VBox vbox = new VBox();
        tab.setContent(vbox);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(5, 5, 5, 5));

        VBox modelVBox = new VBox();
        modelVBox.setSpacing(5);
        vbox.getChildren().add(modelVBox);

        Label modelLabel = new Label("Model");
        modelVBox.getChildren().add(modelLabel);

        HBox modelHBox = new HBox();
        modelVBox.getChildren().add(modelHBox);
        modelHBox.setMaxWidth(Double.MAX_VALUE);

        TextArea keyTextArea = new TextArea(); // will be used in the listener - this is why I defined it early

        ComboBox<String> modelComboBox = new ComboBox<>();
        modelHBox.getChildren().add(modelComboBox);
        modelComboBox.setItems(FXCollections.observableArrayList(Model.GEMINI_2_5_FLASH_LITE.getValue(), Model.CHATGPT_4O.getValue(), Model.CLAUDE_3_5_SONNET.getValue()));
        modelComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            serviceState.setAiService(
                    switch (Model.parse(newValue.toLowerCase())) {
                        case GEMINI_2_5_FLASH_LITE -> new GeminiService();
                        case CHATGPT_4O -> new ChatGPTService();
                        case CLAUDE_3_5_SONNET -> new ClaudeService();
                    }
            );

            keyTextArea.setText(
                    configurationState.getKeys().getOrDefault(serviceState.getAiService().getModel(), EMPTY_STRING)
            );
        });

        HBox.setHgrow(modelComboBox, Priority.ALWAYS);

        VBox authenticationVBox = new VBox();
        authenticationVBox.setSpacing(5);
        vbox.getChildren().add(authenticationVBox);

        Label authenticationLabel = new Label("Authentication Key");
        authenticationVBox.getChildren().add(authenticationLabel);

        keyTextArea.setPromptText("Authentication key");
        keyTextArea.setWrapText(true);
        authenticationVBox.getChildren().add(keyTextArea);

        Button saveButton = new Button("Save");
        authenticationVBox.getChildren().add(saveButton);
        saveButton.setOnMouseClicked(_ -> {
                    configurationState.getKeys().put(serviceState.getAiService().getModel(), keyTextArea.getText());
                    serviceState.getConfigurationService().writeConfigToFile();
                    serviceState.getAiService().reset();
                }
        );

        return tab;
    }
}
