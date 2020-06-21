package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;

import java.util.Map;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Column;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoAutoIdWrapperHelper.*;

/**
 * @ClassName NoAutoIdQuery
 * @Description NoAutoIdEntity查询（删除）条件
 *
 * @author generate code
 */
public class NoAutoIdQuery extends BaseQuery<NoAutoIdEntity, NoAutoIdQuery> {
    private final Selector selector = new Selector(this);

    private final QueryGroup groupBy = new QueryGroup(this);

    private final Having having = new Having(this);

    private final QueryOrder orderBy = new QueryOrder(this);

    public final WrapperWhere<NoAutoIdQuery> and = new WrapperWhere<>(this);

    public final WrapperWhere<NoAutoIdQuery> or = new WrapperWhere<>(this, false);

    public NoAutoIdQuery(){
        super(NoAutoIdEntity.class);
    }

    public NoAutoIdQuery(ParameterPair parameters) {
        super(NoAutoIdEntity.class, parameters);
    }

    /**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器NoAutoIdQuery
     */
    public NoAutoIdQuery select(Consumer<Selector> by){
        by.accept(selector);
        return this;
    }
    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param by 设置分组字段
     * @return 查询器NoAutoIdQuery
     */
    public NoAutoIdQuery groupBy(Consumer<QueryGroup> by) {
        by.accept(this.groupBy);
        return this;
    }

    /**
     * having 函数设置
     *
     * @param by having函数设置器
     * @return 查询器NoAutoIdQuery
     */
    public NoAutoIdQuery having(Consumer<Having> by){
        by.accept(having);
        return this;
    }

    /**
    * order by设置
    *
    * @param by 设置排序字段和升降序
    * @return 查询器NoAutoIdQuery
    */
    public NoAutoIdQuery orderBy(Consumer<QueryOrder> by){
        by.accept(this.orderBy);
        return this;
    }

    @Override
    public NoAutoIdQuery eqByNotNull(NoAutoIdEntity entity) {
        super.eqByNotNull(NoAutoIdEntityHelper.column(entity));
        return this;
    }

    @Override
    protected String primaryName(){
        return Column.id;
    }

    @Override
    public Class<NoAutoIdQuery> queryClass() {
        return NoAutoIdQuery.class;
    }

    @Override
    protected Map<String, String> property2Column(){
        return NoAutoIdMP.Property2Column;
    }

    @Override
    protected String table(){
        return NoAutoIdMP.Table_Name;
    }
}