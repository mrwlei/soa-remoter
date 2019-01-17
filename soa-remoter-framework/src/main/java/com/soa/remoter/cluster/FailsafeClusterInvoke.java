package com.soa.remoter.cluster;

import com.soa.remoter.invoke.Invocation;
import com.soa.remoter.invoke.Invoke;

/**
 * 调用节点失败，直接忽略
 */
public class FailsafeClusterInvoke implements Cluster {
    
    public String invoke(Invocation invocation) throws Exception {
        Invoke invoke = invocation.getInvoke();
        
        try {
            return invoke.invoke(invocation);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "忽略";
        }
    }
    
}
