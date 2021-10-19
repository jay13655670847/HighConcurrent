package com.jay.cn.consumer.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.jay.cn.consumer.dao.OrderDao;
import com.jay.cn.consumer.entiy.Order;
import com.jay.cn.consumer.entiy.Product;
import com.jay.cn.consumer.service.ShopService;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.UUID;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 22:18
 */
@Service
public class ShopServiceImpl implements ShopService {

    Logger logger = LoggerFactory.getLogger(ShopServiceImpl.class);

    @Autowired
    OrderDao orderDao;

    private static JedisCluster jedis;

    @Autowired
    RocketMQTemplate rocketMQTemplate;


    @Override
    public Product queryOrderCount(String productId) {
        return orderDao.queryOrderCount(productId);
    }

    @Override
    public int updateProduct(int count, String productId) {
        return orderDao.updateProduct(count, productId);
    }

    @Override
    public int addOrder(Order order) {
        return orderDao.addOrder(order);
    }

    @Override
    public int isExist(String id) {
        return orderDao.isExist(id);
    }




}
