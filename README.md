# Fluent-Mybatis介绍
- 微信交流群
![-w330](image/wechat.png)

[FluentMybatis文档](https://gitee.com/fluent-mybatis/fluent-mybatis-docs)
    
[掘金系列文章](https://juejin.im/user/1811586730696142/posts)

## FluentMybatis特性
![-w930](image/fluent-mybatis-feature.png)

## FluentMybatis原理
![-w930](image/fluent-mybatis-driver.png)

## 运行测试
`fluent-mybatis-test`测试需要本地提前运行MySQL并导入测试表，可使用Docker镜像快速部署测试库

## Fluent-Mybatis 历程
- 1.4.3 版本, 支持java 9+编译生成代码
- 1.4.2 版本, 增加一对一, 一对多和多对多支持
- 1.4.1 版本, 特性升级, 增加join, left join, right join连表查询; 支持自定义默认查询条件，更新条件。
- 1.3.x 版本, 重大改版, 语法和1.2.x一样， 但不再生成大量的java类，只需要一个Entity，借助java的processor功能，就实现了强大的fluent形式的增删改查操作。
- 1.2.x 版本, 在使用了一段时间后, 对fluent mybatis语法进行了比较大的调整, 语法结构已经稳定。但需要借助代码生成器，生成一系列文件，给人的感觉入手有点繁琐。
- 1.0.x 版本, 借鉴了mybatis plus和tk mybatis的思想, 但基于Fluent API方式进行了重新开发。
