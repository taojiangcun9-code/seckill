package org.example.controller;
import org.example.entity.SeckillGoods;
import org.example.mq.SeckillMessage;
import org.example.mq.SeckillProducer;
import org.example.result.CodeMsg;
import org.example.result.Result;
import org.example.service.GoodsService;
import org.example.service.OrderService;
import org.example.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.example.service.RedisService;
import org.example.dto.SeckillRequest;
import org.example.service.SeckillService;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisService redisservice;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillProducer seckillProducer;

    @Autowired
    private  SeckillService seckillService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/do_seckill")
    public Result<String> seckill(@RequestBody SeckillRequest req,
                                  @RequestHeader("Authorization") String token) {

        Long userId = tokenService.getUserId(token);

        if (userId == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        if (userId == null) {
            return Result.error(CodeMsg.SERVER_ERROR.fillArgs("未登录"));
        }


        Long goodsId = req.getGoodsId();

        if (goodsId == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }


        System.out.println("goodsId = " + goodsId);

        SeckillGoods goods = goodsService.getByGoodsId(goodsId);

        if (goods == null) {
            return Result.error(CodeMsg.REPEATE_NOTFOUND);
        }

        Date now = new Date();

        if (now.before(goods.getStartDate())) {
            return Result.error(CodeMsg.REPEATE_NOTFOUND);
        }

        if (now.after(goods.getEndDate())) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        String orderKey = "order:" + userId + ":" + goodsId;

        if (redisservice.exists(orderKey)) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }


        Long result = seckillService.luaDeductStock(goodsId);

        if (result == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        if (result == -2) {
            return Result.error(CodeMsg.REPEATE_NOTFOUND);
        }

        if (result == -1) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        SeckillMessage msg = new SeckillMessage(userId, goodsId);
        seckillProducer.send(msg);

        return Result.success("秒杀成功 user=" + userId);
    }
}
