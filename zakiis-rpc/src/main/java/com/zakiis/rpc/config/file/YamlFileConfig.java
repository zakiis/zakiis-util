package com.zakiis.rpc.config.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.zakiis.core.loader.LoadLevel;
import com.zakiis.core.loader.Scope;
import com.zakiis.rpc.config.FileConfigFactory;



@LoadLevel(name = FileConfigFactory.YAML_TYPE, order = 1, scope = Scope.PROTOTYPE)
public class YamlFileConfig implements FileConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlFileConfig.class);
    private Map configMap;

    public YamlFileConfig(File file, String name) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream is = new FileInputStream(file)) {
            configMap = yaml.load(is);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("file not found");
        }
    }

    @Override
    public String getString(String path) {
        try {
            Map config = configMap;
            String[] dataId = path.split("\\.");
            for (int i = 0; i < dataId.length - 1; i++) {
                if (config.containsKey(dataId[i])) {
                    config = (Map) config.get(dataId[i]);
                } else {
                    return null;
                }
            }
            Object value = config.get(dataId[dataId.length - 1]);
            return value == null ? null : String.valueOf(value);
        } catch (Exception e) {
            LOGGER.warn("get config data error" + path, e);
            return null;
        }
    }
}
