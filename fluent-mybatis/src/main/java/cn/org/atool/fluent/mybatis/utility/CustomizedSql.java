package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.segment.model.Parameters;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isSpace;

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
            int start = sql.indexOf("#{", index);
            if (start == -1) {
                list.add(sql.substring(index));
                break;
            } else {
                list.add(sql.substring(index, start));
            }
            int end = sql.indexOf('}', start);
            if (end == -1) {
                throw new RuntimeException("illegal sql, starts '#{' in[" + start + "], but '}' not found. sql:" + sql);
            }
            String variable = sql.substring(start, end + 1);
            list.add(variable);
            index = end + 1;
        }
        return list;
    }

    public static String rewriteSql(String sql, Parameters parameters, Object variable) {
        String placeholder = parameters.putParameter(variable);
        String prefix = placeholder.substring(2, placeholder.length() - 1);
        List<String> segments = parse(sql);
        StringBuilder buff = new StringBuilder();
        for (String segment : segments) {
            if (segment.startsWith("#{") && segment.endsWith("}")) {
                variable = renameVariable(prefix, segment.substring(2, segment.length() - 1).trim());
                buff.append("#{").append(variable).append("}");
            } else {
                buff.append(segment);
            }
        }
        return buff.toString();
    }

    public static String renameVariable(String prefix, String variable) {
        if (variable.equals("value")) {
            return prefix;
        }
        if (variable.startsWith("value")) {
            char ch = variable.charAt(5);
            if (isSpace(ch) || ch == '[' || ch == ',') {
                return prefix + variable.substring(5);
            }
        }
        return prefix + '.' + variable;
    }
}
