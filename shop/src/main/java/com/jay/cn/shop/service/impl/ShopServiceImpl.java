package com.jay.cn.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jay.cn.shop.dao.OrderDao;
import com.jay.cn.shop.entiy.Order;
import com.jay.cn.shop.entiy.Product;
import com.jay.cn.shop.service.ShopService;
import com.jay.cn.shop.utils.JedisClusterTemplate;
import com.jay.cn.shop.utils.RedissonTemplate;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    private static RedissonClient redissonClient;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    static {
         jedis = JedisClusterTemplate.getJedis();
         redissonClient = RedissonTemplate.getRedis();
    }

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

    volatile long counts = 0;

    public void shop (Product product) throws InterruptedException {
        //??????key
        String key = product.getProductId() + "_count";

        //????????????????????????????????????
        if (!redissonClient.getAtomicLong(key).isExists()){
            boolean tryLock = redissonClient.getLock("query_lock").tryLock(3, TimeUnit.SECONDS);
            if (tryLock){
                if (!redissonClient.getAtomicLong(key).isExists()){
                    logger.info("redis???count????????????????????????");
                    Product queryOrderCount = orderDao.queryOrderCount(product.getProductId());
                    redissonClient.getAtomicLong(key).set(queryOrderCount.getCount());
                    counts = queryOrderCount.getCount();
                }
            }
        }else {
            counts = redissonClient.getAtomicLong(key).get();
        }

        if (counts >0 && product.getCount() <= counts){

            for (int i = 0; i < 5; i++) {
                RLock lock = redissonClient.getLock(product.getProductId() + "_lock");

                logger.info(Thread.currentThread().getName()+"-???????????????:"+"---"+counts);

                if (lock.tryLock(3, TimeUnit.SECONDS) && counts>0){
                    counts = redissonClient.getAtomicLong(key).get();
                    logger.info(Thread.currentThread().getName()+"-?????????:"+"---"+counts);
                    if (counts >0 && product.getCount() <= counts){
                        //redis?????????
                        long l = redissonClient.getAtomicLong(key).decrementAndGet();
                        logger.info(Thread.currentThread().getName()+"-?????????:"+"---"+l);

                        //??????Bean
                        Order order = new Order();
                        order.setId(UUID.randomUUID().toString().replace("-",""));
                        order.setCounts(product.getCount()* -1);
                        order.setProductId(product.getProductId());
                        order.setCreateTime(new Date());

                        //?????????
                        Message<Order> message = MessageBuilder.withPayload(order).build();
                        SendResult sendResult = rocketMQTemplate.syncSend("topic_order", message);
                        logger.info("sendResult:"+ JSONObject.toJSONString(sendResult));

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock.unlock();
                        logger.info(Thread.currentThread().getName()+"?????????");
                        break;
                    }else{
                        logger.info("???????????????????????????~~~");
                        lock.unlock();
                        logger.info(Thread.currentThread().getName()+"?????????");
                        break;
                    }

                }else{
                    try {
                        //long l = redissonClient.getAtomicLong(Thread.currentThread().getName()).incrementAndGet();
                        logger.info(Thread.currentThread().getName()+"-???????????????:"+"??????????????????"+redissonClient.getAtomicLong(key).get()+",???????????????");
                        Thread.sleep(200);
                        if (redissonClient.getAtomicLong(key).get()<=0){
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            return;

        }else{
            logger.info("?????????????????????~~~"+redissonClient.getAtomicLong(key).get());
            return;
        }
    }

}
