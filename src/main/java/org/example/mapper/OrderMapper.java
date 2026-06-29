package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {

    void insertOrder(@Param("userId") Long userId,
                     @Param("goodsId") Long goodsId);

    Integer checkOrder(@Param("userId") Long userId,
                       @Param("goodsId") Long goodsId);
}