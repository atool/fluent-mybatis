package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import lombok.Getter;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * AbstractMethod 抽象方法
 *
 * @author darui.wu
 * @create 2020/5/26 11:25 上午
 */
public abstract class AbstractMethod implements InjectMethod {
    @Getter
    private DbType dbType;

    protected AbstractMethod(DbType dbType) {
        assertNotNull("dbType", dbType);
        this.dbType = dbType;
    }
}