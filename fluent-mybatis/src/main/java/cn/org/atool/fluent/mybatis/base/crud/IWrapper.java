package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.model.HintType;
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
@SuppressWarnings({"unchecked"})
public interface IWrapper<
    E extends IEntity,
    W extends IWrapper<E, W, NQ>,
    NQ extends IBaseQuery<E, NQ>>
    extends Serializable {

    /**
     * 返回where
     *
     * @return WhereBase
     */
    WhereBase<?, W, NQ> where();

    /**
     * 返回查询器或更新器对应的xml数据
     * 系统方法, 请勿调用
     *
     * @return WrapperData
     */
    WrapperData getWrapperData();

    /**
     * 在select或update指定位置插入hint语句
     *
     * @param type 指定位置
     * @param hint hint语句
     * @return W
     */
    default W hint(HintType type, String hint) {
        this.getWrapperData().hint(type, hint);
        return (W) this;
    }

    /**
     * 在select或update开头插入hint语句
     *
     * @param hint hint语句
     * @return W
     */
    default W hint(String hint) {
        return this.hint(HintType.Before_All, hint);
    }
}