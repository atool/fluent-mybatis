package cn.org.atool.fluent.mybatis.method.partition;

import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.method.metadata.TableFieldMeta;
import cn.org.atool.fluent.mybatis.method.metadata.TableMeta;
import cn.org.atool.fluent.mybatis.method.model.SqlBuilder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.SqlProviderUtils.*;
import static java.util.stream.Collectors.joining;

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