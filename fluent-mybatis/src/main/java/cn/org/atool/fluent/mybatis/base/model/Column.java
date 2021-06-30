package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.ColumnSegment;
import lombok.Getter;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Param_EW;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isColumnName;

/**
 * sql操作字段信息
 *
 * @author darui.wu
 */
@Getter
public class Column {
    public static final Column EMPTY_COLUMN = Column.column(EMPTY);

    private final String column;

    private final String alias;

    private final BaseWrapper wrapper;

    private final FieldMapping mapping;

    private Column(String column, String alias, BaseWrapper wrapper) {
        this.column = column;
        this.alias = alias;
        this.wrapper = wrapper;
        this.mapping = wrapper == null ? null : wrapper.column(column);
    }

    public Column(FieldMapping mapping, String alias, BaseWrapper wrapper) {
        this.column = mapping.column;
        this.alias = alias;
        this.wrapper = wrapper;
        this.mapping = mapping;
    }

    /**
     * 字段部分 t.column [AS alias]?
     *
     * @return 字段部分 t.column [AS alias]?
     */
    public ColumnSegment columnSegment() {
        String columnAs = column + (isBlank(alias) ? EMPTY : " AS " + alias);
        String tAlias = EMPTY;
        /** 非别名字段 && 表别名非空**/
        if (wrapper != null && notBlank(wrapper.getTableAlias()) &&
            isColumnName(column) &&
            !wrapper.getWrapperData().getFieldAlias().contains(column)) {
            tAlias = wrapper.getTableAlias() + ".";
        }
        return ColumnSegment.column(tAlias + columnAs);
    }

    public boolean isAssignableFrom(Object para) {
        if (para == null ||
            this.mapping == null ||
            this.mapping.javaType == null ||
            this.mapping.typeHandler == null) {
            return false;
        }
        Class pClass = para.getClass();
        return this.mapping.javaType.isAssignableFrom(pClass);
    }

    /**
     * 是映射字段, 并且para是映射字段类型值
     *
     * @param column 映射字段定义
     * @param para   参数值
     * @return
     */
    public static boolean isFieldAndAssignableFrom(Column column, Object para) {
        return column != null && column.isAssignableFrom(para);
    }

    /**
     * 返回 #{variableName, javaType=x.y.z.Name, typeHandler=xyzHandler}
     *
     * @param column   字段名称
     * @param paraName 变量名
     * @param para     变量值
     * @return #{variableName, javaType=x.y.z.Name, typeHandler=xyzHandler}
     */
    public static String wrapColumn(Column column, String paraName, Object para) {
        StringBuilder buff = new StringBuilder("#{")
            .append(Param_EW).append(Wrapper_Para).append(paraName);
        if (Column.isFieldAndAssignableFrom(column, para)) {
            buff.append(", ").append("javaType=").append(column.mapping.javaType.getName())
                .append(", ").append("typeHandler=").append(column.mapping.typeHandler.getName());
        }
        return buff.append("}").toString();
    }

    static final String Wrapper_Para = ".wrapperData.parameters.";

    public static Column column(String column) {
        return new Column(column, null, null);
    }

    public static Column column(String column, BaseWrapper wrapper) {
        return new Column(column, null, wrapper);
    }

    public static Column column(FieldMapping mapping, BaseWrapper wrapper) {
        return new Column(mapping, null, wrapper);
    }

    @Override
    public String toString() {
        return "Column[" + column + ']';
    }
}