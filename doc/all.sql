drop table if exists `test`;
create table `test` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    `password` varchar(50) comment '密码',
    primary key (`id`)
)engine=innodb default charset=utf8mb4 comment='测试';

drop table if exists `demo`;
create table `demo` (
                        `id` bigint not null comment 'id',
                        `name` varchar(50) comment '名称',
                        primary key (`id`)
)engine=innodb default charset=utf8mb4 comment='测试';

drop table if exists `ebook`;
create table `ebook` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    `category1_id` bigint comment '分类一',
    `category2_id` bigint comment '分类二',
    `description` varchar(200) comment '描述',
    `cover` varchar(200) comment '封面',
    `doc_count` int comment '文档数',
    `view_count` int comment '阅读数',
    `vote_count` int comment '点赞数',
    primary key (`id`)
)engine=innodb default charset=utf8mb4 comment='电子书';

insert into `ebook`(`id`,`name`,`description`) values (1,'springboot','开发技术栈');

select * from ebook;

#分类表
drop table if exists `category`;
create table  `category`(
    `id` bigint not null comment 'id',
    `parent` bigint not null default 0 comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int comment '顺序',
    primary key (`id`)
)engine=innodb default charset=utf8mb4 comment='分类';

insert into `category` (`id`,`parent`,`name`,`sort`) values (100,000,'前段开发',100),
                                                            (101,100,'VUE',101),
                                                            (102,100,'HTML & CSS',102),
                                                            (200,000,'JAVA',200),
                                                            (201,200,'基础应用',201),
                                                            (202,200,'框架应用',202),
                                                            (300,000,'Python',300),
                                                            (301,300,'基础应用',301),
                                                            (302,300,'进阶方向应用',302),
                                                            (400,000,'数据库',400),
                                                            (401,400,'MYSQL',401),
                                                            (500,000,'其他',500),
                                                            (501,500,'服务器',501),
                                                            (502,500,'开发者工具',502),
                                                            (503,500,'其他',503);

select  * from category;

#文档表
drop table if exists `doc`;
create table `doc`(
    `id` bigint not null comment 'id',
    `ebook_id` bigint not null default 0 comment '电子书id',
    `parent` bigint not null default  0 comment '父id',
    `name` varchar(50) not null comment '名称',
    `sort` int comment '顺序',
    `view_count` int default 0 comment '阅读数',
    `vote_count` int default 0 comment '点赞数',
    primary key (`id`)
)engine=innodb default charset=utf8mb4 comment='文档';

insert into `doc`(`id` , `ebook_id` , `parent` , `name` , `sort` , `view_count` , `vote_count`) values (1,1,0,'文档1',1,0,0),
                                                                                                       (2,1,1,'文档1.1',1,0,0),
                                                                                                       (3,1,0,'文档2',2,0,0),
                                                                                                       (4,1,3,'文档2.1',1,0,0),
                                                                                                       (5,1,3,'文档2.2',2,0,0),
                                                                                                       (6,1,5,'文档2.2.1',1,0,0);
