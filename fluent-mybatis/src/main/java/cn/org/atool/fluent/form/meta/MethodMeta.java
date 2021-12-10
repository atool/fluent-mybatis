package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.kits.MethodArgNamesKit;
import cn.org.atool.fluent.form.meta.entry.ArgEntryMeta;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.LockKit;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static cn.org.atool.fluent.form.annotation.MethodType.*;
import static cn.org.atool.fluent.form.kits.ParameterizedTypeKit.notFormObject;
import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * FormService方法元数据
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unused"})
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

    public final MethodArgNames argNames;
    /**
     * 入参类型
     */
    public final ArgumentMeta[] argMetas;

    /**
     * 返回值类型
     */
    public final Class returnType;
    /**
     * 返回值泛型类型(如果有)
     */
    public final Class returnParameterType;

    private MethodMeta(Class entityClass, Method method, MethodType methodType, ArgumentMeta[] metas, Class returnType, Class returnParameterType) {
        this.entityClass = entityClass;
        this.method = method == null ? UUID.randomUUID() + "()" : method.toString();
        this.methodType = methodType;
        this.argMetas = metas;
        this.returnType = returnType;
        this.returnParameterType = returnParameterType;
        this.argNames = null;
    }

    private MethodMeta(Class entityClass, Method method) {
        this.entityClass = entityClass;
        this.method = method.toString();
        this.returnType = method.getReturnType();
        this.returnParameterType = this.getParameterTypeOfReturn(method);
        this.argNames = MethodArgNamesKit.parseMethodStyle(method.getName());
        this.methodType = parseMethodType(method);
        this.argMetas = this.buildArgumentMeta(argNames, method.getParameters());
    }

    private static MethodType parseMethodType(Method method) {
        FormMethod aMethod = method.getDeclaredAnnotation(FormMethod.class);
        if (aMethod != null && aMethod.type() != Auto) {
            return aMethod.type();
        }
        String name = method.getName();
        for (MethodType type : autos) {
            if (type.match(name)) {
                return type;
            }
        }
        return Query;
    }

    private static final List<MethodType> autos = Arrays.asList(Delete, LogicDelete, Save, Update);

    /**
     * 按 Method.toString() 签名进行加锁
     */
    private final static LockKit<String> MethodLock = new LockKit<>(16);

    public static final KeyMap<EntryMetas> MethodArgsMeta = new KeyMap<>();

    /**
     * 构造MethodMeta
     * 缓存在 {@link cn.org.atool.fluent.form.IMethodAround#cache(Class, Method)} 中处理
     */
    public static MethodMeta meta(Class entityClass, Method method) {
        return new MethodMeta(entityClass, method);
    }

    /**
     * 显式构造MethodMeta
     * 缓存在 {@link cn.org.atool.fluent.form.IMethodAround#cache(Class, Method)} 中处理
     */
    public static MethodMeta meta(Class entityClass, Method method, MethodType methodType, ArgumentMeta[] args, Class returnType, Class returnParameterType) {
        return new MethodMeta(entityClass, method, methodType, args, returnType, returnParameterType);
    }

    private ArgumentMeta[] buildArgumentMeta(MethodArgNames names, Parameter[] parameters) {
        ArgumentMeta[] args = new ArgumentMeta[parameters.length];
        int index = 0;
        for (int i = 0; i < parameters.length; i++) {
            String name = names.get(index);
            if (name == null) {
                args[i] = new ArgumentMeta(methodType, parameters[i], null, i, null);
            } else {
                args[i] = new ArgumentMeta(methodType, parameters[i], name, i, null);
                if (Objects.equals(name, args[i].entryName)) {
                    index++;
                }
            }
        }
        return args;
    }

    /* ============================ ***/

    /**
     * 构造入参的元数据列表
     *
     * @return FormMetas
     */
    public EntryMetas metas() {
        if (isBlank(method)) {
            return this.buildMetasFromArgs();
        } else {
            MethodLock.lockDoing(MethodArgsMeta::containsKey, method, () -> MethodArgsMeta.put(method, this.buildMetasFromArgs()));
            return MethodArgsMeta.get(method);
        }
    }

    private EntryMetas buildMetasFromArgs() {
        this.validate();
        EntryMetas argsMetas = new EntryMetas(null, this.isAnd());
        for (ArgumentMeta arg : this.argMetas) {
            if (notFormObject(arg.type)) {
                argsMetas.addMeta(new ArgEntryMeta(arg));
            } else {
                EntryMetas entryMetas = EntryMetas.getFormMeta(arg.type);
                for (IEntryMeta entryMeta : entryMetas.allMetas()) {
                    EntryMeta meta = (EntryMeta) entryMeta;
                    if (meta.getter != null) {
                        argsMetas.addMeta(new ArgEntryMeta(arg, meta));
                    }
                }
            }
        }
        return argsMetas;
    }

    public boolean isAnd() {
        return this.argNames == null || this.argNames.isAnd;
    }

    private void validate() {
        if (this.entityClass == null) {
            throw new IllegalArgumentException("Annotation[@Action] must be declared on method[" + method + "].");
        }
        if (this.argMetas == null || this.argMetas.length == 0) {
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

    public boolean isReturnNumber() {
        return isReturnInt() || isReturnLong();
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
     * 结果值是布尔型
     *
     * @return true/false
     */
    public boolean isReturnBool() {
        return returnType == Boolean.class || returnType == boolean.class;
    }

    /**
     * 结果值是列表
     *
     * @return true/false
     */
    public boolean isReturnList() {
        return Collection.class.isAssignableFrom(returnType);
    }

    /**
     * 无返回值
     *
     * @return true/false
     */
    public boolean isReturnVoid() {
        return returnType == void.class || returnType == Void.class;
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
     * 是否是单参，并且参数是Collection类型
     *
     * @return true/false
     */
    public boolean isOneArgArr() {
        return this.argMetas.length == 1 && this.argMetas[0].isArray;
    }

    /**
     * 是否是单参，并且参数是Collection类型
     *
     * @return true/false
     */
    public boolean isOneArgListOrArray() {
        ArgumentMeta meta = this.argMetas[0];
        return this.argMetas.length == 1 && meta.entryType == EntryType.Form && (meta.isList || meta.isArray);
    }

    /**
     * 查询数据接口
     */
    public boolean isQuery() {
        return methodType == null || methodType == Query;
    }

    /**
     * 更新数据接口
     */
    public boolean isUpdate() {
        return methodType == Update;
    }

    /**
     * 创建实例接口
     */
    public boolean isSave() {
        return methodType == Save;
    }

    /**
     * 物理删除接口
     */
    public boolean isDelete() {
        return methodType == Delete;
    }

    /**
     * 逻辑删除接口
     */
    public boolean isLogicDelete() {
        return methodType == LogicDelete;
    }
}