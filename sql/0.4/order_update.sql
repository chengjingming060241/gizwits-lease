ALTER TABLE `order_base`
ADD COLUMN `real_money` DOUBLE(11,2) NULL COMMENT '订单金额中真实金额，只有此部分参与分润',
ADD COLUMN `promotion_money` DOUBLE(11,2) NULL COMMENT '订单的优惠金额，与real_money合起来就是amount，此部分不参与分润' ;
