package com.soa.remoter.configBean;

import com.soa.remoter.cluster.Cluster;
import com.soa.remoter.cluster.FailfastClusterInvoke;
import com.soa.remoter.cluster.FailoverClusterInvoke;
import com.soa.remoter.cluster.FailsafeClusterInvoke;
import com.soa.remoter.invoke.HttpInvoke;
import com.soa.remoter.invoke.Invoke;
import com.soa.remoter.invoke.NettyInvoke;
import com.soa.remoter.invoke.RmiInvoke;
import com.soa.remoter.loadbalance.LoadBalance;
import com.soa.remoter.loadbalance.RandomLoadBalance;
import com.soa.remoter.loadbalance.RoundRobinLoadBalance;
import com.soa.remoter.proxy.advice.InvokeInvocationHandler;
import com.soa.remoter.registry.BaseRegistryDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reference extends BaseConfigBean implements FactoryBean,
        InitializingBean, ApplicationContextAware {
    
    private String intf;
    
    private String loadbalance;
    
    private String protocol;
    
    private String cluster;
    
    private String retries;
    
    private static ApplicationContext application;
    
    private Invoke invoke;
    
    private static Map<String, Invoke> invokes = new HashMap<String, Invoke>();
    
    private static Map<String, LoadBalance> loadBalances = new HashMap<String, LoadBalance>();
    
    private static Map<String, Cluster> clusters = new HashMap<String, Cluster>();
    
    /** 
     * @Fields registryInfo 这个是生产者的多个服务的列表 
     */
    
    private List<String> registryInfo = new ArrayList<String>();
    
    static {
        invokes.put("http", new HttpInvoke());
        invokes.put("rmi", new RmiInvoke());
        invokes.put("netty", new NettyInvoke());
        
        loadBalances.put("romdom", new RandomLoadBalance());
        loadBalances.put("roundrob", new RoundRobinLoadBalance());
        
        clusters.put("failover", new FailoverClusterInvoke());
        clusters.put("failfast", new FailfastClusterInvoke());
        clusters.put("failsafe", new FailsafeClusterInvoke());
    }
    
    public List<String> getRegistryInfo() {
        return registryInfo;
    }
    
    public void setRegistryInfo(List<String> registryInfo) {
        this.registryInfo = registryInfo;
    }
    
    public Reference() {
        System.out.println("Reference!构造");
    }
    
    public String getIntf() {
        return intf;
    }
    
    public void setIntf(String intf) {
        this.intf = intf;
    }
    
    public String getLoadbalance() {
        return loadbalance;
    }
    
    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public Object getObject() throws Exception {
        System.out.println("Reference! getObject");
        if (protocol != null && !"".equals(protocol)) {
            invoke = invokes.get(protocol);
        }
        else {
            Protocol pro = application.getBean(Protocol.class);
            if (pro != null) {
                invoke = invokes.get(pro.getName());
            }
            else {
                invoke = invokes.get("http");
            }
        }
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[] {Class.forName(intf)},
                new InvokeInvocationHandler(invoke, this));
    }
    
    public Class getObjectType() {
        try {
            if (intf != null && !"".equals(intf)) {
                return Class.forName(intf);
            }
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean isSingleton() {
        // TODO Auto-generated method stub
        return true;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        Reference.application = applicationContext;
    }
    
    public static ApplicationContext getApplication() {
        return application;
    }
    
    public void afterPropertiesSet() throws Exception {
        registryInfo = BaseRegistryDelegate.getRegistry(id, application);
        System.out.println(registryInfo);
        
        //完成订阅
        //        RedisApi.subsribe("channel" + id, new RedisServerRegistry());
    }
    
    public static Map<String, LoadBalance> getLoadBalances() {
        return loadBalances;
    }
    
    public static void setLoadBalances(Map<String, LoadBalance> loadBalances) {
        Reference.loadBalances = loadBalances;
    }
    
    public String getCluster() {
        return cluster;
    }
    
    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
    
    public String getRetries() {
        return retries;
    }
    
    public void setRetries(String retries) {
        this.retries = retries;
    }
    
    public static Map<String, Cluster> getClusters() {
        return clusters;
    }
    
    public static void setClusters(Map<String, Cluster> clusters) {
        Reference.clusters = clusters;
    }
    
}
