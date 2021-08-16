package cn.org.atool.fluent.mybatis.base.dao;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.provider.BaseSqlProvider;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.MapFunction;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * IDaoProtected: 被限制在Dao类中使用的方法, 只允许在子类中调用，不暴露给更高层的Service外部直接访问
 * * <p>
 * * ！！！！！！！！！！！！
 * * 理由：
 * * 不希望在更高层Service中直接构造Query, Update等具体查询条件, 希望对外接口是语义化的
 *
 * @param <E> 实体类
 * @author Created by darui.wu on 2020/6/24.
 */
@SuppressWarnings({"rawtypes", "unused"})
public interface IProtectedDao<E extends IEntity> {
    /**
     * insert into a_table(fields) select fields from b_table;
     *
     * @param fields 要插入的字段
     * @param query  select数据
     * @return 拷贝插入的记录数
     * @see BaseSqlProvider#insertSelect(Map)
     */
    default int insertSelect(String[] fields, IQuery query) {
        return this.mapper().insertSelect(fields, query);
    }

    /**
     * insert into a_table(fields) select fields from b_table;
     *
     * @param fields 要插入的字段
     * @param query  select数据
     * @return 拷贝插入的记录数
     * @see BaseSqlProvider#insertSelect(Map)
     */
    default int insertSelect(FieldMapping[] fields, IQuery query) {
        return this.insertSelect(Stream.of(fields).map(c -> c.column).toArray(String[]::new), query);
    }

    /**
     * 批量执行增删改操作
     *
     * <pre>
     * 传入多个操作时, 需要数据库支持
     * 比如MySql需要在jdbc url链接中附加设置 &allowMultiQueries=true
     * </pre>
     *
     * @param crud 增删改操作
     */
    default void batchCrud(BatchCrud crud) {
        this.mapper().batchCrud(crud);
    }

    /**
     * 根据条件query删除记录
     *
     * @param query 条件
     * @return 删除记录数
     */
    default int deleteBy(IQuery<E> query) {
        return this.mapper().delete(query);
    }

    /**
     * 根据update设置更新记录
     *
     * @param updates 更新条件
     * @return 最后一个更新, 成功记录数
     */
    default int updateBy(IUpdate... updates) {
        return this.mapper().updateBy(updates);
    }

    /**
     * 根据update设置更新记录
     *
     * @param updates 更新条件
     * @return 最后一个更新, 成功记录数
     */
    default int updateBy(Collection<IUpdate> updates) {
        return this.mapper().updateBy(updates.toArray(new IUpdate[0]));
    }

    /**
     * 根据query查询对应实例列表
     *
     * @param query 查询条件
     * @return 实例列表
     */
    default List<E> listEntity(IQuery<E> query) {
        return this.mapper().listEntity(query);
    }

    /**
     * 根据query查询记录列表, 返回Map对象列表
     *
     * @param query 查询条件
     * @return map list
     */
    default List<Map<String, Object>> listMaps(IQuery<E> query) {
        return this.mapper().listMaps(query);
    }

    /**
     * 根据query查询记录列表，并根据function将记录转换成需要的对象
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      POJO实体类型
     * @return POJO list
     */
    default <POJO> List<POJO> listPoJos(IQuery<E> query, MapFunction<POJO> mapFunction) {
        return this.mapper().listPoJos(query, mapFunction);
    }

    /**
     * 根据query查询记录列表, 并将数据结果转换PoJo对象
     * 转换规则是下划线转驼峰
     * 如果不符合这个规则, 请使用方法手动映射{@link #listPoJos(IQuery, MapFunction)}
     *
     * @param clazz  PoJo对象类型
     * @param query  查询条件
     * @param <POJO> PoJo对象类型
     * @return PoJo列表
     */
    default <POJO> List<POJO> listPoJos(Class<POJO> clazz, IQuery<E> query) {
        return this.mapper().listPoJos(clazz, query);
    }

