package cn.org.atool.mybatis.fluent.demo.query;

import cn.org.atool.mybatis.fluent.base.IEntityUpdate;
import cn.org.atool.mybatis.fluent.base.IProperty2Column;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import cn.org.atool.mybatis.fluent.demo.entity.AddressEntity;
import cn.org.atool.mybatis.fluent.demo.mapping.AddressMP;

/**
 * @ClassName AddressEntityUpdate
 * @Description AddressEntity更新设置
 *
 * @author generate code
 */
public class AddressEntityUpdate extends AbstractWrapper<AddressEntity, String, AddressEntityUpdate>
    implements IEntityUpdate<AddressEntityUpdate>, IProperty2Column {
    /**
    * SQL 更新字段内容，例如：name='1',age=2
    */
    private final List<String> sqlSet;

    private final Map<String, Object> updates = new HashMap<>();

    public final AddressEntityWrapperHelper.And<AddressEntityUpdate> and = new AddressEntityWrapperHelper.And<>(this);

    public final AddressEntityWrapperHelper.Set set = new AddressEntityWrapperHelper.Set(this);

    public final AddressEntityWrapperHelper.UpdateOrder orderBy = new AddressEntityWrapperHelper.UpdateOrder(this);

    public AddressEntityUpdate(){
        // 如果无参构造函数，请注意实体 NULL 情况 SET 必须有否则 SQL 异常
        this(null);
    }

    public AddressEntityUpdate(AddressEntity entity) {
        super.setEntity(entity);
        super.initNeed();
        this.sqlSet = new ArrayList<>();
    }

    private AddressEntityUpdate(AddressEntity entity, List<String> sqlSet, AtomicInteger paramNameSeq,
        Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
        super.setEntity(entity);
        this.sqlSet = sqlSet;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
    }

    @Override
    public String getSqlSet() {
        if (CollectionUtils.isEmpty(sqlSet)) {
            return null;
        }
        return String.join(StringPool.COMMA, sqlSet);
    }

    @Override
    public Map<String, String> getProperty2Column() {
        return AddressMP.Property2Column;
    }

    @Override
    public Map<String, ? extends Object> getUpdates() {
        return this.updates;
    }

    @Override
    public AddressEntityUpdate set(boolean condition, String column, Object value){
        if(condition){
            this.updates.put(column, value);
        }
        return this;
    }

    @Override
    public AddressEntityUpdate setSql(boolean condition, String sql) {
        if (condition && StringUtils.isNotEmpty(sql)) {
            sqlSet.add(sql);
        }
        return this;
    }

    public LambdaUpdateWrapper<AddressEntity> lambda() {
        throw new RuntimeException("no support!");
    }

    @Override
    protected AddressEntityUpdate instance() {
        return new AddressEntityUpdate(entity, sqlSet, paramNameSeq, paramNameValuePairs, new MergeSegments());
    }


    @Override
    public AddressEntityUpdate limit(int from, int limit){
        super.last(String.format("limit %d, %d", from, limit));
        return this;
    }

    @Override
    public AddressEntityUpdate limit(int limit){
        super.last(String.format("limit %d", limit));
        return this;
    }
}
