package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.interfaces.IEntityQuery;
import cn.org.atool.fluent.mybatis.condition.interfaces.IProperty2Column;
import cn.org.atool.fluent.mybatis.condition.AbstractWrapper;
import cn.org.atool.fluent.mybatis.condition.SharedString;
import cn.org.atool.fluent.mybatis.condition.segments.MergeSegments;
import cn.org.atool.fluent.mybatis.metadata.FieldInfo;
import cn.org.atool.fluent.mybatis.metadata.TableHelper;
import cn.org.atool.fluent.mybatis.util.Constants;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Column;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressEntityWrapperHelper.And;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressEntityWrapperHelper.QueryOrder;

import static cn.org.atool.fluent.mybatis.util.MybatisUtil.isNotEmpty;

/**
 * @ClassName AddressEntityQuery
 * @Description AddressEntity查询（删除）条件
 *
 * @author generate code
 */
public class AddressEntityQuery extends AbstractWrapper<AddressEntity, String, AddressEntityQuery>
    implements IEntityQuery<AddressEntityQuery, AddressEntity>, IProperty2Column {
    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    public final And<AddressEntityQuery> and = new And<>(this);

    public final QueryOrder orderBy = new QueryOrder(this);

    public AddressEntityQuery(){
        this(null);
    }

    public AddressEntityQuery(AddressEntity entity){
        super.setEntity(entity);
        super.initNeed();
    }

    public AddressEntityQuery(AddressEntity entity, String... columns){
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    private AddressEntityQuery(AddressEntity entity, AtomicInteger paramNameSeq,
        Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
        super.setEntity(entity);
        this.entityClass = AddressEntity.class;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
    }

    @Override
    public AddressEntityQuery select(String... columns) {
        if (isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(Constants.COMMA, columns));
        }
        return this;
    }

    @Override
    public AddressEntityQuery select(Predicate<FieldInfo> predicate) {
        this.entityClass = AddressEntity.class;
        this.sqlSelect.setStringValue(TableHelper.getTableInfo(getCheckEntityClass()).chooseSelect(predicate));
        return this;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }

    /**
     * 只查询主键字段
     *
     * @return
     */
    public AddressEntityQuery selectId(){
        return this.select(Column.id);
    }

    @Override
    public AddressEntityQuery distinct(String... columns){
        if(isNotEmpty(columns)){
            this.sqlSelect.setStringValue(super.distinctSelect(columns));
        }
        return this;
    }

    public AddressEntityQuery distinct(Predicate<FieldInfo> predicate) {
        this.entityClass = AddressEntity.class;
        this.sqlSelect.setStringValue(super.distinctSelect(getCheckEntityClass(), predicate));
        return this;
    }

    public AddressEntityQuery distinct(Class<AddressEntity> entityClass, Predicate<FieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(super.distinctSelect(getCheckEntityClass(), predicate));
        return this;
    }

    /**
    * <p>
    * 用于生成嵌套 sql
    * 故 sqlSelect 不向下传递
    * </p>
    */
    @Override
    protected AddressEntityQuery instance() {
        return new AddressEntityQuery(entity, paramNameSeq, paramNameValuePairs, new MergeSegments());
    }

    @Override
    public Map<String, String> getProperty2Column(){
        return AddressMP.Property2Column;
    }

    @Override
    public AddressEntityQuery limit(int from, int limit){
        super.last(String.format("limit %d, %d", from, limit));
        return this;
    }

    @Override
    public AddressEntityQuery limit(int limit){
        super.last(String.format("limit %d", limit));
        return this;
    }
}