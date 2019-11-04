package cn.org.atool.mbplus.base;

import cn.org.atool.mbplus.mapper.IEntityMapper;

/**
 * @Descriotion:
 * @param:
 * @return:
 * @author:darui.wu Created by darui.wu on 2019/10/29.
 */
public interface IMapperDao<E extends IEntity, Q extends IEntityQuery<Q, E>, U extends IEntityUpdate<U>> {
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
    U update();

    /**
     * 返回主键字段名称
     *
     * @return
     */
    String findPkColumn();
}