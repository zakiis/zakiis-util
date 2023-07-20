package com.zakiis.rpc.discovery.registry;


public interface RegistryProvider {
    /**
     * provide a registry implementation instance
     * @return RegistryService
     */
    RegistryService provide();
}

