# MySql技能

## mysql数据表分区.
分区语法: 
create table t1(
    id int,
    name varchar(20)
) partition by range(id)(
    partition p0 values less than(100),
    partition p1 values less than(200),
    partition p2 values less than(300),
    partition p3 values less than(400),
    partition p4 values less than(500),
    partition p5 values less than(600),
    partition p6 values less than(700),
    partition p7 values less than(800),
    partition p8 values less than(900),
    partition p9 values less than(1000),
    partition p10 values less than maxvalue
);