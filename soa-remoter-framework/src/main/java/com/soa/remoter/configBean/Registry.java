package com.soa.remoter.configBean;

import com.soa.remoter.registry.BaseRegistry;
import com.soa.remoter.registry.RedisRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

public class Registry extends BaseConfigBean implements InitializingBean,
        ApplicationContextAware {
    
    public ApplicationContext application;
    
    private static final Map<String, BaseRegistry> registryMap = new HashMap<String, BaseRegistry>();
    
    static {
        registryMap.put("redis", new RedisRegistry());
    }
    
    private static final long serialVersionUID = 45672141098765L;
    
    private String protocol;
    
    private String address;
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.application = applicationContext;
        
    }

    public void afterPropertiesSet() throws Exception {
        
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public static Map<String, BaseRegistry> getRegistryMap() {
        return registryMap;
    }

}
