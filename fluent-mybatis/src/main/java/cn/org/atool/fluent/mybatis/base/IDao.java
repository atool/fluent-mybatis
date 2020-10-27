package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

/**
 * dao自定义接口继承类, 定义同 IMapperDao
 * 自定义接口泛型参数需要严格按照&lt;E,Q,U>顺序定义
 * 否则会有编译错误或运行时类型转换错误
 *
 * @param <E> 表示Entity类
 * @param <Q> 表示对应的查询器
 * @param <U> 表示对应的更新器
 * @author wudarui
 */
public interface IDao<E, Q, U> {
    /**
     * 获取对应entity的BaseMapper
     *
     * @return
     */
    IEntityMapper mapper();

    /**
     * 构造空白查询条件
     *
     * @return
     */
    IQuery query();

    /**
     * 构造空白更新条件
     *
     * @return
     */
    IUpdate updater();

    /**
     * 返回主键字段
     *
     * @return
     */
    default FieldMapping primaryField() {
        throw new RuntimeException("not implement.");
    }
}