package com.zakiis.rpc.discovery.registry;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.zakiis.rpc.config.ConfigurationCache;
import com.zakiis.rpc.config.ConfigurationFactory;

/**
 * The interface of Registry service.
 * @date 2023-07-06 14:08:38
 * @author Liu Zhenghua
 */
public interface RegistryService<T> {

	/**
     * The constant PREFIX_SERVICE_MAPPING.
     */
    String PREFIX_SERVICE_MAPPING = "vgroupMapping.";
    /**
     * The constant PREFIX_SERVICE_ROOT.
     */
    String PREFIX_SERVICE_ROOT = "service";
    /**
     * The constant CONFIG_SPLIT_CHAR.
     */
    String CONFIG_SPLIT_CHAR = ".";

    Set<String> SERVICE_GROUP_NAME = new HashSet<>();

    /**
     * Service node health check
     */
    Map<String,List<InetSocketAddress>> CURRENT_ADDRESS_MAP = new ConcurrentHashMap<>();
    /**
     * Register.
     *
     * @param address the address
     * @throws Exception the exception
     */
    void register(InetSocketAddress address) throws Exception;

    /**
     * Unregister.
     *
     * @param address the address
     * @throws Exception the exception
     */
    void unregister(InetSocketAddress address) throws Exception;

    /**
     * Subscribe.
     *
     * @param cluster  the cluster
     * @param listener the listener
     * @throws Exception the exception
     */
    void subscribe(String cluster, T listener) throws Exception;

    /**
     * Unsubscribe.
     *
     * @param cluster  the cluster
     * @param listener the listener
     * @throws Exception the exception
     */
    void unsubscribe(String cluster, T listener) throws Exception;

    /**
     * Lookup list.
     *
     * @param key the key
     * @return the list
     * @throws Exception the exception
     */
    List<InetSocketAddress> lookup(String key) throws Exception;

    /**
     * Close.
     * @throws Exception the exception
     */
    void close() throws Exception;

    /**
     * Get current service group name
     *
     * @param key service group
     * @return the service group name
     */
    default String getServiceGroup(String key) {
        key = PREFIX_SERVICE_ROOT + CONFIG_SPLIT_CHAR + PREFIX_SERVICE_MAPPING + key;
        if (!SERVICE_GROUP_NAME.contains(key)) {
            ConfigurationCache.addConfigListener(key);
            SERVICE_GROUP_NAME.add(key);
        }
        return ConfigurationFactory.getInstance().getConfig(key);
    }

    default List<InetSocketAddress> aliveLookup(String transactionServiceGroup) {
        return CURRENT_ADDRESS_MAP.computeIfAbsent(transactionServiceGroup, k -> new ArrayList<>());
    }

    default List<InetSocketAddress> refreshAliveLookup(String transactionServiceGroup,
        List<InetSocketAddress> aliveAddress) {
        return CURRENT_ADDRESS_MAP.put(transactionServiceGroup, aliveAddress);
    }

}
