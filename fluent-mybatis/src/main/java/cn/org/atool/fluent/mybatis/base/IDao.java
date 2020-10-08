package cn.org.atool.fluent.mybatis.base;

/**
 * dao自定义接口继承类
 * <p>
 * 这里不能复用IMapperDao的原因有二:
 * 1. 若继承时不处理泛型参数, 会导致编译错误
 * 2. 如果处理泛型, 会让自定义变复杂, 同时fluent mybatis编译生成也会变复杂
 * <p>
 * 直接定义一个无泛型的基类, 可以达到
 * 1. 避免泛型问题
 * 2. 自定义接口通过继承IDao也可以使用IMapperDao中定义的基础方法
 * 3. 在实现类中的接口多继承和重载实现(IDao, IMapperDao), 规避了泛型继承的编译问题
 *
 * @author wudarui
 */
public interface IDao {
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
     * 返回主键字段名称
     *
     * @return
     */
    String findPkColumn();
}
