package vtrip.booking.booking_service.config.mapper;

import common.lib.vtrip.infrastructure.datasource.properties.mapper.IExternalConfigMappingProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomExternalConfigMapping implements IExternalConfigMappingProvider {
    @Override
    public Map<String, String> prefixMapping() {
        final Map<String, String> map = new ConcurrentHashMap<>();
        map.put("kafka_topic", "spring.kafka.consume-topics.");
        return map;
    }

    @Override
    public Map<String, String> keyMapping() {
        final Map<String, String> map = new ConcurrentHashMap<>();
        map.put("kafka_topic_ops_transaction_account_transaction_account_create",
                "spring.kafka.produce-topics.kafka_topic_ops_transaction_account_transaction_account_create");
        map.put("app_name1","app.name");
        return map;
    }
}
