package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
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

    default W hint(HintType type, String hint) {
        this.getWrapperData().hint(type, hint);
        return (W) this;
    }

    default W hint(String hint) {
        return this.hint(HintType.Before_All, hint);
    }

    /**
     * 返回字段对应的column映射
     *
     * @param column 数据库字段名称
     * @return 字段映射
     */
    default FieldMapping column(String column) {
        return null;
    }
}