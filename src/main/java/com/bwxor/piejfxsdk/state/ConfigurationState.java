package com.bwxor.piejfxsdk.state;

import com.bwxor.piejfxsdk.type.Model;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationState {
    private Map<Model, String> keys = new HashMap<>();
    private Path configurationDirectory;
    public static final ConfigurationState instance = new ConfigurationState();

    private ConfigurationState() {
    }

    public Map<Model, String> getKeys() {
        return keys;
    }

    public void setKeys(Map<Model, String> keys) {
        this.keys = keys;
    }

    public Path getConfigurationDirectory() {
        return configurationDirectory;
    }

    public void setConfigurationDirectory(Path configurationDirectory) {
        this.configurationDirectory = configurationDirectory;
    }
}
