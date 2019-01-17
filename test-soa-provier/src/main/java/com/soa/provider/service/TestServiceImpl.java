package com.soa.provider.service;

import org.springframework.stereotype.Service;

/**
 * Created by wang-lei on 2019/1/16.
 */
@Service
public class TestServiceImpl implements TestService{
    @Override
    public String say(String param) {
        System.out.println("请求 Provider成功啦，参数为 : " + param);
        return "你好";
    }

    @Override
    public String saveOrder(String orderId) {
        System.out.println("保存订单");
        return "保存订单成功";
    }
}
