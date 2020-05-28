package cn.org.atool.fluent.mybatis.method.model;

import cn.org.atool.fluent.mybatis.metadata.FieldInfo;
import cn.org.atool.fluent.mybatis.metadata.TableHelper;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.method.InjectMethod;

import java.util.List;

/**
 * MethodInject
 *
 * @author darui.wu
 * @create 2020/5/25 8:53 下午
 */
public class InjectMapperXml {
    /**
     * 生成mapperClass对应的mybatis配置文件
     *
     * @param mapperKlass mapper类
     * @param methods     注入的方法列表
     * @return
     */
    public static String buildMapperXml(Class mapperKlass, List<InjectMethod> methods) {
        Class entityKlass = TableHelper.extractEntity(mapperKlass);
        TableInfo tableInfo = TableHelper.getTableInfo(entityKlass);
        SqlBuilder xml = SqlBuilder.instance()
            .quotas("<?xml version='1.0' encoding='UTF-8'?>\n")
            .quotas("<!DOCTYPE mapper PUBLIC '-//mybatis.org//DTD Mapper 3.0//EN' 'http://mybatis.org/dtd/mybatis-3-mapper.dtd'>\n")
            .quotas("<mapper namespace='%s'>\n", mapperKlass.getName());

        resultMap(xml, entityKlass, tableInfo);
        selectSql(xml, tableInfo);
        for (InjectMethod xmlMethod : methods) {
            xml.newLine();
            xmlMethod(xml, entityKlass, tableInfo, xmlMethod);
            xml.newLine();
        }
        String text = xml.append("</mapper>").toString();
        return text;
    }

    /**
     * 构造select list部分
     *
     * @param xml
     * @param table
     */
    private static SqlBuilder selectSql(SqlBuilder xml, TableInfo table) {
        return xml.newLine()
            .quotas("<sql id='SELECT_COLUMNS'>").newLine().append("<![CDATA[")
            .append(table.getAllSqlSelect())
            .append("]]>").newLine()
            .append("</sql>").newLine();
    }

    /**
     * 构造方法定义
     *
     * @param xml
     * @param entity
     * @param table
     * @param method
     */
    private static void xmlMethod(SqlBuilder xml, Class entity, TableInfo table, InjectMethod method) {
        xml.append(method.getMethodSql(entity, table));
    }

    /**
     * 构造 &lt;resultMap>部分
     *
     * @param xml
     * @param entityKlass
     * @param table
     */
    private static void resultMap(SqlBuilder xml, Class entityKlass, TableInfo table) {
        xml.append("<!-- base result map -->").newLine()
            .quotas("<resultMap id='BaseResultMap' type='%s'>", entityKlass.getName()).newLine();
        if (table.getPrimary() != null) {
            xml.quotas("<id column='%s' property='%s' />", table.getKeyColumn(), table.getKeyProperty()).newLine();
        }
        for (FieldInfo field : table.getFields()) {
            xml.quotas("<result column='%s' property='%s' />", field.getColumn(), field.getProperty()).newLine();
        }
        xml.append("</resultMap>").newLine();
    }
}