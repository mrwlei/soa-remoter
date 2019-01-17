package com.soa.remoter.cluster;

import com.soa.remoter.invoke.Invocation;
import com.soa.remoter.invoke.Invoke;

/**
 * 这个如果调用失败就自动切换到其他集群节点
 */
public class FailoverClusterInvoke implements Cluster {
    
    public String invoke(Invocation invocation) throws Exception {
        
        String retries = invocation.getReference().getRetries();
        Integer retriint = Integer.parseInt(retries);
        
        for (int i = 0; i < retriint; i++) {
            try {
                Invoke invoke = invocation.getInvoke();
                String result = invoke.invoke(invocation);
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        
        throw new RuntimeException("retries" + retries + "全部失败！！！！");
    }
    
}
