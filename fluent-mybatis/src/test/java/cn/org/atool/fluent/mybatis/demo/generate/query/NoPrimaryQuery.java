package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;

import java.util.Map;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Column;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryWrapperHelper.*;

/**
 * @ClassName NoPrimaryQuery
 * @Description NoPrimaryEntity查询（删除）条件
 *
 * @author generate code
 */
public class NoPrimaryQuery extends BaseQuery<NoPrimaryEntity, NoPrimaryQuery> {
    private final Selector selector = new Selector(this);

    private final QueryGroup groupBy = new QueryGroup(this);

    private final Having having = new Having(this);

    private final QueryOrder orderBy = new QueryOrder(this);

    public final WrapperWhere<NoPrimaryQuery> and = new WrapperWhere<>(this);

    public final WrapperWhere<NoPrimaryQuery> or = new WrapperWhere<>(this, false);

    public NoPrimaryQuery(){
        super(NoPrimaryEntity.class);
    }

    public NoPrimaryQuery(ParameterPair parameters) {
        super(NoPrimaryEntity.class, parameters);
    }

    /**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器NoPrimaryQuery
     */
    public NoPrimaryQuery select(Consumer<Selector> by){
        by.accept(selector);
        return this;
    }
    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param by 设置分组字段
     * @return 查询器NoPrimaryQuery
     */
    public NoPrimaryQuery groupBy(Consumer<QueryGroup> by) {
        by.accept(this.groupBy);
        return this;
    }

    /**
     * having 函数设置
     *
     * @param by having函数设置器
     * @return 查询器NoPrimaryQuery
     */
    public NoPrimaryQuery having(Consumer<Having> by){
        by.accept(having);
        return this;
    }

    /**
    * order by设置
    *
    * @param by 设置排序字段和升降序
    * @return 查询器NoPrimaryQuery
    */
    public NoPrimaryQuery orderBy(Consumer<QueryOrder> by){
        by.accept(this.orderBy);
        return this;
    }

    @Override
    public NoPrimaryQuery eqByNotNull(NoPrimaryEntity entity) {
        super.eqByNotNull(NoPrimaryEntityHelper.column(entity));
        return this;
    }


    @Override
    public Class<NoPrimaryQuery> queryClass() {
        return NoPrimaryQuery.class;
    }

    @Override
    protected Map<String, String> property2Column(){
        return NoPrimaryMP.Property2Column;
    }

    @Override
    protected String table(){
        return NoPrimaryMP.Table_Name;
    }
}