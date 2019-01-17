package com.soa.remoter.registry;

import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 注册中心
 */
public interface BaseRegistry {
    public boolean registry(String param, ApplicationContext application);
    
    public List<String> getRegistry(String id, ApplicationContext application);
}
