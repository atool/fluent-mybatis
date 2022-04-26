package cn.org.atool.fluent.processor.mybatis.base;

import javax.annotation.processing.Messager;

/**
 * 编译器相关类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused"})
public interface IProcessor {
    /**
     * 返回Messager
     *
     * @return Messager
     */
    Messager getMessager();
}