CREATE TABLE IF NOT EXISTS `feedback_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `nick_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `username` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `avatar` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像地址',
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `picture_url` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片地址',
  `picture_num` int(2) DEFAULT NULL COMMENT '图片数',
  `origin` int(1) NOT NULL COMMENT '消息来源：1 移动用户端,2 移动管理端 ',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备序列号',
  `mac` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'MAC地址',
  `recipient_id` int(11) NOT NULL COMMENT '收件人id',
  `recipient_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收件人姓名',
  `is_read` int(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0 未读，1已读',
  PRIMARY KEY (`id`),
  KEY `recipient_id_index` (`recipient_id`),
  KEY `sno_index` (`sno`),
  KEY `mac_index` (`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='问题反馈表'

CREATE TABLE IF NOT EXISTS `feedback_business` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `username` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `nick_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发件人手机号',
   `avatar` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像地址',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `picture_url` text COLLATE utf8mb4_unicode_ci COMMENT '图片地址',
  `picture_num` int(2) DEFAULT NULL COMMENT '图片数',
  `user_id` int(11) DEFAULT NULL COMMENT '系统用户ID',
  `recipient_id` int(11) DEFAULT NULL COMMENT '收件人ID',
  `recipient_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收件人名称',
  `is_read` int(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0 未读，1已读',
  PRIMARY KEY (`id`),
  KEY `recipient_id_index` (`recipient_id`),
  KEY `user_id_index` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业务系统表'

CREATE TABLE IF NOT EXISTS `sys_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `addresser_id` int(11) DEFAULT NULL COMMENT '发件人ID',
  `addresser_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发件人名称',
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `title` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `recipient_id` int(11) DEFAULT NULL COMMENT '收件人ID',
  `recipient_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收件人名称',
  `is_read` int(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0 未读，1已读',
  PRIMARY KEY (`id`),
  KEY `recipient_id_index` (`recipient_id`),
  KEY `addresser_id_index` (`addresser_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统消息表'