package com.jay.cn.shop.dao;

import com.jay.cn.shop.entiy.Order;
import com.jay.cn.shop.entiy.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 21:56
 */
@Mapper
@Component
public interface OrderDao {

    @Results({
            @Result(property = "count",column = "count")
    })
    @Select("select count from product where product_id=#{productId}")
    Product queryOrderCount(String productId);

    @Update("update product set count=count + #{count} where product_id=#{productId}")
    int updateProduct(@Param("count")int count,@Param("productId")String productId);

    @Insert("insert into order_info values (#{id},#{productId},#{createTime},#{counts})")
    int addOrder(Order order);

    @Select("select count(1) from order_info where id=#{id}")
    int isExist(String id);

}
