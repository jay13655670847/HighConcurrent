package com.jay.cn.consumer.message;

import com.alibaba.fastjson.JSONObject;
import com.jay.cn.consumer.entiy.Order;
import com.jay.cn.consumer.service.impl.ShopServiceImpl;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 22:58
 */
@Component
@RocketMQMessageListener(consumerGroup = "consumer_order",topic = "topic_order")
public class ConsumerListenter  implements RocketMQListener<String> {

    Class clazz;
    Logger logger = LoggerFactory.getLogger(ConsumerListenter.class);

    @Autowired
    ShopServiceImpl shopService;

    @Override
    public void onMessage(String message) {
        logger.info("consumer开始消费："+message);
        Order order = JSONObject.parseObject(message, Order.class);
        int exist = shopService.isExist(order.getId());

        if (exist>0){
            logger.info("数据库已存在");
            return;
        }
        int i = shopService.addOrder(order);
        shopService.updateProduct(order.getCounts(),order.getProductId());
        logger.info("consumer消费完成："+order.getId());
    }
}
