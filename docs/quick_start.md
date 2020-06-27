## 开始第一个例子
### 新建Java工程，设置maven依赖
    并且设置项目编译级别为Java8及以上
    
```xml
<dependencies>
    <!-- fluent-mybatis项目运行依赖, scope为compile -->
    <dependency>
        <groupId>com.github.atool</groupId>
        <artifactId>fluent-mybatis</artifactId>
        <version>1.2.0</version>
    </dependency>

    <!-- fluent-mybatis代码生成依赖, 应用运行无需依赖, scope设为test-->
    <dependency>
        <groupId>com.github.atool</groupId>
        <artifactId>fluent-mybatis-generator</artifactId>
        <version>1.2.0</version>
        <scope>test</scope>
    </dependency>

    <!-- 其它必要依赖, 比如spring, lombok, mysql, 数据库连接池等-->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.6</version>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-dbcp2</artifactId>
        <version>2.5.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>4.3.14.RELEASE</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>4.3.14.RELEASE</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### 新建演示用的数据库结构
```mysql
create schema fluent_mybatis_tutorial;
use fluent_mybatis_tutorial;

drop table if exists user;
CREATE TABLE user  (
  id bigint(21) unsigned auto_increment primary key COMMENT '主键id',
  avatar varchar(255) DEFAULT NULL COMMENT '头像',
  account varchar(45) DEFAULT NULL COMMENT '账号',
  password varchar(45) DEFAULT NULL COMMENT '密码',
  user_name varchar(45) DEFAULT NULL COMMENT '名字',
  birthday datetime DEFAULT NULL COMMENT '生日',
  e_mail varchar(45) DEFAULT NULL COMMENT '电子邮件',
  phone varchar(20) DEFAULT NULL COMMENT '电话',
  bonus_points bigint(21) DEFAULT 0 COMMENT '会员积分',
  status varchar(32) DEFAULT NULL COMMENT '状态(字典)',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '更新时间',
  is_deleted tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户表';

drop table  if exists receiving_address;
CREATE TABLE receiving_address  (
  id bigint(21) unsigned auto_increment primary key COMMENT '主键id',
  user_id bigint(21) NOT NULL COMMENT '用户id',
  province varchar(50) DEFAULT NULL COMMENT '省份',
  city varchar(50) DEFAULT NULL COMMENT '城市',
  district varchar(50) DEFAULT NULL COMMENT '区',
  detail_address varchar(100) DEFAULT NULL COMMENT '详细住址',
  gmt_create datetime DEFAULT NULL COMMENT '创建时间',
  gmt_modified datetime DEFAULT NULL COMMENT '更新时间',
  is_deleted tinyint(2) DEFAULT 0 COMMENT '是否逻辑删除'
) ENGINE = InnoDB CHARACTER SET = utf8 COMMENT = '用户收货地址';
```
    
### 根据表结构生成fluent-mybatis基础代码
``` java

public class TutorialGeneratorMain {
    /**
     * 需要生成代码的数据库链接, 这里为演示方便, 设置为本机数据库
     */
    static String url = "jdbc:mysql://localhost:3306/fluent_mybatis_tutorial?useUnicode=true&characterEncoding=utf8";

    static String srcDir = System.getProperty("user.dir");
    /**
     * mapper, query, update, baseDao等文件生成路径
     * 一般建议放在应用共享二方包下面，应用依赖 scope设置为 compile
     */
    static String mapperJavaDir = srcDir + "/01-mapper-shared/src/main/java";
    /**
     * 为方便测试, 生成的测试辅助工具类, 如果你没有测试习惯, 可以不用生成
     * 一般建议放在单独的共享包下面，应用依赖时 scope设置为 test
     */
    static String testJavaDir = srcDir + "/02-test-shared/src/main/java";
    /**
     * 各个表对应的Dao类, DaoImpl空实现类, 放在应用下
     * 本部分代码没有实际实现，仅仅是减少手工创建这些类的工作量
     */
    static String daoJavaDir = srcDir + "/03-hello-world/src/main/java";

    public static void main(String[] args) {
        MybatisGenerator.build()
            .globalConfig(config -> config
                /**
                 * 设置代码生成路径
                 */
                .setOutputDir(mapperJavaDir, testJavaDir, daoJavaDir)
                /**
                 * 设置数据库连接属性
                 */
                .setDataSource(url, "root", "password")
                /**
                 * 设置生成的代码基础package路径
                 */
                .setBasePackage("cn.org.atool.fluent.mybatis.tutorial"))
            .tables(config -> config
                /**
                 * 设置要生成代码的表, 这里是上面sql脚本创建的2张表
                 */
                .table("user")
                .table("receiving_address")
                .foreach(t -> t
                    /**
                     * 设置表通用字段, gmt_create是记录创建时间字段, gmt_modified是记录最后修改字段, is_deleted是表逻辑删除标识字段
                     * 这里按照一般规范, 设置了这3个字段,如果你的表没有这些约定, 也可以不用设置
                     */
                    .setColumn("gmt_created", "gmt_modified", "is_deleted")
                )
            )
            /**
             * 执行代码生成动作
             */
            .execute();
    }
}
```
稍等片刻，基础脚手架代码就生成完毕

### 开始运行第一个例子
#### 定义spring相关配置
```java
@ComponentScan(basePackages = "cn.org.atool.fluent.mybatis.tutorial")
@MapperScan("cn.org.atool.fluent.mybatis.tutorial.mapper")
@Configuration
public class DataSourceConfig {
    /**
     * 设置dataSource属性
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/fluent_mybatis_tutorial?useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;
    }

    /**
     * 设置FluentMybatisSessionFactoryBean bean
     * 替换掉Mybatis提供的 SqlSessionFactoryBean
     *
     * @param dataSource
     * @return
     */
    @Bean
    public FluentMybatisSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        FluentMybatisSessionFactoryBean bean = new FluentMybatisSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean;
    }
}
```

使用Main函数启动测试
```java
public class SpringMainDemo {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(DataSourceConfig.class);
        ctx.refresh();

