package com.jay.cn.shop.utils;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 22:12
 */
public class JedisClusterTemplate {

    private static JedisCluster jedis;
    static {
        // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort("192.168.216.130", 6379));
        hostAndPortsSet.add(new HostAndPort("192.168.216.130", 6380));
        hostAndPortsSet.add(new HostAndPort("192.168.216.130", 6381));
        hostAndPortsSet.add(new HostAndPort("192.168.216.131", 6379));
        hostAndPortsSet.add(new HostAndPort("192.168.216.131", 6380));
        hostAndPortsSet.add(new HostAndPort("192.168.216.131", 6381));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        jedis = new JedisCluster(hostAndPortsSet, jedisPoolConfig);
    }

    public static JedisCluster getJedis(){
        return jedis;
    }

}
