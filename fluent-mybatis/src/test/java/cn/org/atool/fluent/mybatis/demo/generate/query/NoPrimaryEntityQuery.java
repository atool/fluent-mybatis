package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.base.IEntityQuery;
import cn.org.atool.fluent.mybatis.base.IProperty2Column;
import cn.org.atool.fluent.mybatis.util.MybatisUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Column;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryEntityWrapperHelper.And;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryEntityWrapperHelper.QueryOrder;

/**
 * @ClassName NoPrimaryEntityQuery
 * @Description NoPrimaryEntity查询（删除）条件
 *
 * @author generate code
 */
public class NoPrimaryEntityQuery extends AbstractWrapper<NoPrimaryEntity, String, NoPrimaryEntityQuery>
    implements IEntityQuery<NoPrimaryEntityQuery, NoPrimaryEntity>, IProperty2Column {
    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    public final And<NoPrimaryEntityQuery> and = new And<>(this);

    public final QueryOrder orderBy = new QueryOrder(this);

    public NoPrimaryEntityQuery(){
        this(null);
    }

    public NoPrimaryEntityQuery(NoPrimaryEntity entity){
        super.setEntity(entity);
        super.initNeed();
    }

    public NoPrimaryEntityQuery(NoPrimaryEntity entity, String... columns){
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    private NoPrimaryEntityQuery(NoPrimaryEntity entity, AtomicInteger paramNameSeq,
        Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
        super.setEntity(entity);
        this.entityClass = NoPrimaryEntity.class;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
    }

    @Override
    public NoPrimaryEntityQuery select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(StringPool.COMMA, columns));
        }
        return this;
    }

    @Override
    public NoPrimaryEntityQuery select(Predicate<TableFieldInfo> predicate) {
        this.entityClass = NoPrimaryEntity.class;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getCheckEntityClass()).chooseSelect(predicate));
        return this;
    }

    @Override
    public NoPrimaryEntityQuery select(Class<NoPrimaryEntity> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getCheckEntityClass()).chooseSelect(predicate));
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
    public NoPrimaryEntityQuery selectId(){
        throw new RuntimeException("table primary undefined!");
    }

    @Override
    public NoPrimaryEntityQuery distinct(String... columns){
        if(ArrayUtils.isNotEmpty(columns)){
            this.sqlSelect.setStringValue(MybatisUtil.distinct(columns));
        }
        return this;
    }

    public NoPrimaryEntityQuery distinct(Predicate<TableFieldInfo> predicate) {
        this.entityClass = NoPrimaryEntity.class;
        this.sqlSelect.setStringValue(MybatisUtil.distinct(getCheckEntityClass(), predicate));
        return this;
    }

    public NoPrimaryEntityQuery distinct(Class<NoPrimaryEntity> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(MybatisUtil.distinct(getCheckEntityClass(), predicate));
        return this;
    }


    /**
     * 暂不支持
     */
    public LambdaQueryWrapper<NoPrimaryEntity> lambda() {
        throw new RuntimeException("no support!");
    }

    /**
    * <p>
    * 用于生成嵌套 sql
    * 故 sqlSelect 不向下传递
    * </p>
    */
    @Override
    protected NoPrimaryEntityQuery instance() {
        return new NoPrimaryEntityQuery(entity, paramNameSeq, paramNameValuePairs, new MergeSegments());
    }

    @Override
    public Map<String, String> getProperty2Column(){
        return NoPrimaryMP.Property2Column;
    }

    @Override
    public NoPrimaryEntityQuery limit(int from, int limit){
        super.last(String.format("limit %d, %d", from, limit));
        return this;
    }

    @Override
    public NoPrimaryEntityQuery limit(int limit){
        super.last(String.format("limit %d", limit));
        return this;
    }
}