package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.crud.IBaseUpdate;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.GetterFunc;
import cn.org.atool.fluent.mybatis.utility.MappingKits;

import java.util.Arrays;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

/**
 * BaseSetter: 更新设置操作
 *
 * @param <S> 更新器
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class UpdateBase<
    S extends UpdateBase<S, U>,
    U extends IBaseUpdate<?, U, ?>
    >
    extends BaseSegment<UpdateApply<S, U>, U> {

    private final UpdateApply<S, U> apply = new UpdateApply<>((S) this);

    public final S set = (S) this;

    protected UpdateBase(U updater) {
        super(updater);
    }

    /**
     * 按照values中非null值更新记录
     *
     * @param values key-value条件
     * @return self
     */
    public S byNotNull(Map<String, Object> values) {
        if (values != null) {
            values.forEach((column, value) -> {
                Column _column = Column.column(column, this.wrapper);
                this.wrapperData().updateSet(_column, value);
            });
        }
        return (S) this;
    }

    /**
     * 根据entity值更新
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 除主键外的非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param columns 要更新的字段
     * @return self
     */
    public S byEntity(IEntity entity, FieldMapping column, FieldMapping... columns) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns(column, columns);
        return this.byEntity(entity, arr);
    }

    /**
     * 根据entity值更新
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 除主键外的非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param getters 要更新的字段, Entity::getter函数
     * @return self
     */
    public <E extends IEntity> S byEntity(E entity, GetterFunc<E> getter, GetterFunc<E>... getters) {
        assertNotNull("entity", entity);
        Class klass = IRefs.instance().findFluentEntityClass(entity.getClass());
        String[] arr = MappingKits.toColumns(klass, getter, getters);
        return this.byEntity(entity, arr);
    }


    /**
     * 根据entity值更新
     * <pre>
     * o 指定字段列表, 可以是 null 值
     * o 无指定字段时, 除主键外的非空entity字段
     * </pre>
     *
     * @param entity  实例
     * @param columns 要更新的字段
     * @return self
     */
    public S byEntity(IEntity entity, String... columns) {
        super.byEntity(entity, (column, value) -> {
            Column _column = Column.column(column, this.wrapper);
            this.wrapperData().updateSet(_column, value);
        }, false, Arrays.asList(columns));
        return (S) this;
    }

    /**
     * 根据entity字段(包括null字段), 但排除指定字段
     *
     * @param entity   实例
     * @param excludes 排除更新的字段
     * @return self
     */
    public S byExclude(IEntity entity, FieldMapping exclude, FieldMapping... excludes) {
        assertNotNull("entity", entity);
        String[] arr = MappingKits.toColumns(exclude, excludes);
        return this.byExclude(entity, arr);
    }

    /**
     * 根据entity字段(包括null字段), 但排除指定字段
     *
     * @param entity   实例
     * @param excludes 排除更新的字段, Entity::getter函数
     * @return self
     */
    public <E extends IEntity> S byExclude(E entity, GetterFunc<E> exclude, GetterFunc<E>... excludes) {
        assertNotNull("entity", entity);
        Class klass = IRefs.instance().findFluentEntityClass(entity.getClass());
        String[] arr = MappingKits.toColumns(klass, exclude, excludes);
        return this.byExclude(entity, arr);
    }

    /**
     * 更新除指定的排除字段外其它entity字段值(包括null字段)
     * <p>
     * 无排除字段时, 更新除主键外其它字段(包括null值字段)
     *
     * @param entity   实例
     * @param excludes 排除更新的字段
     * @return self
     */
    public S byExclude(IEntity entity, String... excludes) {
        super.byExclude(entity, (column, value) -> {
            Column _column = Column.column(column, this.wrapper);
            this.wrapperData().updateSet(_column, value);
        }, false, Arrays.asList(excludes));
        return (S) this;
    }

    @Override
    protected UpdateApply<S, U> apply() {
        return apply;
    }
}