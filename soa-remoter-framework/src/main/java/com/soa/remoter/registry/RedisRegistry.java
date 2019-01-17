package com.soa.remoter.registry;

import com.alibaba.fastjson.JSONObject;
import com.soa.remoter.configBean.Protocol;
import com.soa.remoter.configBean.Registry;
import com.soa.remoter.configBean.Service;
import com.soa.remoter.registry.support.RedisApi;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis的注册中心处理类
 */
public class RedisRegistry implements BaseRegistry {
    
    public boolean registry(String ref, ApplicationContext application) {
        try {
            Protocol protocol = application.getBean(Protocol.class);
            Map<String, Service> services = application.getBeansOfType(Service.class);
            
            Registry registry = application.getBean(Registry.class);
            RedisApi.createJedisPool(registry.getAddress());
            for (Map.Entry<String, Service> entry : services.entrySet()) {
                if (entry.getValue().getRef().equals(ref)) {
                    JSONObject jo = new JSONObject();
                    jo.put("protocol", JSONObject.toJSONString(protocol));
                    jo.put("service", JSONObject.toJSONString(entry.getValue()));
                    
                    JSONObject ipport = new JSONObject();
                    ipport.put(protocol.getHost() + ":" + protocol.getPort(),jo);
                    //                RedisApi.lpush(ipport, ref);
                    lpush(ipport, ref);
                }
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void lpush(JSONObject ipport, String key) {
        if (RedisApi.exists(key)) {
            Set<String> keys = ipport.keySet();
            String ipportStr = "";
            //这个循环里面只会循环一次
            for (String kk : keys) {
                ipportStr = kk;
            }
            
            //拿redis对应key里面的 内容
            List<String> registryInfo = RedisApi.lrange(key);
            List<String> newRegistry = new ArrayList<String>();
            
            boolean isold = false;
            
            for (String node : registryInfo) {
                JSONObject jo = JSONObject.parseObject(node);
                if (jo.containsKey(ipportStr)) {
                    newRegistry.add(ipport.toJSONString());
                    isold = true;
                }
                else {
                    newRegistry.add(node);
                }
            }
            
            if (isold) {
                //老机器启动去重
                if (newRegistry.size() > 0) {
                    RedisApi.del(key);
                    String[] newReStr = new String[newRegistry.size()];
                    for (int i = 0; i < newRegistry.size(); i++) {
                        newReStr[i] = newRegistry.get(i);
                    }
                    RedisApi.lpush(key, newReStr);
                }
            }
            else {
                //这里是加入新启动的机器
                RedisApi.lpush(key, ipport.toJSONString());
            }
        } else {
            //第一次启动
            RedisApi.lpush(key, ipport.toJSONString());
        }
    }
    
    public List<String> getRegistry(String id, ApplicationContext application) {
        try {
            Registry registry = application.getBean(Registry.class);
            RedisApi.createJedisPool(registry.getAddress());
            if (RedisApi.exists(id)) {
                //拿key对应的list
                return RedisApi.lrange(id);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
