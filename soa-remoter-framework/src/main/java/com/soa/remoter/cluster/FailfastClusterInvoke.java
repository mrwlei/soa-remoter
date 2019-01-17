package com.soa.remoter.cluster;

import com.soa.remoter.invoke.Invocation;
import com.soa.remoter.invoke.Invoke;

/**
 * 这个如果调用节点异常，直接失败
 */
public class FailfastClusterInvoke implements Cluster {
    
    public String invoke(Invocation invocation) throws Exception {
        Invoke invoke = invocation.getInvoke();
        
        try {
            return invoke.invoke(invocation);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
}