    /**
     * 分页查询实例
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default StdPagedList<E> stdPagedEntity(IQuery<E> query) {
        return this.mapper().stdPagedEntity(query);
    }

    /**
     * 分页查询数据（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default StdPagedList<Map<String, Object>> stdPagedMap(IQuery<E> query) {
        return this.mapper().stdPagedMap(query);
    }

    /**
     * 分页查询数据（结果集为Object对象）
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      Object实体类型
     * @return 分页查询结果
     */
    default <POJO> StdPagedList<POJO> stdPagedPoJo(IQuery<E> query, MapFunction<POJO> mapFunction) {
        return this.mapper().stdPagedPoJo(query, mapFunction);
    }

    /**
     * 分页查询数据（（结果集为PoJo对象, 查询结果按照下划线转驼峰规则）
     *
     * @param clazz  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return 分页查询结果
     */
    default <POJO> StdPagedList<POJO> stdPagedPoJo(Class<POJO> clazz, IQuery<E> query) {
        return this.mapper().stdPagedPoJo(clazz, query);
    }

    /**
     * 按Marker标识分页查询
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default TagPagedList<E> tagPagedEntity(IQuery<E> query) {
        return this.mapper().tagPagedEntity(query);
    }

    /**
     * 按Marker标识分页查询（结果集为Map对象）
     *
     * @param query 查询条件
     * @return 分页查询结果
     */
    default TagPagedList<Map<String, Object>> tagPagedMap(IQuery<E> query) {
        return this.mapper().tagPagedMap(query);
    }

    /**
     * 按Marker标识分页查询（结果集为Object对象）
     *
     * @param query       查询条件
     * @param mapFunction 从Map记录对实体POJO的转换函数
     * @param <POJO>      Object实体类型
     * @return 分页查询结果
     */
    default <POJO> TagPagedList<POJO> tagPagedPoJo(IQuery<E> query, MapFunction<POJO> mapFunction) {
        return this.mapper().tagPagedPoJo(query, mapFunction);
    }

    /**
     * 按Marker标识分页查询（结果集为Object对象）
     *
     * @param clazz  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return 分页查询结果
     */
    default <POJO> TagPagedList<POJO> tagPagedPoJo(Class<POJO> clazz, IQuery<E> query) {
        return this.mapper().tagPagedPoJo(clazz, query);
    }

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    default Optional<E> findOne(IQuery<E> query) {
        E one = this.mapper().findOne(query);
        return Optional.ofNullable(one);
    }

    /**
     * 根据query查询满足条件的第一条记录
     * 当有多条记录符合条件时，只取第一条记录
     *
     * @param query 查询条件
     * @return 满足条件的一条记录
     */
    default Optional<Map<String, Object>> findOneMap(IQuery<E> query) {
        return this.mapper().findOneMap(query);
    }

    /**
     * 根据query查询满足条件的第一条记录，并根据converter从map转换为Object实例
     *
     * @param query       查询条件
     * @param mapFunction 从map转换为Object实例
     * @param <POJO>      Object类型
     * @return Object实例
     */
    default <POJO> Optional<POJO> findOne(IQuery<E> query, MapFunction<POJO> mapFunction) {
        return this.mapper().findOne(query, mapFunction);
    }

    /**
     * 根据query查询满足条件的第一条记录，并转换为Object实例
     *
     * @param clazz  Object类型
     * @param query  查询条件
     * @param <POJO> Object类型
     * @return Object实例
     */
    default <POJO> Optional<POJO> findOne(Class<POJO> clazz, IQuery<E> query) {
        return this.mapper().findOne(clazz, query);
    }

    /**
     * 返回符合条件的记录数
     *
     * @param query 查询条件
     * @return 符合条件的记录数
     */
    default int count(IQuery<E> query) {
        return this.mapper().count(query);
    }

    /**
     * 获取对应entity的BaseMapper
     *
     * @return IRichMapper
     */
    IRichMapper<E> mapper();
}