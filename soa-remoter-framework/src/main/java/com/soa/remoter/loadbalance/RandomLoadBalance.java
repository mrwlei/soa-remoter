package com.soa.remoter.loadbalance;

import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * 随机的负载均衡算法
 */
public class RandomLoadBalance implements LoadBalance {
    
    public NodeInfo doSelect(List<String> registryInfo) {
        Random random = new Random();
        int index = random.nextInt(registryInfo.size());
        String registry = registryInfo.get(index);
        
        JSONObject registryJo = JSONObject.parseObject(registry);
        Collection values = registryJo.values();
        JSONObject node = new JSONObject();
        for (Object value : values) {
            node = JSONObject.parseObject(value.toString());
        }
        
        JSONObject protocol = node.getJSONObject("protocol");
        NodeInfo nodeinfo = new NodeInfo();
        nodeinfo.setHost(protocol.get("host") != null ? protocol.getString("host") : "");
        nodeinfo.setPort(protocol.get("port") != null ? protocol.getString("port") : "");
        nodeinfo.setContextpath(protocol.get("contextpath") != null ? protocol.getString("contextpath") : "");

        return nodeinfo;
    }
}
