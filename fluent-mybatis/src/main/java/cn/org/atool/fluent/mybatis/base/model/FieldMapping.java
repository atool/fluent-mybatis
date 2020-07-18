package cn.org.atool.fluent.mybatis.base.model;

/**
 * FieldMeta: 实体字段和数据库字段映射信息
 *
 * @author darui.wu
 * @create 2020/6/23 9:16 上午
 */
public class FieldMapping {
    /**
     * 属性名称
     */
    public final String name;
    /**
     * 字段名称
     */
    public final String column;

    public FieldMapping(String name, String column) {
        this.name = name;
        this.column = column;
    }

    /**
     * 返回字段名称
     *
     * @return
     */
    @Override
    public String toString() {
        return this.column;
    }
}