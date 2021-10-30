package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static cn.org.atool.fluent.form.annotation.MethodType.*;
import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * FormService方法元数据
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
@Accessors(chain = true)
public class MethodMeta {
    /**
     * 操作表对象
     */
    public final Class entityClass;
    /**
     * 调用方法
     */
    public final String method;
    /**
     * 操作类型
     */
    public final MethodType methodType;
    /**
     * 入参类型
     */
    public final ArgumentMeta[] args;
    /**
     * 返回值类型
     */
    public final Class returnType;
    /**
     * 返回值泛型类型(如果有)
     */
    public final Class returnParameterType;

    private MethodMeta(Class entityClass, MethodType methodType, ArgumentMeta[] args, Class returnType, Class returnParameterType) {
        this.entityClass = entityClass;
        this.method = null;
        this.methodType = methodType;
        this.args = args;
        this.returnType = returnType;
        this.returnParameterType = returnParameterType;
    }

    MethodMeta(Class entityClass, Method method, Object[] args) {
        this.entityClass = entityClass;
        this.method = method.toString();
        FormMethod aMethod = method.getDeclaredAnnotation(FormMethod.class);
        this.methodType = aMethod == null ? Query : aMethod.type();
        this.args = new ArgumentMeta[args.length];
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            this.args[i] = new ArgumentMeta(parameters[i], args[i]);
        }
        this.returnType = method.getReturnType();
        this.returnParameterType = this.getParameterTypeOfReturn(method);
    }

    /*** ============================ ***/
    public static final KeyMap<EntryMetas> MethodArgsMeta = new KeyMap<>();

    /**
     * 构造入参的元数据列表
     *
     * @return FormMetas
     */
    public EntryMetas metas() {
        if (isBlank(method)) {
            return this.buildMetasFromArgs();
        }
        if (MethodArgsMeta.containsKey(method)) {
            return MethodArgsMeta.get(method);
        }
        synchronized (MethodMeta.class) {
            if (MethodArgsMeta.containsKey(method)) {
                return MethodArgsMeta.get(method);
            }
            EntryMetas argsMetas = this.buildMetasFromArgs();
            MethodArgsMeta.put(method, argsMetas);
            return argsMetas;
        }
    }

    private EntryMetas buildMetasFromArgs() {
        this.validate();
        EntryMetas argsMetas = new EntryMetas();
        for (int index = 0; index < this.args.length; index++) {
            this.addArgMeta(argsMetas, index);
        }
        return argsMetas;
    }

    private void addArgMeta(EntryMetas argsMetas, int index) {
        ArgumentMeta arg = this.args[index];
        if (arg.notFormObject()) {
            Function<MethodMeta, Object> getter = method -> method.args[index].value;
            EntryMeta meta = new EntryMeta(arg.entryName, arg.entryType, getter, arg.ignoreNull);
            argsMetas.addMeta(meta);
        } else {
            Class aClass = arg.type;
            EntryMetas metas = EntryMetas.getFormMeta(aClass);
            for (EntryMeta meta : metas.getMetas()) {
                argsMetas.addMeta(this.argMeta(index, meta));
            }
            argsMetas.addMeta(this.argMeta(index, metas.getPageSize()));
            argsMetas.addMeta(this.argMeta(index, metas.getCurrPage()));
            argsMetas.addMeta(this.argMeta(index, metas.getPagedTag()));
        }
    }

    private EntryMeta argMeta(int index, EntryMeta meta) {
        if (meta == null) {
            return null;
        }
        Function<MethodMeta, Object> getter = method -> {
            Object object = method.args[index].value;
            return meta.getter.apply(object);
        };
        return new EntryMeta(meta.name, meta.type, getter, meta.ignoreNull);
    }

    private void validate() {
        if (this.entityClass == null) {
            throw new IllegalArgumentException("Annotation[@Action] must be declared on method[" + method + "].");
        }
        if (this.args == null || this.args.length == 0) {
            throw new IllegalArgumentException("Method[" + method + "] must be have one parameter.");
        }
        if (this.returnType == null) {
            throw new IllegalArgumentException("The result type can't be null.");
        }
    }

    /**
     * 返回方法返回参数的泛型参数类型
     *
     * @param method api方法
     * @return 泛型参数类型
     */
    private Class getParameterTypeOfReturn(Method method) {
        Class pType = null;
        Type rType = method.getGenericReturnType();
        if (rType instanceof ParameterizedType) {
            pType = (Class) ((ParameterizedType) rType).getActualTypeArguments()[0];
        }
        return pType;
    }

    /**
     * 是否count接口
     *
     * @return true/false
     */
    public boolean isCount() {
        return this.methodType == Query && (this.isReturnInt() || this.isReturnLong());
    }

    /**
     * 结果值是long型
     *
     * @return true/false
     */
    public boolean isReturnLong() {
        return returnType == Long.class || returnType == long.class;
    }

    /**
     * 结果值是int型
     *
     * @return true/false
     */
    public boolean isReturnInt() {
        return returnType == Integer.class || returnType == int.class;
    }

    /**
     * 是否标准分页
     *
     * @return true/false
     */
    public boolean isStdPage() {
        return methodType == Query && StdPagedList.class.isAssignableFrom(returnType);
    }

    /**
     * 是否Tag分页
     *
     * @return true/false
     */
    public boolean isTagPage() {
        return methodType == Query && TagPagedList.class.isAssignableFrom(returnType);
    }

    /**
     * 是否Tag分页
     *
     * @return true/false
     */
    public boolean isList() {
        return methodType == Query && Collection.class.isAssignableFrom(returnType);
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
        return new MethodMeta(entityClass, Save, args, returnType, null);
    }

    /**
     * 构造更新Action
     *
     * @param entityClass 操作表Entity类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta update(Class entityClass, ArgumentMeta... args) {
        return new MethodMeta(entityClass, Update, args, int.class, null);
    }

    /**
     * 构造count查询Action
     *
     * @param entityClass 操作表Entity类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta count(Class entityClass, ArgumentMeta... args) {
        return new MethodMeta(entityClass, Query, args, int.class, null);
    }

    /**
     * 构造单个对象查询Action
     *
     * @param entityClass 操作表Entity类型
     * @param returnType  返回的单个对象类型
     * @param args        入参
     * @return ActionMeta
     */
    public static MethodMeta findOne(Class entityClass, Class returnType, ArgumentMeta... args) {
        return new MethodMeta(entityClass, Query, args, returnType, null);
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
        return new MethodMeta(entityClass, Query, args, List.class, returnParameterType);
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
        return new MethodMeta(entityClass, Query, args, StdPagedList.class, returnParameterType);
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
        return new MethodMeta(entityClass, Query, args, TagPagedList.class, returnParameterType);
    }
}