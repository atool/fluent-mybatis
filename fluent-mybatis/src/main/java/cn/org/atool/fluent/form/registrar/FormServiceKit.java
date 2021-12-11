package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.form.meta.MethodArgs;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.setter.FormHelper;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 静态方法
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface FormServiceKit {
    /**
     * table name 和 Entity class映射定义
     */
    KeyMap<Class> TableEntityClass = new KeyMap();

    /**
     * 构造eClass实体实例
     *
     * @param meta 操作定义
     * @param args 入参
     * @return entity实例
     */
    static Object save(MethodMeta meta, Object... args) {
        IEntity entity = FormHelper.newEntity(meta, args);
        Object pk = RefKit.mapper(meta.entityClass).save(entity);
        if (meta.isReturnVoid()) {
            return null;
        } else if (meta.isReturnBool()) {
            return pk != null;
        } else if (meta.returnType.isAssignableFrom(meta.entityClass)) {
            return entity;
        } else {
            return FormHelper.entity2result(entity, meta.returnType);
        }
    }

    /**
     * 批量插入
     *
     * @param meta 入参元数据
     * @param list 入参列表
     * @return entity实例
     */
    static Object save(MethodMeta meta, Collection list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the save list can't be empty.");
        }
        List<IEntity> entities = new ArrayList<>();
        for (Object obj : list) {
            IEntity entity = FormHelper.newEntity(meta, new Object[]{obj});
            entities.add(entity);
        }
        int count = RefKit.mapper(meta.entityClass).save(entities);
        return returnUpdateResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta 方法元数据
     * @param list 入参是List
     * @return ignore
     */
    static Object delete(MethodMeta meta, Collection list, boolean isLogic) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the delete list can't be empty.");
        }
        List<Integer> counts = new ArrayList<>(list.size());
        for (Object obj : list) {
            IQuery query = FormHelper.newQuery(new MethodArgs(meta, new Object[]{obj}));
            int count;
            if (isLogic) {
                count = RefKit.mapper(meta.entityClass).logicDelete(query);
            } else {
                count = RefKit.mapper(meta.entityClass).delete(query);
            }
            counts.add(count);
        }
        return returnUpdateResult(meta, counts);
    }

    /**
     * 更新操作
     *
     * @param meta 操作定义
     * @param args 入参
     * @return ignore
     */
    static Object update(MethodMeta meta, Object... args) {
        IUpdate update = FormHelper.newUpdate(new MethodArgs(meta, args));
        int count = RefKit.mapper(meta.entityClass).updateBy(update);
        return returnUpdateResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta    操作定义
     * @param isLogic 是否逻辑删除
     * @param args    入参
     * @return ignore
     */
    static Object delete(MethodMeta meta, boolean isLogic, Object... args) {
        IQuery query = FormHelper.newQuery(new MethodArgs(meta, args));
        int count;
        if (isLogic) {
            count = RefKit.mapper(meta.entityClass).logicDelete(query);
        } else {
            count = RefKit.mapper(meta.entityClass).delete(query);
        }
        return returnUpdateResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta 方法元数据
     * @param list 入参是List
     * @return ignore
     */
    static Object update(MethodMeta meta, Collection list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the update list can't be empty.");
        }
        IUpdate[] updates = new IUpdate[list.size()];
        int index = 0;
        for (Object obj : list) {
            IUpdate update = FormHelper.newUpdate(new MethodArgs(meta, new Object[]{obj}));
            updates[index++] = update;
        }
        int count = RefKit.mapper(meta.entityClass).updateBy(updates);
        return returnUpdateResult(meta, count);
    }

    /**
     * 构造查询条件实例
     *
     * @param meta 操作定义
     * @param args 入参
     * @return 查询实例
     */
    static Object query(MethodMeta meta, Object... args) {
        IQuery query = FormHelper.newQuery(new MethodArgs(meta, args));
        if (meta.isCount()) {
            int count = query.to().count();
            return meta.isReturnLong() ? (long) count : count;
        } else if (meta.isStdPage()) {
            /* 标准分页 */
            StdPagedList paged = query.to().stdPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), meta.returnParameterType);
            return paged.setData(data);
        } else if (meta.isTagPage()) {
            /* Tag分页 */
            TagPagedList paged = query.to().tagPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), meta.returnParameterType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (meta.isList()) {
            /* 返回List */
            List<IEntity> list = query.to().listEntity();
            return FormHelper.entities2result(list, meta.returnParameterType);
        } else if (meta.returnType == boolean.class || meta.returnType == Boolean.class) {
            /* exists操作 */
            ((BaseQuery) query).select("1").limit(1);
            Optional ret = query.to().findOneMap();
            return ret.isPresent();
        } else {
            /* 查找单条数据 */
            query.limit(1);
            IEntity entity = (IEntity) query.to().findOne().orElse(null);
            return FormHelper.entity2result(entity, meta.returnType);
        }
    }

    static Object returnUpdateResult(MethodMeta meta, int count) {
        if (meta.isReturnVoid()) {
            return null;
        } else if (meta.isReturnBool()) {
            return count > 0;
        } else if (meta.isReturnInt()) {
            return count;
        } else if (meta.isReturnLong()) {
            return (long) count;
        } else {
            throw new IllegalStateException("The type of batch result can only be: void, int, long, or boolean.");
        }
    }

    static Object returnUpdateResult(MethodMeta meta, List<Integer> counts) {
        if (meta.isReturnList()) {
            return counts;
        } else {
            int total = 0;
            for (int c : counts) {
                total += c;
            }
            return returnUpdateResult(meta, total);
        }
    }
}