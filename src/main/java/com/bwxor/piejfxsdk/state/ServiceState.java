package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.service.ConfigurationService;
import com.bwxor.piejfxsdk.service.ai.AIService;
import com.bwxor.plugin.service.PluginNotificationService;

public class ServiceState {
    private AIService aiService;
    private PluginNotificationService pluginNotificationService;
    private ConfigurationService configurationService;
    public static final ServiceState instance = new ServiceState();

    private ServiceState() {}

    public AIService getAiService() {
        return aiService;
    }

    public void setAiService(AIService aiService) {
        this.aiService = aiService;
    }

    public PluginNotificationService getPluginNotificationService() {
        return pluginNotificationService;
    }

    public void setPluginNotificationService(PluginNotificationService pluginNotificationService) {
        this.pluginNotificationService = pluginNotificationService;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
