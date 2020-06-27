# Fluent-Mybatis介绍

   因为github图片显示的问题，详细介绍请移步 
   [gitee/fluent-mybatis-docs](https://gitee.com/fluent-mybatis/fluent-mybatis-docs)
    
## Fluent-Mybatis能干吗
fluent-mybatis是mybatis的增强版，既有改变，又有增强，简化开发、提高效率。

- 无需xml文件，直接用java所见即所得的写出sql语句
    1. 干掉了生成xml文件步骤，避免了维护xml文件，同时也避免要在xml文件中设置一堆&lt;if test="...">
    2. 避免了dao类里面if else方式的参数设置
    3. 避免参数设置不当，导致全表扫描
    
- 智能化的，语义化的流式接口，让你一气呵成写完sql操作
    1. 流式动态接口，结合IDE的智能提示，最大限度的避免书写错误
    2. 对不可空的参数会自动判断，避免粗心的程序员没有做前置检验导致的错误结果
    3. 支持嵌套查询，99%的单表操作使用fluent-mybatis语法就可以直接完成，无需再自定义mapper操作
``` java

    @DisplayName("嵌套查询：地址包含'杭州滨江'的所有用户列表")
    @Test
    void test_nested_query_address_like() {
        UserQuery query = new UserQuery()
            .where
            .id().in(AddressQuery.class,
                q -> q.select(by -> by.userId().as())
                    .where.address().like("杭州滨江").end())
            .end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT id, address_id, age, gmt_created, gmt_modified, grade, is_deleted, user_name, version " +
                "FROM t_user " +
                "WHERE id IN (SELECT user_id FROM address WHERE address LIKE ?)");
    }
```

    4. 对聚合函数的支持，包括select 聚合函数 和 having 聚合函数判断
``` java

    @DisplayName("按级别grade统计年龄在15和25之间的人数在10人以上，该条件内最大、最小和平均年龄")
    @Test
    public void test_count_gt_10_groupByGrade() throws Exception {
        UserQuery query = new UserQuery()
            .select.grade().as().id().count().age().max().age().min().age().avg().end()
            .where
            .age().between(15, 25).end()
            .groupBy
            .grade().end()
            .having
            .id().count().gt(10).end();
        mapper.listEntity(query);
        db.sqlList().wantFirstSql()
            .eq("SELECT grade, COUNT(id), MAX(age), MIN(age), AVG(age) " +
                "FROM t_user " +
                "WHERE age BETWEEN ? AND ? " +
                "GROUP BY grade " +
                "HAVING COUNT(id) > ?");
    }
```
  
- 增强功能
    1. 可以自动帮忙进行传统的分页操作, 只需要传入一个查询条件, 自动完成count查询
    总数，和limit查询分页列表的操作。并且在查询总数的时候，自动去除了order by的部分，大大简化了分页查询
``` java

    @DisplayName("准备100条数据, 分页查询，一次操作返回总数和符合条件的列表")
    @Test
    public void test_select_paged_list() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(100)
            .id.autoIncrease()
            .user_name.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
        );

        PagedList<UserEntity> list = dao.selectPagedList(new UserQuery()
            .where.
                userName().like("user")
            .end()
            .orderBy.
                id()
            .end()
            .limit(20, 10)
        );
        want.number(list.getTotal()).eq(100);
        List<Integer> ids = list.getData().stream().map(e -> (int) (long) e.getId()).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        db.sqlList().wantSql(0)
            .eq("SELECT COUNT( * ) FROM t_user " +
                "WHERE user_name LIKE ?");
        db.sqlList().wantSql(1).end("FROM t_user " +
            "WHERE user_name LIKE ? " +
            "ORDER BY id " +
            "LIMIT ?, ?");
    }
```
    
    2. 支持按标识进行分页的操作，每次查询会自动多查一条数据作为下一次查询的marker标识
    
``` java
    @DisplayName("准备100条数据，按条件>分页开始标识方式查询，自动获取下一页的标识")
    @Test
    public void test_select_paged_list() throws Exception {
        db.table(t_user).clean().insert(TM.user.createWithInit(100)
            .id.autoIncrease()
            .user_name.formatAutoIncrease("user_%d")
            .age.generate((index) -> new Random().nextInt(100))
        );

        MarkerList<UserEntity> list = dao.selectMarkerList(new UserQuery()
            .where.
                id().gt(20).
                userName().like("user").end()
            .orderBy.
                id().end()
            .limit(10)
        );

        List<Integer> ids = list.getData().stream()
            .map(e -> (int) (long) e.getId()).collect(Collectors.toList());
        want.list(ids).eqReflect(new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30});
        long next = list.parseMarker((UserEntity e) -> e.getId());
        want.number(next).eq(31L);
    }
```
    
    3. 按需查询，可以灵活指定需要查询的数据，减少数据传输
    4. 结合test4j单元测试工具，可以自动化的进行内存数据库方式测试，
    并且无需提供脚本，框架会根据实体类自动生成数据库脚本，真正做到实时随地跑测试。
    可以选择h2,速度快，但有少量语法和mysql不一致；也可以选择mariadb数据库，语法和mysql高度一致;
    当然更可以支持实体数据库，方便查看测试过程中的数据。
    
    ***重要：不论什么数据库，你都无需维护测试数据库的脚本***
    
## 使用fluent-mybatis

   [使用fluent-mybatis](docs/quick_start.md)