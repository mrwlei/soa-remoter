package com.soa.remoter.proxy.advice;

import com.soa.remoter.cluster.Cluster;
import com.soa.remoter.configBean.Reference;
import com.soa.remoter.invoke.Invocation;
import com.soa.remoter.invoke.Invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * InvokeInvocationHandler 这个是一个advice，在这个advice里面就进行了rpc的远程调用
 * rpc：http、rmi、netty
 */
public class InvokeInvocationHandler implements InvocationHandler {
    
    private Invoke invoke;
    
    private Reference reference;
    
    public InvokeInvocationHandler(Invoke invoke, Reference reference) {
        this.invoke = invoke;
        this.reference = reference;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在这个invoke里面最终要调用多个远程的provider
        Invocation invocation = new Invocation();
        invocation.setMethod(method);
        invocation.setObjs(args);
        invocation.setReference(reference);
        invocation.setInvoke(invoke);
        //        String result = invoke.invoke(invocation);
        Cluster cluster = reference.getClusters().get(reference.getCluster());

        String result = cluster.invoke(invocation);
        return result;
    }
}
