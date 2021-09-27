package cn.org.atool.fluent.mybatis.spring;

/**
 * 类型转换
 *
 * @param <T> 目标对象类型
 * @author darui.wu
 */
public interface IConvertor<T> {
    T get(Object obj);
}