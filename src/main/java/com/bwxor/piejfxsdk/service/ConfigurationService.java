package com.bwxor.piejfxsdk.service;

import com.bwxor.piejfxsdk.builder.JSONAPIKeyBuilder;
import com.bwxor.piejfxsdk.state.ConfigurationState;
import com.bwxor.piejfxsdk.type.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Paths;

public class ConfigurationService {
    public void loadAPIKeys() throws IOException {
        ConfigurationState configurationState = ConfigurationState.instance;
        File confFile = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString(), "config.json").toUri());

        createDirectoryAndFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(confFile))) {
            String confContent = bufferedReader.readAllAsString();

            JSONObject jsonObject = new JSONObject(confContent);
            JSONObject config = jsonObject.getJSONObject("config");
            JSONArray array = config.getJSONArray("keys");

            for (int i = 0; i < array.length(); i++) {
                Model model = Model.parse(array.getJSONObject(i).getString("model"));
                String key = array.getJSONObject(i).getString("key");
                configurationState.getKeys().put(model, key);
            }
        }
    }

    private void createDirectoryAndFile() {
        File confDir = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString()).toUri());
        File confFile = new File(Paths.get(confDir.toString(), "config.json").toUri());

        if (!confDir.exists()) {
            confDir.mkdirs();
            createFile();
        }
        else if (!confFile.exists()) {
            createFile();
        }
    }

    private void createFile() {
        File confDir = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString()).toUri());
        File confFile = new File(Paths.get(confDir.toString(), "config.json").toUri());

        try {
            confFile.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(confFile));

            JSONObject root = new JSONObject();
            JSONObject config = new JSONObject();
            root.put("config", config);
            JSONArray keys = new JSONAPIKeyBuilder()
                    .withModel(Model.GEMINI_2_5_FLASH_LITE, "")
                    .withModel(Model.CHATGPT_4O, "")
                    .withModel(Model.CLAUDE_3_5_SONNET, "")
                    .build();

            config.put("keys", keys);

            bufferedWriter.write(root.toString());
            bufferedWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeConfigToFile() {
        ConfigurationState configurationState = ConfigurationState.instance;

        File confFile = new File(Paths.get(ConfigurationState.instance.getConfigurationDirectory().toString(), "config.json").toUri());

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(confFile))) {
            JSONObject root = new JSONObject();
            JSONObject config = new JSONObject();
            root.put("config", config);
            JSONArray keys = new JSONArray();
            config.put("keys", keys);

            int currIndex = 0;

            for (var e : configurationState.getKeys().entrySet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("model", e.getKey().getValue());
                jsonObject.put("key", e.getValue());
                keys.put(currIndex++, jsonObject);
            }

            bufferedWriter.write(root.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
