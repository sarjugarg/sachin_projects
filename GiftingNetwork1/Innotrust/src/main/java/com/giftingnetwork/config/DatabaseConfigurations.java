package com.giftingnetwork.config;
 
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "db")
public class DatabaseConfigurations {
    private Map<String, DatabaseConfiguration> configurations = new HashMap<>();

    public Map<String, DatabaseConfiguration> getConfigurations() {
        return configurations;
    }
    public void setConfigurations(Map<String, DatabaseConfiguration> configurations) {
        this.configurations = configurations;
    }

    public Map<Object, Object> createTargetDataSources() {
        Map<Object, Object> result = new HashMap<>();
         configurations.forEach((key, value) ->  result.put(key, value.createDataSource()));
         return result;
    }
}
