package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName NoAutoIdMapping
 * @Description no_auto_id映射定义
 *
 * @author generate code
 */
public interface NoAutoIdMapping {
    /**
     * 表名称
     */
    String Table_Name = "no_auto_id";
    /**
    * 实体名称
    */
    String Entity_NAME = "NoAutoIdEntity";

    /**
     * id字段映射
     * <p></p>
     */
    FieldMapping id = new FieldMapping("id", "id");
    /**
     * column1字段映射
     * <p></p>
     */
    FieldMapping column1 = new FieldMapping("column1", "column_1");

    /**
     * 实例属性和数据库字段对应表
     */
    Map<String, String> Property2Column = new HashMap<String,String>(){
        {
            this.put(id.name, id.column);
            this.put(column1.name, column1.column);
        }
    };

    /**
     * 数据库所有字段列表
     */
    Set<String> ALL_COLUMNS = new HashSet<String>() {
        {
            this.add(id.column);
            this.add(column1.column);
        }
    };
}