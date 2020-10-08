package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.IDao;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.EQ;

/**
 * base dao 自定义接口
 */
public interface MyCustomerInterface extends IDao {

    default IQuery defaultQuery() {
        return (IQuery) this.query()
            .where().apply("is_deleted", EQ, false)
            .end();
    }
}