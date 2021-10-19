package com.jay.cn.shop.service;

import com.jay.cn.shop.entiy.Order;
import com.jay.cn.shop.entiy.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
