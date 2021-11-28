package cn.org.atool.fluent.form;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.meta.ArgumentMeta;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.meta.entry.PagedEntry;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.form.annotation.MethodType.*;
import static cn.org.atool.fluent.form.registrar.FormServiceKit.TableEntityClass;

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
     * 构造新增记录Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回值类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta buildSave(Class entityClass, Method method, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, Save, args, returnType, null);
    }

    /**
     * 构造更新Action
     *
     * @param entityClass 操作表Entity类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta buildUpdate(Class entityClass, Method method, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, Update, args, returnType, null);
    }

    /**
     * 构造单个对象查询(count 或 findOne) Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回的单个对象类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta buildQuery(Class entityClass, Method method, Class returnType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, Query, args, returnType, null);
    }

    /**
     * 构造列表查询Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 列表元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta buildList(Class entityClass, Method method, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, Query, args, List.class, returnParameterType);
    }

    /**
     * 构造标准分页Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 分页元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta buildStdPage(Class entityClass, Method method, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, Query, args, StdPagedList.class, returnParameterType);
    }

    /**
     * 构造tag分页Action
     *
     * @param entityClass         操作表Entity类型
     * @param returnParameterType 分页元素类型
     * @param args                入参
     * @return ActionMeta
     */
    public static MethodMeta buildTagPage(Class entityClass, Method method, Class returnParameterType, ArgumentMeta... args) {
        return MethodMeta.meta(entityClass, method, Query, args, TagPagedList.class, returnParameterType);
    }

    /**
     * 参数为表单项
     *
     * @param methodType 方法类型
     * @param type       参数类型
     * @param index      参数次序
     * @return ArgumentMeta
     */
    public static <T, R> ArgumentMeta argForm(MethodType methodType, Type type, int index, Map types) {
        return new ArgumentMeta(methodType, null, true, EntryType.Form, type, index, types);
    }

    /**
     * 构建tag分页表单
     *
     * @param index 参数次序
     * @return PagedEntry
     */
    public static <T> ArgumentMeta argPaged(int index, Map types) {
        return argForm(MethodType.Query, PagedEntry.class, index, types);
    }
}