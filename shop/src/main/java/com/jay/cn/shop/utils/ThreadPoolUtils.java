package com.jay.cn.shop.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/19 9:41
 */
public class ThreadPoolUtils {

    public static ThreadPoolExecutor getThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 0, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(20), new ThreadFactoryBuilder().setNameFormat("threadPool-shop").build(), new ThreadPoolExecutor.AbortPolicy());

        return threadPoolExecutor;
    }

}
