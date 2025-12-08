package cn.org.atool.fluent.processor.mybatis.entity;

import cn.org.atool.generator.database.config.impl.RelationConfig;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.methodNameOfEntity;
import static cn.org.atool.generator.util.GeneratorHelper.isBlank;

/**
 * 加了@RefMethod注解的方法
 *
 * @author darui.wu
 */
@Getter
@ToString
@Accessors(chain = true)
public class EntityRefMethod extends FieldOrMethod {
    private final Map<String, String> mapping = new HashMap<>();

    /**
     * 构造函数
     *
     * @param property 属性名
     * @param javaType java类型
     */
    public EntityRefMethod(String property, TypeName javaType) {
        super(property, javaType);
    }

    /**
     * 设置映射值
     *
     * @param value 映射值
     */
    public void setValue(String value) {
        String[] pairs = value.split("&");
        for (String pair : pairs) {
            if (isBlank(pair)) {
                continue;
            }
            if (RelationConfig.isEquation(pair)) {
                String[] items = pair.split("=");
                this.mapping.put(items[0].trim(), items[1].trim());
            } else {
                mapping.clear();
                return;
            }
        }
    }

    /**
     * <pre>
     * Ref对应的方法名称
     * </pre>
     *
     * @param fluent FluentEntity
     * @return ignore
     */
    public String getRefMethod(FluentEntity fluent) {
        return methodNameOfEntity(this.name, fluent.getClassName());
    }

    /**
     * 是否自动映射
     *
     * @return true: auto mapping
     */
    public boolean isAutoMapping() {
        return !mapping.isEmpty();
    }

    /**
     * 获取返回实体类名
     *
     * @return entity class name
     */
    public String getReturnEntity() {
        if (this.javaType instanceof ClassName) {
            return ((ClassName) this.javaType).simpleName();
        } else if (this.javaType instanceof ParameterizedTypeName) {
            List<TypeName> args = ((ParameterizedTypeName) javaType).typeArguments();
            if (args.size() == 1 && args.get(0) instanceof ClassName) {
                return ((ClassName) args.get(0)).simpleName();
            }
        }
        throw new RuntimeException(
                "not support the type[" + this.javaType.toString() + "], only support return: Entity or List<Entity>.");
    }

    /**
     * 获取返回类型
     *
     * @return return type
     */
    public TypeName getReturnType() {
        if (this.javaType instanceof ClassName) {
            return this.javaType;
        } else if (this.javaType instanceof ParameterizedTypeName) {
            List<TypeName> args = ((ParameterizedTypeName) javaType).typeArguments();
            if (args.size() == 1 && args.get(0) instanceof ClassName) {
                return args.get(0);
            }
        }
        throw new RuntimeException(
                "not support the type[" + this.javaType.toString() + "], only support return: Entity or List<Entity>.");
    }

    /**
     * 是否返回List
     *
     * @return true: return list
     */
    public boolean returnList() {
        if (this.javaType instanceof ParameterizedTypeName) {
            ClassName type = ((ParameterizedTypeName) this.javaType).rawType();
            return List.class.getSimpleName().equals(type.simpleName());
        } else {
            return false;
        }
    }

    /**
     * 是否抽象方法
     *
     * @return true: 抽象方法
     */
    public boolean isAbstractMethod() {
        if (!this.isAutoMapping()) {
            return true;
        }
        FluentEntity ref = FluentList.getFluentEntity(this.getReturnEntity());
        return ref == null;
    }
}