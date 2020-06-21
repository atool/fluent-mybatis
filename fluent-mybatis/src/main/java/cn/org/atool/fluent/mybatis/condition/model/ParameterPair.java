package cn.org.atool.fluent.mybatis.condition.model;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.org.atool.fluent.mybatis.condition.model.Constants.WRAPPER;

/**
 * ParameterPair: 自定义参数列表
 *
 * @author darui.wu
 * @create 2020/6/19 1:53 下午
 */
public class ParameterPair extends HashMap<String, Object> {
    /**
     * 自定义参数序号
     */
    protected final AtomicInteger sequence = new AtomicInteger();

    public ParameterPair() {
        super(16);
    }

    /**
     * 参数化处理
     *
     * @param sqlStr sql语句
     * @param params sql语句参数
     * @return 参数化的sql语句
     */
    public String paramSql(String sqlStr, Object... params) {
        if (MybatisUtil.isEmpty(sqlStr)) {
            throw new FluentMybatisException("sql parameter can't be null.");
        }

        StringBuilder buff = new StringBuilder();
        int index = 0;
        char prev = 0;
        for (char ch : sqlStr.toCharArray()) {
            if (prev == char_backslash && ch != char_question) {
                buff.append(/** 补上上一个反斜杠 **/char_backslash);
            }
            if (ch == char_question) {
                if (prev == char_backslash) {
                    buff.append(/** 字符 '?' 的反义处理 **/char_question);
                } else if (index < params.length) {
                    buff.append(this.parseParameter(params[index++]));
                } else {
                    throw new FluentMybatisException("占位符和参数个数不匹配:" + sqlStr);
                }
            } else if (ch != char_backslash) {
                buff.append(ch);
            }
            prev = ch;
        }
        if (prev == char_backslash) {
            buff.append(/** 补上结尾的反斜杠 **/char_backslash);
        }
        if (index < params.length) {
            throw new FluentMybatisException("占位符和参数个数不匹配:" + sqlStr);
        }
        return buff.toString();
    }

    /**
     * 构造参数占位变量，并设置占位符和变量值对应关系
     *
     * @param para 变量
     * @return 占位符
     */
    private String parseParameter(Object para) {
        String paramName = WRAPPER_PARAM + this.sequence.incrementAndGet();
        String placeholder = String.format(WRAPPER_PARAM_FORMAT, WRAPPER, paramName);
        this.put(paramName, para);
        return placeholder;
    }

    private static final char char_question = '?';
    private static final char char_backslash = '\\';
    /**
     * 变量名称格式, 前缀+序号
     */
    static final String WRAPPER_PARAM = "variable_";

    /**
     * 变量在xml文件中的占位符全路径表达式
     * 例子: #{ew.parameters.variable_1}
     */
    static final String WRAPPER_PARAM_FORMAT = "#{%s.parameters.%s}";
}