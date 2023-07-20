package com.zakiis.rpc.discovery.registry;


public enum RegistryType {
    /**
     * File registry type.
     */
    File,
    /**
     * ZK registry type.
     */
    ZK,
    /**
     * Redis registry type.
     */
    Redis,
    /**
     * Nacos registry type.
     */
    Nacos,
    /**
     * Eureka registry type.
     */
    Eureka,
    /**
     * Consul registry type
     */
    Consul,
    /**
     * Etcd3 registry type
     */
    Etcd3,
    /**
     * Sofa registry type
     */
    Sofa,
    /**
     * Custom registry type
     */
    Custom;

    /**
     * Gets type.
     *
     * @param name the name
     * @return the type
     */
    public static RegistryType getType(String name) {
        for (RegistryType registryType : RegistryType.values()) {
            if (registryType.name().equalsIgnoreCase(name)) {
                return registryType;
            }
        }
        throw new IllegalArgumentException("not support registry type: " + name);
    }
}
