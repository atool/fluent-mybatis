package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;

import java.util.Map;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Column;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressWrapperHelper.*;

/**
 * @ClassName AddressQuery
 * @Description AddressEntity查询（删除）条件
 *
 * @author generate code
 */
public class AddressQuery extends BaseQuery<AddressEntity, AddressQuery> {
    private final Selector selector = new Selector(this);

    private final QueryGroup groupBy = new QueryGroup(this);

    private final Having having = new Having(this);

    private final QueryOrder orderBy = new QueryOrder(this);

    public final WrapperWhere<AddressQuery> and = new WrapperWhere<>(this);

    public final WrapperWhere<AddressQuery> or = new WrapperWhere<>(this, false);

    public AddressQuery(){
        super(AddressEntity.class);
    }

    public AddressQuery(ParameterPair parameters) {
        super(AddressEntity.class, parameters);
    }

    /**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器AddressQuery
     */
    public AddressQuery select(Consumer<Selector> by){
        by.accept(selector);
        return this;
    }
    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param by 设置分组字段
     * @return 查询器AddressQuery
     */
    public AddressQuery groupBy(Consumer<QueryGroup> by) {
        by.accept(this.groupBy);
        return this;
    }

    /**
     * having 函数设置
     *
     * @param by having函数设置器
     * @return 查询器AddressQuery
     */
    public AddressQuery having(Consumer<Having> by){
        by.accept(having);
        return this;
    }

    /**
    * order by设置
    *
    * @param by 设置排序字段和升降序
    * @return 查询器AddressQuery
    */
    public AddressQuery orderBy(Consumer<QueryOrder> by){
        by.accept(this.orderBy);
        return this;
    }

    @Override
    public AddressQuery eqByNotNull(AddressEntity entity) {
        super.eqByNotNull(AddressEntityHelper.column(entity));
        return this;
    }

    @Override
    protected String primaryName(){
        return Column.id;
    }

    @Override
    public Class<AddressQuery> queryClass() {
        return AddressQuery.class;
    }

    @Override
    protected Map<String, String> property2Column(){
        return AddressMP.Property2Column;
    }

    @Override
    protected String table(){
        return AddressMP.Table_Name;
    }
}