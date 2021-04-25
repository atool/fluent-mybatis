package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.crud.*;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;

import java.util.stream.Stream;

/**
 * 批量增删改(没有查)操作构造
 *
 * @author wudarui
 */
public interface BatchCrud extends IWrapper {
    /**
     * 构造批量增删改构造器
     *
     * @return
     */
    static BatchCrud batch() {
        return new BatchCrudImpl();
    }

    /**
     * 按顺序添加Insert语句
     *
     * @param entities
     * @return
     */
    BatchCrud addInsert(IEntity... entities);

    /**
     * 添加  insert into a_table (fields) select fields from b_table where ...;
     *
     * @param insertTable 要插入数据的表名, inset into insertTable
     * @param fields      要插入的字段列表, a_table fields
     * @param query       要插入的字段值, query: select xxx from b_table
     * @return
     * @see IEntityMapper#insertSelect(String[], IQuery)
     */
    BatchCrud addInsertSelect(String insertTable, String[] fields, IQuery query);

    /**
     * 添加  insert into a_table (fields) select fields from b_table where ...;
     *
     * @param insertTable 要插入数据的表名, inset into insertTable
     * @param fields      要插入的字段列表, a_table fields
     * @param query       要插入的字段值, query: select xxx from b_table
     * @return
     * @see IEntityMapper#insertSelect(String[], IQuery)
     */
    default BatchCrud addInsertSelect(String insertTable, FieldMapping[] fields, IQuery query) {
        return this.addInsertSelect(insertTable, Stream.of(fields).map(c -> c.column).toArray(String[]::new), query);
    }

    /**
     * 按顺序添加update语句
     *
     * @param updates
     * @return
     */
    BatchCrud addUpdate(IBaseUpdate... updates);

    /**
     * 按顺序添加delete语句
     *
     * @param deletes
     * @return
     */
    BatchCrud addDelete(IBaseQuery... deletes);
}