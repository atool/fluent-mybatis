package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * ParameterPair: 自定义参数列表
 *
 * @author darui.wu 2020/6/19 1:53 下午
 */
public class Parameters extends HashMap<String, Object> {
    /**
     * 全局实例化的Parameters序号
     */
    private static final AtomicLong globalSeq = new AtomicLong(0);
    /**
     * join表别名序号
     */
    private static final AtomicLong alias = new AtomicLong(0);
    /**
     * 当前实例序号, 具有全局唯一性, 保证变量的唯一性
     */
    private final long instanceNo = globalSeq.incrementAndGet();
    /**
     * 自定义参数序号
     */
    private final AtomicInteger sequence = new AtomicInteger();

    /**
     * 自动分配表别名
     *
     * @return ignore
     */
    public static String alias() {
        return "t" + alias.incrementAndGet();
    }

    public Parameters() {
        super(16);
    }

    /**
     * 往查询/更新中增加带参数的sql片段
     *
     * @param wrapper 查询/更新实例
     * @param sql     外部传入的原始sql片段, 带有占位符 '?'
     * @param args    占位符参数列表
     * @return 经过占位符处理的sql
     */
    public static String parseSql(IWrapper wrapper, String sql, Object... args) {
        return wrapper.getWrapperData().getParameters().paramSql(null, sql, args);
    }

    /**
     * 参数化处理
     *
     * @param column 映射字段, 如果 = null, 表示非原始字段赋值
     * @param sqlStr sql语句
     * @param params sql语句参数
     * @return 参数化的sql语句
     */
    public String paramSql(Column column, String sqlStr, Object... params) {
        if (If.isBlank(sqlStr)) {
            throw new FluentMybatisException("sql parameter can't be null.");
        }

        StringBuilder buff = new StringBuilder();
        int index = 0;
        char prev = 0;
        for (char ch : sqlStr.toCharArray()) {
            if (prev == char_backslash && ch != char_question) {
                buff.append(/* 补上上一个反斜杠 **/char_backslash);
            }
            if (ch == char_question) {
                if (prev == char_backslash) {
                    buff.append(/* 字符 '?' 的反义处理 **/char_question);
                } else if (index < params.length) {
                    buff.append(this.putParameter(column, params[index++]));
                } else {
                    throw new FluentMybatisException("占位符和参数个数不匹配:" + sqlStr);
                }
            } else if (ch != char_backslash) {
                buff.append(ch);
            }
            prev = ch;
        }
        if (prev == char_backslash) {
            buff.append(/* 补上结尾的反斜杠 **/char_backslash);
        }
        if (index < params.length) {
            throw new FluentMybatisException("占位符和参数个数不匹配:" + sqlStr);
        }
        return buff.toString();
    }

    /**
     * 构造参数占位变量，并设置占位符和变量值对应关系
     *
     * @param column 被赋值字段
     * @param para   变量
     * @return 占位符
     */
    public String putParameter(Column column, Object para) {
        String paramName = WRAPPER_PARAM + this.instanceNo + "_" + this.sequence.incrementAndGet();
        this.put(paramName, para);
        return Column.wrapColumn(column, paramName, para);
    }

    private static final char char_question = '?';
    private static final char char_backslash = '\\';
    /**
     * 变量名称格式, 前缀+序号
     */
    private static final String WRAPPER_PARAM = "variable_";

    /**
     * 共享变量
     */
    private final List<WeakReference<Parameters>> shared = new ArrayList<>();

    private Parameters nextShared(Iterator<WeakReference<Parameters>> it) {
        WeakReference<Parameters> ref = it.next();
        if (ref == null || ref.get() == null) {
            it.remove();
            return null;
        } else {
            return ref.get();
        }
    }

    @Override
    public Object put(String key, Object value) {
        Object obj = super.put(key, value);
        for (Iterator<WeakReference<Parameters>> it = shared.iterator(); it.hasNext(); ) {
            Parameters parameter = this.nextShared(it);
            if (parameter != null) {
                parameter.put(key, value);
            }
        }
        return obj;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        super.putAll(m);
        for (Iterator<WeakReference<Parameters>> it = shared.iterator(); it.hasNext(); ) {
            Parameters parameter = this.nextShared(it);
            if (parameter != null) {
                parameter.putAll(m);
            }
        }
    }

    /**
     * 设置join查询（或子查询）的共享变量
     *
     * @param shared 共享变量
     */
    public void sharedParameter(Parameters shared) {
        if (this == shared) {
            return;
        }
        // 已设置过共享
        for (WeakReference<Parameters> item : this.shared) {
            if (item.get() == shared) {
                return;
            }
        }
        this.shared.add(new WeakReference<>(shared));
        shared.putAll(this);
    }
}