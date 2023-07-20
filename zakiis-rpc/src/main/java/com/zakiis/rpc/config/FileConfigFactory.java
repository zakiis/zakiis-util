package com.zakiis.rpc.config;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Set;

import com.zakiis.core.loader.EnhancedServiceLoader;
import com.zakiis.rpc.config.file.FileConfig;

public class FileConfigFactory {

    public static final String DEFAULT_TYPE = "CONF";

    public static final String YAML_TYPE = "YAML";

    private static final LinkedHashMap<String, String> SUFFIX_MAP = new LinkedHashMap<String, String>(4) {
		private static final long serialVersionUID = 799169877391687685L;
		{
            put("conf", DEFAULT_TYPE);
            put("properties", DEFAULT_TYPE);
            put("yml", YAML_TYPE);
        }
    };


    public static FileConfig load() {
        return loadService(DEFAULT_TYPE, null, null);
    }

    public static FileConfig load(File targetFile, String name) {
        String fileName = targetFile.getName();
        String configType = getConfigType(fileName);
        return loadService(configType, new Class[]{File.class, String.class}, new Object[]{targetFile, name});
    }

    private static String getConfigType(String fileName) {
        String configType = DEFAULT_TYPE;
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex > 0) {
            configType = SUFFIX_MAP.getOrDefault(fileName.substring(suffixIndex + 1), DEFAULT_TYPE);
        }

        return configType;
    }

    private static FileConfig loadService(String name, @SuppressWarnings("rawtypes") Class[] argsType, Object[] args) {
        FileConfig fileConfig = EnhancedServiceLoader.load(FileConfig.class, name, argsType, args);
        return fileConfig;
    }

    public static Set<String> getSuffixSet() {
        return SUFFIX_MAP.keySet();
    }

    public synchronized static void register(String suffix, String beanActiveName) {
        SUFFIX_MAP.put(suffix, beanActiveName);
    }


}
