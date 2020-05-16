package cn.org.atool.fluent.mybatis.function;

/**
 * Executor
 *
 * @author:darui.wu Created by darui.wu on 2020/5/15.
 */
@FunctionalInterface
public interface Executor {
    /**
     * 执行动作
     */
    void execute();
}