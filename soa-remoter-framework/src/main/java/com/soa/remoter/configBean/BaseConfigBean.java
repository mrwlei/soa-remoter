package com.soa.remoter.configBean;

import java.io.Serializable;

public abstract class BaseConfigBean implements Serializable {
    private static final long serialVersionUID = 667333278987L;

    public String id;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
}
