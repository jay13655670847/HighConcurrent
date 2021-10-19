package com.jay.cn.consumer.service;

import com.jay.cn.consumer.entiy.Order;
import com.jay.cn.consumer.entiy.Product;


/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 22:17
 */
public interface ShopService {

    Product queryOrderCount(String productId);

    int updateProduct(int count, String productId);

    int addOrder(Order order);

    int isExist(String id);

}
