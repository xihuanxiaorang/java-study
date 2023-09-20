CREATE TABLE IF NOT EXISTS `author`
(
    `id`    INT(50)      NOT NULL COMMENT '作者id' AUTO_INCREMENT,
    `name`  VARCHAR(30)  NOT NULL COMMENT '名字',
    `age`   INT(30)      NOT NULL COMMENT '年龄',
    `sex`   INT(30)      NOT NULL COMMENT '性别',
    `email` VARCHAR(255) NOT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `article`
(
    `id`          INT(50)      NOT NULL COMMENT '文章id' AUTO_INCREMENT,
    `author_id`   INT(50)      NOT NULL COMMENT '作者id',
    `title`       VARCHAR(100) NOT NULL COMMENT '标题',
    `type`        INT(30)      NOT NULL COMMENT '类型（1：JAVA，2：DUBBO，4：SPRING，8：MYBATIS）',
    `content`     VARCHAR(255) NOT NULL COMMENT '内容',
    `create_time` DATETIME     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`author_id`) REFERENCES `author` (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

INSERT INTO `author`(`name`, `age`, `sex`, `email`)
VALUES ('coolblog.xyz', 28, 0, 'coolblog.xyz@outlook.com'),
       ('nullllun', 29, 1, 'coolblog.xyz@outlook.com');

INSERT INTO `article`(`author_id`, `title`, `type`, `content`, `create_time`)
VALUES (1, 'Mybatis源码分析系列文章导读', 8, 'Mybatis源码分析系列文章导读', '2018-07-15 15:30:09'),
       (2, 'HashMap源码详细分析（JDK1.8）', 1, 'HashMap源码详细分析（JDK1.8）', '2018-01-18 15:29:13'),
       (1, 'Java CAS 原理分析', 1, 'Java CAS 原理分析', '2018-05-15 15:28:33'),
       (1, 'Spring IOC 容器源码分析-获取单例Bean', 4, 'Spring IOC 容器源码分析-获取单例Bean', '2018-06-01 00:00:00'),
       (1, 'Spring IOC 容器源码分析-循环依赖的解决办法', 4, 'Spring IOC 容器源码分析-循环依赖的解决办法',
        '2018-06-08 00:00:00'),
       (2, 'Spring AOP 源码分析系列文章导读', 4, 'Spring AOP 源码分析系列文章导读', '2018-06-17 00:00:00'),
       (2, 'Spring AOP 源码分析-创建代理对象', 4, 'Spring AOP 源码分析-创建代理对象', '2018-06-20 00:00:00'),
       (1, 'Spring MVC 原理揭秘 - 一个请求的旅行过程', 4, 'Spring MVC 原理揭秘 - 一个请求的旅行过程',
        '2018-06-29 00:00:00'),
       (2, 'Spring MVC 原理揭秘 - 容器的创建过程', 4, 'Spring MVC 原理揭秘 - 容器的创建过程', '2018-06-30 00:00:00'),
       (2, 'Spring IOC 容器源码分析系列文章导读', 4, 'Spring IOC 容器源码分析系列文章导读', '2018-05-30 00:00:00');