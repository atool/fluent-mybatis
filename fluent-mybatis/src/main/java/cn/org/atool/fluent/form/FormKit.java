package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.meta.FormMetas;
import cn.org.atool.fluent.form.setter.FormHelper;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import static cn.org.atool.fluent.mybatis.If.isBlank;

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
     * @param eClass entity类型
     * @param form   entity类型
     * @return entity实例
     */
    public static <E extends IEntity> E newEntity(Class<E> eClass, Object form, FormMetas metas) {
        return FormHelper.newEntity(eClass, form, metas);
    }

    /**
     * 构造查询条件实例
     *
     * @param eClass entity类型
     * @param form   entity类型
     * @return 查询实例
     */
    public static <E extends IEntity> IQuery<E> newQuery(Class<E> eClass, Object form, FormMetas metas) {
        return FormHelper.newQuery(eClass, form, metas);
    }

    /**
     * 构造更新条件实例
     *
     * @param eClass entity类型
     * @param form   entity类型
     * @return 更新实例
     */
    public static <E extends IEntity> IUpdate<E> newUpdate(Class<E> eClass, Object form, FormMetas metas) {
        return FormHelper.newUpdate(eClass, form, metas);
    }

    /**
     * 返回form表单元数据定义
     *
     * @param aClass 表单类型
     * @return 元数据列表
     */
    public static FormMetas metas(Class<?> aClass) {
        return FormMetas.getFormMeta(aClass);
    }

    /**
     * 根据表名称获取实例类型
     *
     * @param table 表名称
     * @return 实例类型
     */
    public static Class<? extends IEntity> getEntityClass(String table) {
        if (isBlank(table)) {
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