package cn.org.atool.fluent.mybatis.demo.generate.datamap.table;

import cn.org.atool.fluent.mybatis.annotation.ColumnDef;
import cn.org.atool.fluent.mybatis.annotation.ColumnDef.PrimaryType;
import com.baomidou.mybatisplus.annotation.TableName;
import org.test4j.module.ICore.DataMap;
import org.test4j.tools.datagen.KeyValue;

import java.util.Date;
import java.util.function.Consumer;

import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP.Column;

/**
 * @ClassName NoAutoIdTableMap
 * @Description NoAutoIdTableMap
 *
 * @author generate code
 */
@TableName(NoAutoIdMP.Table_Name)
public class NoAutoIdTableMap extends DataMap<NoAutoIdTableMap> {
    /**
     * 设置no_auto_id对象column_1字段值
     */
    @ColumnDef(type = "varchar(20)")
    public transient final KeyValue<NoAutoIdTableMap> column_1 = new KeyValue(this, Column.column_1);
    /**
     * 设置no_auto_id对象id字段值
     */
    @ColumnDef(type = "varchar(50)", primary = PrimaryType.Customized)
    public transient final KeyValue<NoAutoIdTableMap> id = new KeyValue(this, Column.id);

    public NoAutoIdTableMap() {
        super();
    }

    public NoAutoIdTableMap(int size) {
        super(size);
    }

    /**
     * 创建NoAutoIdTableMap
     * 并初始化主键和gmtCreate, gmtModified, isDeleted等特殊值
     *
     */
    public NoAutoIdTableMap init() {
        return this;
    }

    public NoAutoIdTableMap with(Consumer<NoAutoIdTableMap> init) {
        init.accept(this);
        return this;
    }

    public static NoAutoIdTableMap create() {
        return new NoAutoIdTableMap(1);
    }

    public static NoAutoIdTableMap create(int size) {
        return new NoAutoIdTableMap(size);
    }

    public static class Factory {
        public NoAutoIdTableMap create() {
            return NoAutoIdTableMap.create();
        }

        public NoAutoIdTableMap create(int size) {
            return NoAutoIdTableMap.create(size);
        }

        public NoAutoIdTableMap createWithInit() {
            return NoAutoIdTableMap.create(1).init();
        }

        public NoAutoIdTableMap createWithInit(int size) {
            return NoAutoIdTableMap.create(size).init();
        }
    }
}