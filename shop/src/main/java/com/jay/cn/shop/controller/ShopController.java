package com.jay.cn.shop.controller;

import com.jay.cn.shop.entiy.Product;
import com.jay.cn.shop.service.impl.ShopServiceImpl;
import com.jay.cn.shop.utils.ThreadPoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lj
 * @version 1.0
 * @date 2021/10/18 22:16
 */
@RestController
public class ShopController {

    @Autowired
    ShopServiceImpl shopService;

    static ThreadPoolExecutor threadPool;

    static {
        threadPool = ThreadPoolUtils.getThreadPool();
    }

    @RequestMapping("/shop")
    public String transfer(HttpServletRequest request, HttpServletResponse response){
        String count = request.getParameter("count");
        String productId = request.getParameter("productId");
        Product product = new Product();
        product.setProductId(productId);
        product.setCount(Integer.parseInt(count));

        new Thread(() -> {
            try {
                shopService.shop(product);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return "下单成功";
    }

//    public static void main(String[] args) {
//        ArrayList<Thread> threads = new ArrayList<>();
//
//        ShopServiceImpl shopService  = new ShopServiceImpl();
//
//        for (int i = 0; i < 20; i++) {
//            new Thread(() -> {
//                Product product = new Product();
//                product.setProductId("1001");
//                product.setCount(Integer.parseInt("1"));
//                shopService.shop(product);
//            }, "Thread-" + i).start();
//        }
//
//    }
}
