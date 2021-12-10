package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.segment.model.Parameters;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isSpace;
import static java.lang.Math.min;

/**
 * 完整自定义的sql语句解析重写工具类
 *
 * @author wudarui
 */
public class CustomizedSql {
    static List<String> parse(String sql) {
        List<String> list = new ArrayList<>();
        int index = 0;
        while (index < sql.length()) {
            int start = getStart(sql, index);
            if (start == -1) {
                list.add(sql.substring(index));
                break;
            } else {
                list.add(sql.substring(index, start));
            }
            int end = sql.indexOf(RIGHT_CURLY_BRACKET, start);
            if (end == -1) {
                throw new RuntimeException("illegal sql, starts '#{' or '${' in[" + start + "], but '}' not found. sql:" + sql);
            }
            String variable = sql.substring(start, end + 1);
            list.add(variable);
            index = end + 1;
        }
        return list;
    }

    private static int getStart(String sql, int index) {
        int start1 = sql.indexOf(HASH_MARK_LEFT_CURLY, index);
        int start2 = sql.indexOf(DOLLAR_LEFT_CURLY, index);
        if (start1 == -1) {
            return start2;
        } else if (start2 == -1) {
            return start1;
        } else {
            return min(start1, start2);
        }
    }

    /**
     * 对自定义的sql中的#{var}和${var}按照ew变量(#{ew.parameters...var})进行重写
     *
     * @param sql        sql
     * @param parameters Parameters
     * @param variable   parameter of sql
     * @return rewrite sql
     */
    public static String rewriteSql(String sql, Parameters parameters, Object variable) {
        String prefix = getVarName(parameters.putParameter(null, variable));
        List<String> segments = parse(sql);
        StringBuilder buff = new StringBuilder();
        for (String segment : segments) {
            if (segment.startsWith(HASH_MARK_LEFT_CURLY) && segment.endsWith(RIGHT_CURLY_BRACKET)) {
                variable = renameVariable(prefix, getVarName(segment));
                buff.append(HASH_MARK_LEFT_CURLY).append(variable).append(RIGHT_CURLY_BRACKET);
            } else if (segment.startsWith(DOLLAR_LEFT_CURLY) && segment.endsWith(RIGHT_CURLY_BRACKET)) {
                variable = renameVariable(prefix, getVarName(segment));
                buff.append(DOLLAR_LEFT_CURLY).append(variable).append(RIGHT_CURLY_BRACKET);
            } else {
                buff.append(segment);
            }
        }
        return buff.toString();
    }

    /**
     * 去掉${var}或#{var}格式化符号后的var名称
     *
     * @param variable variable
     * @return name of var
     */
    private static String getVarName(String variable) {
        return variable.substring(2, variable.length() - 1).trim();
    }

    public static String renameVariable(String prefix, String variable) {
        if (variable.equals(STR_VALUE)) {
            return prefix;
        }
        if (variable.startsWith(STR_VALUE)) {
            char ch = variable.charAt(5);
            if (isSpace(ch) || ch == '[' || ch == ',') {
                return prefix + variable.substring(5);
            }
        }
        return prefix + '.' + variable;
    }
}