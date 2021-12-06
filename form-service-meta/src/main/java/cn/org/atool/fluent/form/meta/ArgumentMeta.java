package cn.org.atool.fluent.form.meta;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.MethodType;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static cn.org.atool.fluent.form.kits.ParameterizedTypeKit.notFormObject;
import static cn.org.atool.fluent.mybatis.If.notBlank;

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

    public ArgumentMeta(MethodType methodType, Parameter parameter, String defaultName, int index, Map types) {
        this.isList = Collection.class.isAssignableFrom(parameter.getType());
        this.type = this.getArgType(methodType, parameter.getParameterizedType(), types);
        Entry entry = parameter.getDeclaredAnnotation(Entry.class);
        if (entry == null) {
            this.entryName = this.findEntryName(parameter, null, defaultName);
            this.ignoreNull = true;
            this.entryType = notFormObject(this.type) ? EntryType.EQ : EntryType.Form;
            /* 未声明@Entry注解 && 没有从方法中解析出变量名称 && 非Form复杂类型 */
            if (this.entryType == EntryType.EQ && defaultName == null) {
                throw new IllegalStateException("Unable to resolve parameter[index=" + index + "] name of method[" + parameter.getDeclaringExecutable().toString() + "].");
            }
        } else {
            this.entryName = this.findEntryName(parameter, entry.value(), defaultName);
            this.ignoreNull = entry.ignoreNull();
            this.entryType = entry.type();
        }
        this.index = index;
    }

    private String findEntryName(Parameter parameter, String entryName, String defaultName) {
        if (notBlank(entryName)) {
            return entryName;
        }
        Param param = parameter.getDeclaredAnnotation(Param.class);
        if (param != null && notBlank(param.value())) {
            return param.value();
        } else {
            return defaultName;
        }
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