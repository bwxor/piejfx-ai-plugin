package com.bwxor.piejfxsdk;

import com.bwxor.piejfxsdk.factory.TabFactory;
import com.bwxor.piejfxsdk.service.ConfigurationService;
import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.state.ServiceState;
import com.bwxor.plugin.Plugin;
import com.bwxor.plugin.input.PluginContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;

public class AIPlugin implements Plugin {
    private PluginContext pluginContext;

    @Override
    public void onLoad(PluginContext pluginContext) {
        ServiceState serviceState = ServiceState.instance;
        ConfigurationState configurationState = ConfigurationState.instance;
        configurationState.setConfigurationDirectory(pluginContext.getConfigurationDirectoryPath());
        serviceState.setPluginNotificationService(pluginContext.getServiceContainer().getNotificationService());
        serviceState.setConfigurationService(new ConfigurationService());

        try {
            serviceState.getConfigurationService().loadAPIKeys();
        } catch (IOException e) {
            // ToDo: Add an error
            throw new RuntimeException(e);
        }

        this.pluginContext = pluginContext;

        TabPane tabPane = pluginContext.getApplicationWindow().getSidebarTabPane();

        Tab mainTab = new Tab("AI Assistant");

        tabPane.getTabs().add(mainTab);

        TabPane promptOrConfigurationTabPane = new TabPane();
        mainTab.setContent(promptOrConfigurationTabPane);
        promptOrConfigurationTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        promptOrConfigurationTabPane.getTabs().clear();

        promptOrConfigurationTabPane.getTabs().add(TabFactory.createPromptTab());
        promptOrConfigurationTabPane.getTabs().add(TabFactory.createConfigurationTab());
    }

    @Override
    public void onKeyPress(KeyEvent keyEvent) {

    }

    @Override
    public void onSaveFile(File file) {
    }

    @Override
    public void onOpenFile(File file) {
    }

    @Override
    public void onCreateFile(File file) {
    }

    @Override
    public void onCreateFolder(File file) {
    }

    @Override
    public void onDeleteFile(File file) {
    }
}
