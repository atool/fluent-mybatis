package cn.org.atool.fluent.mybatis.segment.model;

/**
 * 字段
 *
 * @author wudarui
 */
public class ColumnSegment implements ISqlSegment {
    private final String column;

    public ColumnSegment(String column) {
        this.column = column;
    }

    @Override
    public String getSqlSegment() {
        return this.column;
    }

    public static ColumnSegment column(String column) {
        return new ColumnSegment(column);
    }
}
