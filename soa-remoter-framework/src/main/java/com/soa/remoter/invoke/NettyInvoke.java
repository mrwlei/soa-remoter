package com.soa.remoter.invoke;

import com.alibaba.fastjson.JSONObject;
import com.soa.remoter.configBean.Reference;
import com.soa.remoter.loadbalance.LoadBalance;
import com.soa.remoter.loadbalance.NodeInfo;
import com.soa.remoter.remote.netty.NettyUtil;

import java.util.List;

/**
 * netty通讯协议
 */
public class NettyInvoke implements Invoke {
    
    public String invoke(Invocation invocation) throws Exception {
        try {
            List<String> registryInfo = invocation.getReference().getRegistryInfo();
            //负载均衡算法
            String loadbalance = invocation.getReference().getLoadbalance();
            Reference reference = invocation.getReference();
            LoadBalance loadbalanceBean = reference.getLoadBalances().get(loadbalance);
            
            NodeInfo nodeinfo = loadbalanceBean.doSelect(registryInfo);
            
            //调用远程的生产者是传输的json字符串
            //根据serviceid去对端生产者的spring容器中获取serviceid对应的实例
            //根据methodName和methodType获取实例的method对象
            //然后反射调用method方法
            JSONObject sendparam = new JSONObject();
            sendparam.put("methodName", invocation.getMethod().getName());
            sendparam.put("methodParams", invocation.getObjs());
            sendparam.put("serviceId", reference.getId());
            sendparam.put("paramTypes", invocation.getMethod().getParameterTypes());
            
            return NettyUtil.sendMsg(nodeinfo.getHost(),
                    nodeinfo.getPort(),
                    sendparam.toJSONString());
        }
        catch (Exception e) {
            throw e;
        }
    }
    
}
