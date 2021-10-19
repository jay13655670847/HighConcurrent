package com.example.test.test;

import com.example.test.utils.RedissonTemplate;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/19 16:56
 */
public class test {
    public static void main(String[] args) {
        RedissonClient redis = RedissonTemplate.getRedis();

        boolean lock = redis.getLock("1001_lock").tryLock();
        System.out.println(lock);
    }
}
