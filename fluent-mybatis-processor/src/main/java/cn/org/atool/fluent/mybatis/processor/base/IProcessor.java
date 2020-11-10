package cn.org.atool.fluent.mybatis.processor.base;

import javax.annotation.processing.Messager;

/**
 * 编译器相关类
 *
 * @author darui.wu
 */
public interface IProcessor {
    /**
     * 返回Messager
     *
     * @return
     */
    Messager getMessager();
}