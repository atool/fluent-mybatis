package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.meta.ActionMeta;
import cn.org.atool.fluent.form.meta.FormMetas;
import cn.org.atool.fluent.form.setter.FormHelper;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.List;

/**
 * Form操作辅助类
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class FormKit {
    private static final KeyMap<Class> TableEntityClass = new KeyMap();

    private FormKit() {
    }

    /**
     * 构造eClass实体实例
     *
     * @param action 操作定义
     * @param metas  入参元数据
     * @return entity实例
     */
    public static <R> R save(ActionMeta action, FormMetas metas) {
        IEntity entity = FormHelper.newEntity(action, metas);
        Object pk = RefKit.mapper(action.entityClass).save(entity);
        if (action.returnType == void.class || action.returnType == Void.class) {
            return null;
        } else if (action.returnType == Boolean.class || action.returnType == boolean.class) {
            return (R) (Boolean) (pk != null);
        } else if (action.returnType.isAssignableFrom(action.entityClass)) {
            return (R) entity;
        } else {
            return (R) FormHelper.entity2result(entity, action.returnType);
        }
    }

    /**
     * 更新操作
     *
     * @param action 操作定义
     * @param metas  入参元数据
     * @return ignore
     */
    public static int update(ActionMeta action, FormMetas metas) {
        IUpdate update = FormHelper.newUpdate(action, metas);
        return RefKit.mapper(action.entityClass).updateBy(update);
    }

    /**
     * 构造查询条件实例
     *
     * @param action 操作定义
     * @param metas  入参元数据
     * @return 查询实例
     */
    public static Object query(ActionMeta action, FormMetas metas) {
        IQuery query = FormHelper.newQuery(action, metas);
        if (action.isCount()) {
            int count = query.to().count();
            return action.resultIsLong() ? (long) count : count;
        } else if (action.isStdPage()) {
            /* 标准分页 */
            StdPagedList paged = query.to().stdPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), action.returnParameterType);
            return paged.setData(data);
        } else if (action.isTagPage()) {
            /* Tag分页 */
            TagPagedList paged = query.to().tagPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), action.returnParameterType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (action.isList()) {
            /* 返回List */
            List<IEntity> list = query.to().listEntity();
            return FormHelper.entities2result(list, action.returnParameterType);
        } else {
            /* 查找单条数据 */
            query.limit(1);
            IEntity entity = (IEntity) query.to().findOne().orElse(null);
            return FormHelper.entity2result(entity, action.returnType);
        }
    }

    /**
     * 根据表名称获取实例类型
     *
     * @param table 表名称
     * @return 实例类型
     */
    public static Class<? extends IEntity> getEntityClass(String table) {
        if (If.isBlank(table)) {
            return null;
        }
        if (TableEntityClass.containsKey(table)) {
            return TableEntityClass.get(table);
        }
        AMapping mapping = RefKit.byTable(table);
        if (mapping == null) {
            throw new RuntimeException("The table[" + table + "] not found.");
        } else {
            return mapping.entityClass();
        }
    }

    /**
     * 定义映射关系
     *
     * @param table       表名称
     * @param entityClass 实例类型
     */
    public static void add(String table, Class<? extends IEntity> entityClass) {
        TableEntityClass.put(table, entityClass);
    }
}