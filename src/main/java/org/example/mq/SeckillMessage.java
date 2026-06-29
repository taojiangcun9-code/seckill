package org.example.mq;

public class SeckillMessage {
    private Long userId;
    private Long goodsId;

    public SeckillMessage() {}

    public SeckillMessage(Long userId, Long goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }

    public Long getUserId() { return userId; }
    public Long getGoodsId() { return goodsId; }
}