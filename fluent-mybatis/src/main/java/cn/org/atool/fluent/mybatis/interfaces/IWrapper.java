package cn.org.atool.fluent.mybatis.interfaces;

import cn.org.atool.fluent.mybatis.condition.WhereBase;
import cn.org.atool.fluent.mybatis.condition.model.WrapperData;

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
    NQ extends IQuery<E, NQ>
    >
    extends Serializable {

    /**
     * 追加在sql语句的末尾
     * !!!慎用!!!
     * 有sql注入风险
     *
     * @param lastSql
     * @return
     */
    W last(String lastSql);

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