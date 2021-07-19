package cn.org.atool.fluent.mybatis.base.dao;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.model.FieldType;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.notNull;

/**
 * DaoHelper
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
public class DaoHelper {

    public static IUpdate buildUpdateEntityById(Supplier<IUpdate> supplier, IEntity entity) {
        IUpdate update = supplier.get();
        String primary = ((BaseWrapper) update).fieldName(FieldType.PRIMARY_ID);
        String version = ((BaseWrapper) update).fieldName(FieldType.LOCK_VERSION);
        Map<String, Object> map = entity.toColumnMap();
        boolean hasPrimaryId = false;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String column = entry.getKey();
            Object value = entry.getValue();
            // 主键字段和版本锁字段
            if (Objects.equals(column, primary) || Objects.equals(column, version)) {
                if (notNull(value)) {
                    update.where().apply(column, SqlOp.EQ, value);
                    hasPrimaryId = true;
                }
            } else {
                update.updateSet(column, value);
            }
        }
        if (!hasPrimaryId) {
            throw new RuntimeException("no primary value found.");
        } else {
            return update;
        }
    }

    /**
     * 根据entity非空字段构建update和where条件
     *
     * @param supplier IUpdate supplier
     * @param update   按Entity非空属性更新对应字段
     * @param where    按Entity非空属性构造条件
     * @return IUpdate
     */
    public static IUpdate buildUpdateByEntityNoN(Supplier<IUpdate> supplier, IEntity update, IEntity where) {
        IUpdate updater = supplier.get();
        boolean hasUpdate = false;
        Map<String, Object> updateMap = update.toColumnMap();
        for (Map.Entry<String, Object> entry : updateMap.entrySet()) {
            Object value = entry.getValue();
            if (notNull(value)) {
                updater.updateSet(entry.getKey(), value);
                hasUpdate = true;
            }
        }
        if (!hasUpdate) {
            throw new RuntimeException("no update value found.");
        }
        boolean hasWhere = false;
        Map<String, Object> whereMap = where.toColumnMap();
        for (Map.Entry<String, Object> entry : whereMap.entrySet()) {
            Object value = entry.getValue();
            if (notNull(value)) {
                updater.where().apply(entry.getKey(), SqlOp.EQ, value);
                hasWhere = true;
            }
        }
        if (!hasWhere) {
            throw new RuntimeException("no where condition found.");
        }
        return updater;
    }
}