package org.example.service;

import org.example.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    public void insertOrder(long userId,long goodsId){

        orderMapper.insertOrder(userId,goodsId);
    }
}
