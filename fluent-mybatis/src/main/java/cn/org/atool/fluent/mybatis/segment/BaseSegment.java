package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.refs.IRef;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * BaseSegment
 *
 * @param <R>
 * @param <W>
 * @author darui.wu  2020/6/22 10:47 上午
 */
@SuppressWarnings({"unchecked", "rawtypes", "unused"})
public abstract class BaseSegment<R, W extends IWrapper<?, W, ?>> {
    /**
     * 当前查询（更新）器
     */
    @Getter(AccessLevel.PACKAGE)
    protected final BaseWrapper wrapper;
    /**
     * 当前处理字段
     */
    protected FieldMapping current;

    protected BaseSegment(W wrapper) {
        this.wrapper = (BaseWrapper) wrapper;
    }

    /**
     * 对字段column进行操作
     *
     * @param field 字段信息
     * @return BaseSegment子类或者操作器
     */
    public R set(FieldMapping field) {
        this.current = field;
        return this.apply();
    }

    /**
     * 当前字段
     *
     * @return FieldMapping
     */
    public FieldMapping get() {
        return this.current;
    }

    protected abstract R apply();

    /**
     * 结束本段操作，返回查询（更新）器对象
     *
     * @return 查询（更新）器对象
     */
    public W end() {
        return (W) this.wrapper;
    }

    /**
     * 查询条件
     *
     * @return WrapperData
     */
    WrapperData data() {
        return this.wrapper.data();
    }

    /**
     * 根据entity设置where条件
     *
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 所有非空entity字段
     * </pre>
     *
     * @param entity   实例
     * @param consumer 设置条件
     * @param columns  要设置条件的字段
     */
    protected void byEntity(IEntity entity, BiConsumer<String, Object> consumer, boolean allowPk, List<String> columns) {
        assertNotNull("entity", entity);
        Map<String, Object> map = entity.toColumnMap(true);

        String pk = IRef.primaryColumn(entity.entityClass());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();
            /*
             * 跳过主键设置
             * o 更新设置
             * o 查询, 但主键值为null
             */
            if (Objects.equals(pk, column) && (!allowPk || value == null)) {
                continue;
            }
            if (!columns.isEmpty() && !columns.contains(column) ||
                columns.isEmpty() && value == null) {
                continue;
            }
            consumer.accept(column, value);
        }
    }

    protected void byExclude(IEntity entity, BiConsumer<String, Object> consumer, boolean allowPk, List<String> excludes) {
        assertNotNull("entity", entity);
        Map<String, Object> map = entity.toColumnMap(true);
        String pk = IRef.primaryColumn(entity.entityClass());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();
            /*
             * 跳过主键设置
             * o 更新设置
             * o 查询, 但主键值为null
             */
            if (Objects.equals(pk, column) && (!allowPk || value == null)) {
                continue;
            }
            if (!excludes.isEmpty() && excludes.contains(column)) {
                continue;
            }
            consumer.accept(column, entry.getValue());
        }
    }

    /**
     * 查找column对应的字段映射定义
     *
     * @param column 字段
     * @return WrapperData
     */
    protected FieldMapping fieldMapping(String column) {
        return this.wrapper.column(column);
    }
}