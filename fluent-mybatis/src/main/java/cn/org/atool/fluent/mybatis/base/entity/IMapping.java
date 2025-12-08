package cn.org.atool.fluent.mybatis.base.entity;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.intf.IDataByColumn;
import cn.org.atool.fluent.mybatis.base.intf.IHasDbType;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.UniqueType;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.functions.RefKey;
import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * EntityMapping基类
 *
 * @author darui.wu
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public interface IMapping extends IDefaultGetter, IHasDbType {
    /**
     * 返回数据库表名
     *
     * @param data 数据
     * @return table fragment
     */
    IFragment table(IDataByColumn data);

    /**
     * Mapper class
     *
     * @return mapper class
     */
    Class mapperClass();

    /**
     * 返回不加反义符的表名
     *
     * @return 表名
     */
    String getTableName();

    /**
     * 返回数据库字段映射关系
     *
     * @return Map&lt;String, FieldMapping&gt;
     */
    Map<String, FieldMapping> getColumnMap();

    /**
     * 返回Entity属性映射关系
     *
     * @return Map&lt;String, FieldMapping&gt;
     */
    Map<String, FieldMapping> getFieldsMap();

    /**
     * 返回用 ', ' 连接好的所有字段
     *
     * @return 字段列表片段
     */
    CachedFrag getSelectAll();

    /**
     * 根据Entity属性换取数据库字段名称
     *
     * @param field 属性名称
     * @return 字段名称
     */
    String columnOfField(String field);

    /**
     * 返回特定类型字段
     *
     * @param type 字段类型
     * @return 字段映射
     */
    Optional<FieldMapping> findField(UniqueType type);

    /**
     * 返回实体类对应的所有数据库字段列表
     *
     * @return 数据库字段列表
     */
    List<String> getAllColumns();

    /**
     * 返回所有字段列表
     *
     * @return 所有字段列表
     */
    List<FieldMapping> allFields();

    /**
     * 返回主键字段名称
     * 如果没有主键字段, 则返回null
     *
     * @param nullError 为空时抛出异常
     * @return 主键字段名称
     */
    default String primaryId(boolean nullError) {
        return (String) this.primaryApplier(nullError, f -> f == null ? null : f.column);
    }

    default FieldMapping primaryMapping() {
        return this.findField(UniqueType.PRIMARY_ID).orElse(null);
    }

    /**
     * 返回主键加工对象
     *
     * @param nullError 为空时抛出异常
     * @param applier   根据主键FieldMapping返回对应值
     * @return ignore
     */
    default Object primaryApplier(boolean nullError, Function<FieldMapping, Object> applier) {
        FieldMapping f = this.findField(UniqueType.PRIMARY_ID).orElse(null);
        if (nullError && f == null) {
            throw new FluentMybatisException("the primary not found.");
        } else {
            return applier.apply(f);
        }
    }

    /**
     * 乐观锁字段
     *
     * @return ignore
     */
    default String versionColumn() {
        return this.findField(UniqueType.LOCK_VERSION)
                .map(m -> m.column).orElse(null);
    }

    /**
     * 逻辑删除字段
     *
     * @return ignore
     */
    default String logicDeleteColumn() {
        return this.findField(UniqueType.LOGIC_DELETED)
                .map(c -> c.column).orElse(null);
    }

    /**
     * 逻辑删除字段是否为 Long 型
     *
     * @return ignore
     */
    default boolean longTypeOfLogicDelete() {
        return this.findField(UniqueType.LOGIC_DELETED)
                .map(m -> m.javaType == Long.class)
                .orElse(false);
    }

    /**
     * 返回主键信息
     *
     * @return 主键信息
     */
    TableId tableId();

    /**
     * 返回指定关联关系设置
     *
     * @param refKey 指定关联关系
     * @param <S>    源实体类型
     * @param <R>    关联实体类型
     * @return 关联设置
     */
    default <S extends IEntity, R extends IEntity> RefKey<S, R> findRefKey(String refKey) {
        return (RefKey) this.refKeys().get(refKey);
    }

    /**
     * 返回关联方法定义列表
     *
     * @return ignore
     */
    KeyMap<RefKey> refKeys();
}