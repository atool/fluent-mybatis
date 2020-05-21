package cn.org.atool.fluent.mybatis.util;

/**
 * fluent mybatis 常量定义
 *
 * @author darui.wu
 */
public interface Constants {
    /**
     * project name
     */
    String FLUENT_MYBATIS = "fluent-mybatis";

    /**
     * wrapper 类
     */
    String WRAPPER = "ew";

    /**
     * where
     */
    String WHERE = "WHERE";


    String WRAPPER_PARAM = "MPGENVAL";
    String WRAPPER_PARAM_FORMAT = "#{%s.paramNameValuePairs.%s}";

    /**
     * 字符串 is
     */
    String IS = "is";
    String AND = "and";
    String ASTERISK = "*";
    String STAR = ASTERISK;
    String COMMA = ",";
    String DOT = ".";
    String EMPTY = "";
    String EQUALS = "=";
    String LEFT_BRACKET = "(";
    String LEFT_CHEV = "<";
    String RIGHT_BRACKET = ")";
    String RIGHT_CHEV = ">";
    String SEMICOLON = ";";
    String SPACE = " ";
    String UNDERSCORE = "_";
}