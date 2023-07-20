package com.zakiis.rpc.config;


public interface ExtConfigurationProvider {
    /**
     * provide a AbstractConfiguration implementation instance
     * @param originalConfiguration
     * @return configuration
     */
    Configuration provide(Configuration originalConfiguration);
}
