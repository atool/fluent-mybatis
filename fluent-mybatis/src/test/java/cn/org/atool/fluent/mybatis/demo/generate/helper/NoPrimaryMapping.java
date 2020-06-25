package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName NoPrimaryMapping
 * @Description no_primary映射定义
 *
 * @author generate code
 */
public interface NoPrimaryMapping {
    /**
     * 表名称
     */
    String Table_Name = "no_primary";
    /**
    * 实体名称
    */
    String Entity_NAME = "NoPrimaryEntity";

    /**
     * column1字段映射
     * <p></p>
     */
    FieldMeta column1 = new FieldMeta("column1", "column_1");
    /**
     * column2字段映射
     * <p></p>
     */
    FieldMeta column2 = new FieldMeta("column2", "column_2");

    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(column1.name, column1.column);
            this.put(column2.name, column2.column);
        }
    };

    /**
     * 数据库所有字段列表
     */
    Set<String> ALL_COLUMNS = new HashSet<String>() {
        {
            this.add(column1.column);
            this.add(column2.column);
        }
    };
}