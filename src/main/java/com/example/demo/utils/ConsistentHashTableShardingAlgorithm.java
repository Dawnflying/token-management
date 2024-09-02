package com.example.demo.utils;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;

public class ConsistentHashTableShardingAlgorithm implements PreciseShardingAlgorithm<Long>, RangeShardingAlgorithm<Long> {


    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        if (collection.isEmpty()) {
            return preciseShardingValue.getLogicTableName();
        }
        ConsistentHashing consistentHashing = new ConsistentHashing(collection.size());
        collection.forEach(consistentHashing::addNode);
        String logicalTableName = preciseShardingValue.getLogicTableName();
        String shardingValue = String.valueOf(preciseShardingValue.getValue());
        return consistentHashing.getNode(shardingValue);
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return collection;
    }
}
