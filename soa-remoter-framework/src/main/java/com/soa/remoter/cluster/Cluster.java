package com.soa.remoter.cluster;

import com.soa.remoter.invoke.Invocation;

public interface Cluster {
    public String invoke(Invocation invocation) throws Exception;
}
