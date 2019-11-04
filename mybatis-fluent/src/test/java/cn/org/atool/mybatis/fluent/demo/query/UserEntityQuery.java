package cn.org.atool.mybatis.fluent.demo.query;

import cn.org.atool.mybatis.fluent.base.IEntityQuery;
import cn.org.atool.mybatis.fluent.base.IProperty2Column;
import cn.org.atool.mybatis.fluent.util.MybatisUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import cn.org.atool.mybatis.fluent.demo.entity.UserEntity;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP.Column;

/**
 * @ClassName UserEntityQuery
 * @Description UserEntity查询（删除）条件
 *
 * @author generate code
 */
public class UserEntityQuery extends AbstractWrapper<UserEntity, String, UserEntityQuery>
    implements IEntityQuery<UserEntityQuery, UserEntity>, IProperty2Column {
    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    public final UserEntityWrapperHelper.And<UserEntityQuery> and = new UserEntityWrapperHelper.And<>(this);

    public final UserEntityWrapperHelper.QueryOrder orderBy = new UserEntityWrapperHelper.QueryOrder(this);

    public UserEntityQuery(){
        this(null);
    }

    public UserEntityQuery(UserEntity entity){
        super.setEntity(entity);
        super.initNeed();
    }

    public UserEntityQuery(UserEntity entity, String... columns){
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     */
    private UserEntityQuery(UserEntity entity, AtomicInteger paramNameSeq,
        Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
        super.setEntity(entity);
        this.entityClass = UserEntity.class;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
    }

    @Override
    public UserEntityQuery select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(StringPool.COMMA, columns));
        }
        return this;
    }

    @Override
    public UserEntityQuery select(Predicate<TableFieldInfo> predicate) {
        this.entityClass = UserEntity.class;
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getCheckEntityClass()).chooseSelect(predicate));
        return this;
    }

    @Override
    public UserEntityQuery select(Class<UserEntity> entityClass, Predicate<TableFieldInfo> predicate) {
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
    public UserEntityQuery selectId(){
        return this.select(Column.id);
    }

    @Override
    public UserEntityQuery distinct(String... columns){
        if(ArrayUtils.isNotEmpty(columns)){
            this.sqlSelect.setStringValue(MybatisUtil.distinct(columns));
        }
        return this;
    }

    public UserEntityQuery distinct(Predicate<TableFieldInfo> predicate) {
        this.entityClass = UserEntity.class;
        this.sqlSelect.setStringValue(MybatisUtil.distinct(getCheckEntityClass(), predicate));
        return this;
    }

    public UserEntityQuery distinct(Class<UserEntity> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect.setStringValue(MybatisUtil.distinct(getCheckEntityClass(), predicate));
        return this;
    }


    /**
     * 暂不支持
     */
    public LambdaQueryWrapper<UserEntity> lambda() {
        throw new RuntimeException("no support!");
    }

    /**
    * <p>
    * 用于生成嵌套 sql
    * 故 sqlSelect 不向下传递
    * </p>
    */
    @Override
    protected UserEntityQuery instance() {
        return new UserEntityQuery(entity, paramNameSeq, paramNameValuePairs, new MergeSegments());
    }

    @Override
    public Map<String, String> getProperty2Column(){
        return UserMP.Property2Column;
    }

    @Override
    public UserEntityQuery limit(int from, int limit){
        super.last(String.format("limit %d, %d", from, limit));
        return this;
    }

    @Override
    public UserEntityQuery limit(int limit){
        super.last(String.format("limit %d", limit));
        return this;
    }
}