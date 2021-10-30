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

import static cn.org.atool.fluent.form.annotation.MethodType.Query;
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
            MethodMetaFunc getter = method -> method.args[index].value;
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
        MethodMetaFunc getter = method -> {
            Object object = method.args[index].value;
            if (meta.getter == null) {
                throw new IllegalStateException("getter of EntryName[" + meta.name + "] not found.");
            } else {
                return meta.getter.apply(object);
            }
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

    public static MethodMeta meta(Class entityClass, MethodType methodType, ArgumentMeta[] args, Class returnType, Class returnParameterType) {
        return new MethodMeta(entityClass, methodType, args, returnType, returnParameterType);
    }
}