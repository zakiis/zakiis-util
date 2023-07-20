package com.zakiis.rpc.config;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ConfigurationChangeEvent {

	private String dataId;
    private String oldValue;
    private String newValue;
    private String namespace;
    private ConfigurationChangeType changeType;
    private static final String DEFAULT_NAMESPACE = "DEFAULT";


    public ConfigurationChangeEvent(){

    }

    public ConfigurationChangeEvent(String dataId, String newValue) {
        this(dataId, DEFAULT_NAMESPACE, null, newValue, ConfigurationChangeType.MODIFY);
    }

    public ConfigurationChangeEvent(String dataId, String namespace, String oldValue, String newValue,
                                    ConfigurationChangeType type) {
        this.dataId = dataId;
        this.namespace = namespace;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changeType = type;
    }
    
}
