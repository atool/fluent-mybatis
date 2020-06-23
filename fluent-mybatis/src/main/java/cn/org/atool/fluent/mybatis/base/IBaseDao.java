package cn.org.atool.fluent.mybatis.base;

/**
 * IMapperDao
 *
 * @author:darui.wu Created by darui.wu on 2019/10/29.
 */
public interface IBaseDao<E extends IEntity, Q extends IQuery<E, Q>, U extends IUpdate<E, U, Q>> {
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
    Q query();

    /**
     * 构造空白更新条件
     *
     * @return
     */
    U updater();

    /**
     * 返回主键字段名称
     *
     * @return
     */
    String findPkColumn();
}