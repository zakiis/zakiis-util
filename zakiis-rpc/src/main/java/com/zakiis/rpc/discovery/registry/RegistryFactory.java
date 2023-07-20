package com.zakiis.rpc.discovery.registry;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.loader.EnhancedServiceLoader;
import com.zakiis.rpc.config.ConfigurationFactory;
import com.zakiis.rpc.config.ConfigurationKeys;
import com.zakiis.rpc.exception.NotSupportYetException;



public class RegistryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryFactory.class);

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RegistryService getInstance() {
        return RegistryFactoryHolder.INSTANCE;
    }

    private static RegistryService buildRegistryService() {
        RegistryType registryType;
        String registryTypeName = ConfigurationFactory.CURRENT_FILE_INSTANCE.getConfig(
            ConfigurationKeys.FILE_ROOT_REGISTRY + ConfigurationKeys.FILE_CONFIG_SPLIT_CHAR
                + ConfigurationKeys.FILE_ROOT_TYPE);
        LOGGER.info("use registry center type: {}", registryTypeName);
        try {
            registryType = RegistryType.getType(registryTypeName);
        } catch (Exception exx) {
            throw new NotSupportYetException("not support registry type: " + registryTypeName);
        }
        return EnhancedServiceLoader.load(RegistryProvider.class, Objects.requireNonNull(registryType).name()).provide();

    }

    private static class RegistryFactoryHolder {
        private static final RegistryService INSTANCE = buildRegistryService();
    }
}