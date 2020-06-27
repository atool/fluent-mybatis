package cn.org.atool.fluent.mybatis.base;

/**
 * IMapperDao: 构造mapper, query, update对象
 *
 * @param <E> 实体类
 * @author:darui.wu Created by darui.wu on 2019/10/29.
 */
public interface IMapperDao<E extends IEntity> {
    /**
     * 获取对应entity的BaseMapper
     *
     * @return
     */
    IEntityMapper<E> mapper();

    /**
     * 构造空白查询条件
     *
     * @return
     */
    <Q extends IQuery<?, Q>> Q query();

    /**
     * 构造空白更新条件
     *
     * @return
     */
    <U extends IUpdate<?, U, ?>> U updater();

    /**
     * 返回主键字段名称
     *
     * @return
     */
    String findPkColumn();
}