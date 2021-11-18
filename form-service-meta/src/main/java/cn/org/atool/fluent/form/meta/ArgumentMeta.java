package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.MethodType;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * 参数信息
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "rawtypes", "unchecked"})
public class ArgumentMeta {
    /**
     * 表单项名称
     */
    public final String entryName;
    /**
     * 第index个参数
     */
    public final int index;
    /**
     * 入参是否是Collection类型
     */
    public final boolean isList;
    /**
     * 参数类型或Collection的泛型参数类
     */
    public final Class type;
    /**
     * 表单项类型
     */
    public final EntryType entryType;
    /**
     * 是否忽略空值
     */
    public final boolean ignoreNull;

    public ArgumentMeta(MethodType methodType, String entryName, EntryType type, Type argType, int index, Map types) {
        this.entryName = entryName;
        this.entryType = type == null ? EntryType.EQ : type;
        this.isList = Collection.class.isAssignableFrom(this.getRawType(argType));
        this.type = this.getArgType(methodType, argType, types);
        this.index = index;
        this.ignoreNull = true;
    }

    public ArgumentMeta(MethodType methodType, Parameter parameter, int index, Map types) {
        this.isList = Collection.class.isAssignableFrom(parameter.getType());
        this.type = this.getArgType(methodType, parameter.getParameterizedType(), types);
        Entry entry = parameter.getDeclaredAnnotation(Entry.class);
        if (entry == null) {
            this.entryName = null;
            this.ignoreNull = true;
            this.entryType = EntryType.Form;
        } else {
            this.entryName = entry.name();
            this.ignoreNull = entry.ignoreNull();
            this.entryType = entry.type();
        }
        this.index = index;
    }

    private static final Function nonConvert = obj -> obj;

    private Class getArgType(MethodType methodType, Type pType, Map<String, Object> types) {
        Class klass = this.getRawType(pType);
        if (!this.isList || methodType == null || methodType == MethodType.Query) {
            return klass;
        }
        assert pType instanceof ParameterizedType : "List types must specify generic parameters";
        Type[] pTypes = ((ParameterizedType) pType).getActualTypeArguments();
        if (pTypes.length != 1) {
            throw new IllegalArgumentException("List types must specify generic parameters");
        }
        Type type = pTypes[0];
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getRawType();
        } else if (type instanceof TypeVariable && types != null) {
            TypeVariable vType = (TypeVariable) type;
            String vName = vType.getTypeName() + ":" + vType.getGenericDeclaration().toString();
            return (Class) types.get(vName);
        } else {
            throw new IllegalArgumentException("List types must specify generic parameters");
        }
    }

    private Class getRawType(Type pType) {
        if (pType instanceof Class) {
            return (Class) pType;
        } else if (pType instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) pType).getRawType();
        } else {
            throw new IllegalArgumentException("The type of method parameter should be Class or ParameterizedType.");
        }
    }
}