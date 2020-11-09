package cn.org.atool.fluent.mybatis.base;

/**
 * IRich: Entity类@RefMethod方法实现定义
 *
 * @author wudarui
 */
public interface IRich {
    /**
     * Entity rich @RefMethod method方法定义接口
     * <p>
     * 具体实现 {@link cn.org.atool.fluent.mybatis.base.RichEntity#invoke(boolean, String)}
     * 对应调用 {@link cn.org.atool.fluent.mybatis.base.EntityRefQuery#invoke(Class, String, Object[])}
     *
     * @param cache  结果是否缓存
     * @param method rich method方法名称
     * @param <T>
     * @return
     */
    <T> T invoke(boolean cache, String method);
}