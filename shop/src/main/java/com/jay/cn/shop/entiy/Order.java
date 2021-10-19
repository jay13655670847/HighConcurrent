package com.jay.cn.shop.entiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 21:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    String id;

    String productId;

    int counts;

    Date createTime;
}
