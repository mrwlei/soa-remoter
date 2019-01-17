package com.soa.remoter.invoke;

/**
 * 返回String，用json的方式进行通信
 */
public interface Invoke {
    public String invoke(Invocation invocation) throws Exception;
}
