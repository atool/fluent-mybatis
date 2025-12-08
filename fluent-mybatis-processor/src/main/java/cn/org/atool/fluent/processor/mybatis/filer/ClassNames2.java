package cn.org.atool.fluent.processor.mybatis.filer;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.processor.mybatis.AnnotationKit;
import cn.org.atool.generator.util.ClassNames;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;

import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * ClassName 定义
 */
public interface ClassNames2 extends ClassNames {

    /**
     * Spring_Resource
     */
    ClassName Spring_Resource = AnnotationKit.getResourceClass();

    /**
     * FieldMapping
     */
    ClassName FN_FieldMapping = ClassName.get(FieldMapping.class);

    /**
     * Spring_Component
     */
    ClassName Spring_Component = ClassName.get("org.springframework.stereotype", "Component");

    /**
     * Mybatis_Mapper
     */
    ClassName Mybatis_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    /**
     * Consumer<IWrapperMapper>
     */
    ParameterizedTypeName CN_Consumer_Mapper = ParameterizedTypeName.get(Consumer.class, IWrapperMapper.class);

    /**
     * List<FieldMapping>
     */
    ParameterizedTypeName CN_List_FMapping = ParameterizedTypeName.get(List.class, FieldMapping.class);

    /**
     * List<String>
     */
    ParameterizedTypeName CN_List_Str = ParameterizedTypeName.get(List.class, String.class);

    /**
     * Optional<IMapping>
     */
    ParameterizedTypeName CN_Optional_IMapping = ParameterizedTypeName.get(Optional.class, IMapping.class);

    /**
     * List
     */
    ClassName CN_List = ClassName.get(List.class);

    /**
     * Long
     */
    ClassName CN_Long = ClassName.get(Long.class);

    /**
     * 获取ClassName
     *
     * @param fullClassName 全类名
     * @return ClassName
     */
    static ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }

    /**
     * 获取TypeName
     *
     * @param var VariableElement
     * @return TypeName
     */
    static TypeName javaType(VariableElement var) {
        TypeName type = ClassName.get(var.asType());
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType();
        } else {
            return type;
        }
    }
}