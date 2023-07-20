package com.zakiis.rpc.config;


public interface ConfigurationProvider {
    /**
     * provide a AbstractConfiguration implementation instance
     * @return Configuration
     */
    Configuration provide();
}
