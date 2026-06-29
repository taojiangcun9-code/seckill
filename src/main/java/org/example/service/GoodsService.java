package org.example.service;

import org.example.entity.SeckillGoods;
import org.example.mapper.GoodsMapper;
import org.example.result.CodeMsg;
import org.example.result.Result;
import org.example.common.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 查询全部秒杀商品
     */
    public Result<List<SeckillGoods>> getAllSeckillGoods() {

        List<SeckillGoods> list = goodsMapper.findAllSeckillGoods();

        if (list == null || list.isEmpty()) {
            return Result.error(CodeMsg.SERVER_ERROR.fillArgs("暂无秒杀商品"));
        }

        for (SeckillGoods goods : list) {

            if (goods.getGoodsId() == null || goods.getStockCount() == null) {
                continue;
            }

            String stockKey = RedisKeys.stockKey(goods.getGoodsId());

            redisService.set(stockKey, goods.getStockCount());
        }

        return Result.success(list);
    }


    public SeckillGoods getByGoodsId(Long goodsId) {
        return goodsMapper.getByGoodsId(goodsId);
    }
}