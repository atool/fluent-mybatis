package cn.org.atool.fluent.mybatis.base.model;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.ColumnSegment;
import lombok.Getter;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Param_EW;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isColumnName;

/**
 * sql操作字段信息
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
@Getter
public class Column {
    public static final Column EMPTY_COLUMN = new Column(EMPTY, null) {
        protected DbType dbType() {
            return DbType.OTHER;
        }
    };

    private final String column;

    private final BaseWrapper wrapper;

    private final FieldMapping mapping;

    private Column(String column, BaseWrapper wrapper) {
        this.column = column;
        this.wrapper = wrapper;
        this.mapping = wrapper == null ? null : wrapper.column(column);
    }

    public Column(FieldMapping mapping, BaseWrapper wrapper) {
        this.column = mapping.column;
        this.wrapper = wrapper;
        this.mapping = mapping;
    }

    /**
     * 字段部分 [t.]`column`
     *
     * @return 字段部分 [t.]`column`
     */
    public String wrapColumn() {
        String columnAs = column;
        if (this.isColumnNameAndNotAlias()) {
            columnAs = this.dbType().wrap(column);
        }
        String tAlias = EMPTY;
        /** 非别名字段 && 表别名非空 **/
        if (wrapper != null &&
            notBlank(wrapper.getTableAlias()) &&
            this.isColumnNameAndNotAlias()) {
            tAlias = wrapper.getTableAlias() + ".";
        }
        return tAlias + columnAs;
    }

    protected DbType dbType() {
        if (wrapper != null && wrapper.dbType() != null) {
            return wrapper.dbType();
        } else {
            return DbType.OTHER;
        }
    }

    /**
     * 字段部分 [t.]`column`
     *
     * @return 字段部分 [t.]`column`
     */
    public ColumnSegment columnSegment() {
        String columnAsAlias = this.wrapColumn();
        return ColumnSegment.column(columnAsAlias);
    }

    private boolean isColumnNameAndNotAlias() {
        if (wrapper == null) {
            return false;
        }
        String unWrapper = this.dbType().unwrap(column);
        return isColumnName(unWrapper) &&
            !wrapper.getWrapperData().getFieldAlias().contains(unWrapper);
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
     * @return true: 字段类型和参数类型一样
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

    public static Column column(String column, BaseWrapper wrapper) {
        return new Column(column, wrapper);
    }

    public static Column column(FieldMapping mapping, BaseWrapper wrapper) {
        return new Column(mapping, wrapper);
    }

    @Override
    public String toString() {
        return "Column[" + column + ']';
    }
}