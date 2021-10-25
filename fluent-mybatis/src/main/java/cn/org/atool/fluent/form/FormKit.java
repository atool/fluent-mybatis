package cn.org.atool.fluent.form;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.form.setter.FormHelper;

/**
 * Form操作辅助类
 *
 * @author wudarui
 */
public class FormKit {
    private FormKit() {
    }

    /**
     * 构造eClass实体实例
     *
     * @param eClass entity类型
     * @param form   entity类型
     * @return entity实例
     */
    public static <E extends IEntity> E newEntity(Class<E> eClass, Object form) {
        return FormHelper.newEntity(eClass, form);
    }

    /**
     * 构造查询条件实例
     *
     * @param eClass entity类型
     * @param form   entity类型
     * @return 查询实例
     */
    public static <E extends IEntity> IQuery<E> newQuery(Class<E> eClass, Object form) {
        return FormHelper.newQuery(eClass, form);
    }

    /**
     * 构造更新条件实例
     *
     * @param eClass entity类型
     * @param form   entity类型
     * @return 更新实例
     */
    public static <E extends IEntity> IUpdate<E> newUpdate(Class<E> eClass, Object form) {
        return FormHelper.newUpdate(eClass, form);
    }
}