        UserDao userDao = ctx.getBean(UserDao.class);
        userDao.save(new UserEntity().setId(1L).setAccount("account"));
        UserEntity user = userDao.selectById(1L);
        System.out.print(user.toMap());
        ctx.close();
    }
}
```
控制台输出
```text
{bonusPoints=0, gmtModified=Fri Jun 26 22:23:35 CST 2020, isDeleted=false, id=1, account=account}
```
第一个例子运行成功

#### 换种姿势验证，使用test4j进行测试
首先配置test4j.properties文件

```properties
db.dataSource.list=dataSource
db.dataSource.script.factory=cn.org.atool.fluent.mybatis.tutorial.DataSourceScript
db.dataSource.type=mysql
db.dataSource.driver=com.mysql.jdbc.Driver
db.dataSource.url=jdbc:mysql://localhost:3306/fluent_mybatis_tutorial?characterEncoding=utf8
db.dataSource.userName=root
db.dataSource.password=password
db.dataSource.schemaName=fluent_mybatis_tutorial
```

编写测试代码
```java
@ContextConfiguration(classes = {DataSourceConfig.class})
public class QuickStartTest extends Test4J {
    @Autowired
    private UserDao userDao;

    @Test
    void test_quick_start() {
        db.table(t_user).clean();
        /**
         * 插入一条记录
         */
        userDao.save(new UserEntity().setId(1L).setAccount("account"));
        /**
         * 获取刚插入的记录
         */
        UserEntity user = userDao.selectById(1L);
        /**
         * 对查询结果进行验证
         */
        want.object(user).eqDataMap(EM.user.create(1)
            .id.values(1)
            .account.values("account"));
    }
}
```
如果你执行通过, 恭喜你通过第一关

[具体代码](https://gitee.com/fluent-mybatis/fluent-mybatis-tutorial/tree/master/03-quick-start)

下面我们对生成的脚手架代码进行介绍