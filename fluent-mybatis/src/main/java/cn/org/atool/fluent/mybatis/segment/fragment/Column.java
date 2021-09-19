package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import lombok.Getter;

import java.util.Objects;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Param_EW;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.DOT;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isColumnName;

/**
 * sql操作字段信息
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Getter
public class Column extends CachedFrag {
    /**
     * 表别名
     */
    private final String tAlias;
    /**
     * 字段名称
     */
    public final String column;

    private final FieldMapping mapping;

    private Column(IWrapper wrapper, String column, FieldMapping mapping) {
        super(db -> wrap(db, wrapper, column));
        this.column = column;
        this.tAlias = wrapper.getTableAlias();
        this.mapping = mapping == null ? ((BaseWrapper) wrapper).column(column) : mapping;
    }

    private Column(String tAlias, String column, FieldMapping mapping) {
        super(db -> wrap(db, tAlias, column));
        this.tAlias = tAlias;
        this.column = column;
        this.mapping = mapping;
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

    @Override
    public boolean notEmpty() {
        return notBlank(this.column);
    }

    @Override
    public String toString() {
        return column;
    }

    /**
     * 是映射字段, 并且para是映射字段类型值
     *
     * @param column 映射字段定义
     * @param para   参数值
     * @return true: 字段类型和参数类型一样
     */
    public static boolean isFieldAndAssignableFrom(IFragment column, Object para) {
        if (!(column instanceof Column)) {
            return false;
        } else {
            return ((Column) column).isAssignableFrom(para);
        }
    }

    /**
     * 返回 #{variableName, javaType=x.y.z.Name, typeHandler=xyzHandler}
     *
     * @param column   字段名称
     * @param paraName 变量名
     * @param para     变量值
     * @return #{variableName, javaType=x.y.z.Name, typeHandler=xyzHandler}
     */
    public static String expression(IFragment column, String paraName, Object para) {
        StringBuilder buff = new StringBuilder("#{")
            .append(Param_EW).append(Wrapper_Para).append(paraName);
        if (Column.isFieldAndAssignableFrom(column, para)) {
            Column _column = (Column) column;
            buff.append(", ").append("javaType=").append(_column.mapping.javaType.getName())
                .append(", ").append("typeHandler=").append(_column.mapping.typeHandler.getName());
        }
        return buff.append("}").toString();
    }

    static final String Wrapper_Para = ".data.parameters.";

    public static Column set(IWrapper wrapper, String column) {
        if (wrapper == null) {
            return new Column(EMPTY, column, null);
        } else {
            return new Column(wrapper, column, null);
        }
    }

    public static Column set(IWrapper wrapper, FieldMapping mapping) {
        if (wrapper == null) {
            return new Column(EMPTY, mapping.column, mapping);
        } else {
            return new Column(wrapper, mapping.column, mapping);
        }
    }

    public static Column set(String tAlias, String column) {
        return new Column(tAlias, column, null);
    }

    /* ================ */

    private static String wrap(DbType db, String tAlias, String column) {
        if (isBlank(column)) {
            return EMPTY;
        } else if (isBlank(tAlias)) {
            return db.wrap(column);
        } else {
            return tAlias + DOT + db.wrap(column);
        }
    }

    /**
     * 字段部分 [t.]`column`
     *
     * @return 字段部分 [t.]`column`
     */
    private static String wrap(DbType db, IWrapper wrapper, String column) {
        if (isBlank(column)) {
            return EMPTY;
        }
        String columnAs = column;
        if (isColumnNameAndNotAlias(wrapper, column)) {
            columnAs = db.wrap(column);
        }
        if (wrapper == null || isBlank(wrapper.getTableAlias())) {
            return columnAs;
        } else if (isColumnNameAndNotAlias(wrapper, column)) {
            return wrapper.getTableAlias() + DOT + columnAs;
        } else {
            return columnAs;
        }
    }

    private static boolean isColumnNameAndNotAlias(IWrapper wrapper, String column) {
        if (wrapper == null) {
            return false;
        } else if (!isColumnName(column)) {
            return false;
        } else {
            return !wrapper.data().getFieldAlias().contains(column);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(column, ((Column) o).column);
    }

    @Override
    public int hashCode() {
        return column != null ? column.hashCode() : 0;
    }

    public static boolean columnEquals(Object seg, String column) {
        if (seg instanceof Column) {
            return Objects.equals(column, ((Column) seg).column);
        } else {
            return false;
        }
    }
}