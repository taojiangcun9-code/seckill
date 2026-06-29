package org.example.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.entity.SeckillGoods;

import java.util.List;

@Mapper
public interface GoodsMapper {

    List<SeckillGoods> findAllSeckillGoods();

    SeckillGoods getByGoodsId(Long goodsId);
}