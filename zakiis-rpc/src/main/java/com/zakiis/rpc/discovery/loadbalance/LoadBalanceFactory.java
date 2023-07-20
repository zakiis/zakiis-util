package com.zakiis.rpc.discovery.loadbalance;


import com.zakiis.core.loader.EnhancedServiceLoader;
import com.zakiis.rpc.DefaultValues;
import com.zakiis.rpc.config.ConfigurationFactory;

public class LoadBalanceFactory {

    private static final String CLIENT_PREFIX = "client.";
    /**
     * The constant LOAD_BALANCE_PREFIX.
     */
    public static final String LOAD_BALANCE_PREFIX = CLIENT_PREFIX + "loadBalance.";

    public static final String LOAD_BALANCE_TYPE = LOAD_BALANCE_PREFIX + "type";

    public static final String RANDOM_LOAD_BALANCE = "RandomLoadBalance";

    public static final String XID_LOAD_BALANCE = "XID";

    public static final String ROUND_ROBIN_LOAD_BALANCE = "RoundRobinLoadBalance";

    public static final String CONSISTENT_HASH_LOAD_BALANCE = "ConsistentHashLoadBalance";

    public static final String LEAST_ACTIVE_LOAD_BALANCE = "LeastActiveLoadBalance";


    /**
     * Get instance.
     *
     * @return the instance
     */
    public static LoadBalance getInstance() {
        String config = ConfigurationFactory.getInstance().getConfig(LOAD_BALANCE_TYPE, DefaultValues.DEFAULT_LOAD_BALANCE);
        return EnhancedServiceLoader.load(LoadBalance.class, config);
    }
}
