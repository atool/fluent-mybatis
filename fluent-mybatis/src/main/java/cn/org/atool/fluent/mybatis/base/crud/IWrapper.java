package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.io.Serializable;

/**
 * IWrapper: 查询和更新都用到的接口
 *
 * @param <E>  对应的实体类
 * @param <W>  最终查询器或更新器
 * @param <NQ> 对应的嵌套查询器
 * @author darui.wu
 */
public interface IWrapper<
    E extends IEntity,
    W extends IWrapper<E, W, NQ>,
    NQ extends IBaseQuery<E, NQ>>
    extends Serializable {
    
    /**
     * 返回where
     *
     * @return
     */
    WhereBase<?, W, NQ> where();

    /**
     * 返回查询器或更新器对应的xml数据
     * 系统方法, 请勿调用
     *
     * @return
     */
    WrapperData getWrapperData();
}