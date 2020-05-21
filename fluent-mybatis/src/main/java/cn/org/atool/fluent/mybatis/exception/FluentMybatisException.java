package cn.org.atool.fluent.mybatis.exception;

import cn.org.atool.fluent.mybatis.util.StringUtils;

/**
 * FluentMybatisException 异常类
 *
 * @author darui.wu
 */
public class FluentMybatisException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FluentMybatisException(String message) {
        super(message);
    }

    public FluentMybatisException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * FluentMybatisException 异常
     *
     * @param msg 消息
     * @param t   异常信息
     * @return 返回异常
     */
    public static FluentMybatisException instance(String msg, Throwable t, Object... params) {
        return new FluentMybatisException(StringUtils.format(msg, params), t);
    }

    /**
     * 重载的方法
     *
     * @param msg 消息
     * @return 返回异常
     */
    public static FluentMybatisException instance(String msg, Object... params) {
        return new FluentMybatisException(StringUtils.format(msg, params));
    }
}