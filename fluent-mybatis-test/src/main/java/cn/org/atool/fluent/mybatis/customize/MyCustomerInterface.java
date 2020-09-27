package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IMapperDao;
import cn.org.atool.fluent.mybatis.base.IQuery;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 *
 * @param <E>
 */
public interface MyCustomerInterface<E extends IEntity> extends IMapperDao<E> {
    default IQuery defaultQuery() {
        return this.query()
            .where().apply("is_deleted", EQ, false)
            .end();
    }
}