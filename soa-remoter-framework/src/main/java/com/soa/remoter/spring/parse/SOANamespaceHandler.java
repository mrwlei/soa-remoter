package com.soa.remoter.spring.parse;

import com.soa.remoter.configBean.Protocol;
import com.soa.remoter.configBean.Reference;
import com.soa.remoter.configBean.Registry;
import com.soa.remoter.configBean.Service;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SOANamespaceHandler extends NamespaceHandlerSupport {
    
    public void init() {
        registerBeanDefinitionParser("registry",new RegistryBeanDefinitionParse(Registry.class));
        registerBeanDefinitionParser("protocol",new ProtocolBeanDefinitionParse(Protocol.class));
        registerBeanDefinitionParser("reference",new ReferenceBeanDifinitionParse(Reference.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
    }
    
}
