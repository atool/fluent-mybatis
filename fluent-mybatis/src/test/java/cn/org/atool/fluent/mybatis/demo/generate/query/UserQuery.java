package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;

import java.util.Map;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP.Column;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserEntityHelper;
import cn.org.atool.fluent.mybatis.demo.generate.query.UserWrapperHelper.*;

/**
 * @ClassName UserQuery
 * @Description UserEntity查询（删除）条件
 *
 * @author generate code
 */
public class UserQuery extends BaseQuery<UserEntity, UserQuery> {
    private final Selector selector = new Selector(this);

    private final QueryGroup groupBy = new QueryGroup(this);

    private final Having having = new Having(this);

    private final QueryOrder orderBy = new QueryOrder(this);

    public final WrapperWhere<UserQuery> and = new WrapperWhere<>(this);

    public final WrapperWhere<UserQuery> or = new WrapperWhere<>(this, false);

    public UserQuery(){
        super(UserEntity.class);
    }

    public UserQuery(ParameterPair parameters) {
        super(UserEntity.class, parameters);
    }

    /**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器UserQuery
     */
    public UserQuery select(Consumer<Selector> by){
        by.accept(selector);
        return this;
    }
    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param by 设置分组字段
     * @return 查询器UserQuery
     */
    public UserQuery groupBy(Consumer<QueryGroup> by) {
        by.accept(this.groupBy);
        return this;
    }

    /**
     * having 函数设置
     *
     * @param by having函数设置器
     * @return 查询器UserQuery
     */
    public UserQuery having(Consumer<Having> by){
        by.accept(having);
        return this;
    }

    /**
    * order by设置
    *
    * @param by 设置排序字段和升降序
    * @return 查询器UserQuery
    */
    public UserQuery orderBy(Consumer<QueryOrder> by){
        by.accept(this.orderBy);
        return this;
    }

    @Override
    public UserQuery eqByNotNull(UserEntity entity) {
        super.eqByNotNull(UserEntityHelper.column(entity));
        return this;
    }

    @Override
    protected String primaryName(){
        return Column.id;
    }

    @Override
    public Class<UserQuery> queryClass() {
        return UserQuery.class;
    }

    @Override
    protected Map<String, String> property2Column(){
        return UserMP.Property2Column;
    }

    @Override
    protected String table(){
        return UserMP.Table_Name;
    }
}