package cn.org.atool.fluent.processor.mybatis.filer;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import javax.annotation.Resource;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * ClassName 定义
 */
public interface ClassNames2 extends ClassNames {

    ClassName Spring_Resource = ClassName.get(Resource.class);

    ClassName FN_FieldMapping = ClassName.get(FieldMapping.class);

    ClassName Spring_Component = ClassName.get("org.springframework.stereotype", "Component");

    ClassName Mybatis_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    ParameterizedTypeName CN_Consumer_Mapper = ParameterizedTypeName.get(Consumer.class, IWrapperMapper.class);

    ParameterizedTypeName CN_List_FMapping = ParameterizedTypeName.get(List.class, FieldMapping.class);

    ParameterizedTypeName CN_List_Str = ParameterizedTypeName.get(List.class, String.class);

    ParameterizedTypeName CN_Optional_IMapping = ParameterizedTypeName.get(Optional.class, IMapping.class);

    ClassName CN_List = ClassName.get(List.class);

    ClassName CN_Long = ClassName.get(Long.class);

    static ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }

    static TypeName javaType(VariableElement var) {
        TypeName type = ClassName.get(var.asType());
        if (type instanceof ParameterizedTypeName) {
            return ((ParameterizedTypeName) type).rawType;
        } else {
            return type;
        }
    }
}