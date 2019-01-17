package com.soa.remoter.invoke;

import com.alibaba.fastjson.JSONObject;
import com.soa.remoter.configBean.Reference;
import com.soa.remoter.loadbalance.LoadBalance;
import com.soa.remoter.loadbalance.NodeInfo;
import com.soa.remoter.remote.rmi.RmiUtil;
import com.soa.remoter.remote.rmi.SoaRmi;

import java.rmi.RemoteException;
import java.util.List;

/**
 *  RMi的通讯协议
 */
public class RmiInvoke implements Invoke {
    
    public String invoke(Invocation invocation) throws Exception {
        try {
            List<String> registryInfo = invocation.getReference().getRegistryInfo();
            //这个是负载均衡算法
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
            
            RmiUtil rmi = new RmiUtil();
            SoaRmi soarmi = rmi.startRmiClient(nodeinfo, "remotesoarmi");
            
            return soarmi.invoke(sendparam.toJSONString());
        }
        catch (RemoteException e) {
            throw e;
        }
    }
}
