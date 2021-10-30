package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.meta.EntryMetas;
import cn.org.atool.fluent.form.meta.MethodMeta;
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
     * @param method 操作定义
     * @param metas  入参元数据
     * @return entity实例
     */
    public static <R> R save(MethodMeta method, EntryMetas metas) {
        IEntity entity = FormHelper.newEntity(method, metas);
        Object pk = RefKit.mapper(method.entityClass).save(entity);
        if (method.returnType == void.class || method.returnType == Void.class) {
            return null;
        } else if (method.returnType == Boolean.class || method.returnType == boolean.class) {
            return (R) (Boolean) (pk != null);
        } else if (method.returnType.isAssignableFrom(method.entityClass)) {
            return (R) entity;
        } else {
            return (R) FormHelper.entity2result(entity, method.returnType);
        }
    }

    /**
     * 更新操作
     *
     * @param method 操作定义
     * @param metas  入参元数据
     * @return ignore
     */
    public static int update(MethodMeta method, EntryMetas metas) {
        IUpdate update = FormHelper.newUpdate(method, metas);
        return RefKit.mapper(method.entityClass).updateBy(update);
    }

    /**
     * 构造查询条件实例
     *
     * @param method 操作定义
     * @param metas  入参元数据
     * @return 查询实例
     */
    public static Object query(MethodMeta method, EntryMetas metas) {
        IQuery query = FormHelper.newQuery(method, metas);
        if (method.isCount()) {
            int count = query.to().count();
            return method.isReturnLong() ? (long) count : count;
        } else if (method.isStdPage()) {
            /* 标准分页 */
            StdPagedList paged = query.to().stdPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), method.returnParameterType);
            return paged.setData(data);
        } else if (method.isTagPage()) {
            /* Tag分页 */
            TagPagedList paged = query.to().tagPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), method.returnParameterType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (method.isList()) {
            /* 返回List */
            List<IEntity> list = query.to().listEntity();
            return FormHelper.entities2result(list, method.returnParameterType);
        } else {
            /* 查找单条数据 */
            query.limit(1);
            IEntity entity = (IEntity) query.to().findOne().orElse(null);
            return FormHelper.entity2result(entity, method.returnType);
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