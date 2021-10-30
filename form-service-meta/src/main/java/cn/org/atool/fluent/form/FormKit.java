package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.meta.ArgumentMeta;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.meta.PagedEntry;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;

import java.util.List;

import static cn.org.atool.fluent.form.annotation.MethodType.*;
import static cn.org.atool.fluent.form.registrar.FormServiceFactoryBean.TableEntityClass;

/**
 * Form操作辅助类
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unused"})
public class FormKit {
    private FormKit() {
    }

    /**
     * 定义映射关系
     *
     * @param table       表名称
     * @param entityClass 实例类型
     */
    public static void mapping(String table, Class<? extends IEntity> entityClass) {
        TableEntityClass.put(table, entityClass);
    }

    /**
     * 参数为表单项
     *
     * @param type 参数类型
     * @param arg  参数值
     * @return ArgumentMeta
     */
    public static ArgumentMeta formArg(Class type, Object arg) {
        return new ArgumentMeta(null, EntryType.Form, type, arg);
    }

    /**
     * 构造新增记录Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回值类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta save(Class entityClass, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, Save, args, returnType, null);
    }

    /**
     * 构造更新Action
     *
     * @param entityClass 操作表Entity类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta update(Class entityClass, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, Update, args, int.class, null);
    }

    /**
     * 构造单个对象查询(count 或 findOne) Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回的单个对象类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta query(Class entityClass, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, Query, args, returnType, null);
    }

    /**
     * 构造列表查询Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 列表元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta list(Class entityClass, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, Query, args, List.class, returnParameterType);
    }

    /**
     * 构造标准分页Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 分页元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta stdPage(Class entityClass, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, Query, args, StdPagedList.class, returnParameterType);
    }

    /**
     * 构造tag分页Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 分页元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta tagPage(Class entityClass, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, Query, args, TagPagedList.class, returnParameterType);
    }

    /**
     * 构建tag分页表单
     *
     * @param pageSize 每页记录数
     * @param pagedTag tag分页其实标识
     * @return PagedEntry
     */
    public static PagedEntry tagPaged(int pageSize, Object pagedTag) {
        return new PagedEntry().setPageSize(pageSize)
            .setPagedTag(pagedTag == null ? null : String.valueOf(pagedTag));
    }

    /**
     * 构建tag分页表单
     *
     * @param pageSize 每页记录数
     * @param currPage tag分页其实标识
     * @return PagedEntry
     */
    public static PagedEntry stdPaged(int pageSize, Integer currPage) {
        return new PagedEntry().setPageSize(pageSize)
            .setCurrPage(currPage == null || currPage < 0 ? 0 : currPage);
    }
}