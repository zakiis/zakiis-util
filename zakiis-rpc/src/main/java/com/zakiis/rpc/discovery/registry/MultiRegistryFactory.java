package com.zakiis.rpc.discovery.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zakiis.core.loader.EnhancedServiceLoader;
import com.zakiis.rpc.config.ConfigurationFactory;
import com.zakiis.rpc.config.ConfigurationKeys;
import com.zakiis.rpc.exception.NotSupportYetException;


public class MultiRegistryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiRegistryFactory.class);
    private static final String REGISTRY_TYPE_SPLIT_CHAR = ",";

    /**
     * Gets instances.
     *
     * @return the instance list
     */
    @SuppressWarnings("rawtypes")
	public static List<RegistryService> getInstances() {
        return MultiRegistryFactoryHolder.INSTANCES;
    }

    @SuppressWarnings("rawtypes")
	private static List<RegistryService> buildRegistryServices() {
        List<RegistryService> registryServices = new ArrayList<>();
        String registryTypeNamesStr =
            ConfigurationFactory.CURRENT_FILE_INSTANCE.getConfig(ConfigurationKeys.FILE_ROOT_REGISTRY
                + ConfigurationKeys.FILE_CONFIG_SPLIT_CHAR + ConfigurationKeys.FILE_ROOT_TYPE);
        if (StringUtils.isBlank(registryTypeNamesStr)) {
            registryTypeNamesStr = RegistryType.File.name();
        }
        LOGGER.info("use multi registry center type: {}", registryTypeNamesStr);
        String[] registryTypeNames = registryTypeNamesStr.split(REGISTRY_TYPE_SPLIT_CHAR);
        for (String registryTypeName : registryTypeNames) {
            RegistryType registryType;
            try {
                registryType = RegistryType.getType(registryTypeName);
            } catch (Exception exx) {
                throw new NotSupportYetException("not support registry type: " + registryTypeName);
            }
            RegistryService registryService = EnhancedServiceLoader
                .load(RegistryProvider.class, Objects.requireNonNull(registryType).name()).provide();
            registryServices.add(registryService);
        }
        return registryServices;
    }

    private static class MultiRegistryFactoryHolder {
        @SuppressWarnings("rawtypes")
		private static final List<RegistryService> INSTANCES = buildRegistryServices();
    }
}