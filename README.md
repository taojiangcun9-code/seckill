<<<<<<< HEAD
# 代码
## Redis
前端发起登录请求后，由UserService.java校验并生成String token并存入redis，封装成Result对象返回到前端。
```java
String token = UUID.randomUUID().toString().replace("-", "");

        // RedisKeys
        redisservice.set(
                RedisKeys.tokenKey(token),
                user.getId(),
                3600 * 24 //一天过期
        );
        return Result.success(token);
```
用户登录后跳转到商品页面，自动发起请求，GoodsService查询返回Result商品列表，并存入redis
```java
  for (SeckillGoods goods : list) {

            if (goods.getGoodsId() == null || goods.getStockCount() == null) {
                continue;
            }

            String stockKey = RedisKeys.stockKey(goods.getGoodsId());

            redisService.set(stockKey, goods.getStockCount());
```

用户点击秒杀以后，发送请求，SeckillController.java接收req和token，解析req的商品id，通过token查询用户id，载联合商品id和用户id查询是
否重复订单，再通知consumer下单。
```java
//查重
        String orderKey = "order:" + userId + ":" + goodsId;

        if (redisservice.exists(orderKey)) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        // 扣库存
        String stockKey = "stock:" + goodsId;
        String stockStr = redisservice.get(stockKey);

        System.out.println("stockStr = " + stockStr);

        if (stockStr == null) {
            return Result.error(CodeMsg.REPEATE_NOTFOUND);
        }

        int stock = Integer.parseInt(stockStr);

        if (stock <= 0) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        redisservice.set(stockKey, String.valueOf(stock - 1));

        // 通知consumer生成订单
        SeckillMessage msg = new SeckillMessage(userId, goodsId);
        seckillProducer.send(msg);
```

# 配置问题
## Redis
启动 redis-server.exe redis.windows.conf
进入窗口 redis-cli.exe
刷新  flushall
退出 Ctrl+C

## RabbitMq
启动 rabbitmq-server.bat

# 问题
+ 令牌桶限流
+ 库存扣减后并没有传到sql
