package com.soa.remoter.loadbalance;

import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * 轮询的负载均衡算法
 */
public class RoundRobinLoadBalance implements LoadBalance {
    
    private static Integer index = 0;
    
    public NodeInfo doSelect(List<String> registryInfo) {
        synchronized (index) {
            if (index >= registryInfo.size()) {
                index = 0;
            }
            String registry = registryInfo.get(index);
            index++;
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
}
