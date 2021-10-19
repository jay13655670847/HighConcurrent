package com.example.test.utils;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/19 12:06
 */
public class RedissonTemplate {

    public static RedissonClient getRedis(){
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://192.168.216.130:6379")
                .addNodeAddress("redis://192.168.216.130:6380")
                .addNodeAddress("redis://192.168.216.130:6381")
                .addNodeAddress("redis://192.168.216.131:6379")
                .addNodeAddress("redis://192.168.216.131:6380")
                .addNodeAddress("redis://192.168.216.131:6381");

        RedissonClient redissonClient = Redisson.create(config);
        return  redissonClient;
    }

    public static void main(String[] args) throws InterruptedException {
        boolean lock = getRedis().getLock("lock").tryLock(30, TimeUnit.SECONDS);
        System.out.println("获取锁："+lock);
        while(true){

        }
    }
}
