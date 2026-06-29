package org.example.controller;
import org.example.entity.SeckillGoods;
import org.example.result.Result;
import org.example.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/seckill/list")
    public Result<List<SeckillGoods>> list() {
        return goodsService.getAllSeckillGoods();
    }
}