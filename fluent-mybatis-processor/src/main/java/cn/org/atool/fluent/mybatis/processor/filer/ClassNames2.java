package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.spring.IMapperFactory;
import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * ClassName 定义
 */
public interface ClassNames2 extends ClassNames {

    ClassName Spring_Resource = ClassName.get(Resource.class);

    ClassName FM_IRichMapper = ClassName.get(IRichMapper.class);

    ClassName FM_MapperFactory = ClassName.get(IMapperFactory.class);

    ClassName FN_FieldMapping = ClassName.get(FieldMapping.class);

    ClassName Spring_Component = ClassName.get("org.springframework.stereotype", "Component");

    ClassName Mybatis_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    ParameterizedTypeName CN_Map_AMapping = ParameterizedTypeName.get(KeyMap.class, AMapping.class);

    ParameterizedTypeName CN_Set_ClassName = ParameterizedTypeName.get(Set.class, String.class);

    ParameterizedTypeName CN_Supplier_Str = ParameterizedTypeName.get(Supplier.class, String.class);

    ParameterizedTypeName CN_Consumer_Mapper = ParameterizedTypeName.get(Consumer.class, IWrapperMapper.class);

    ParameterizedTypeName CN_List_FMapping = ParameterizedTypeName.get(List.class, FieldMapping.class);

    ParameterizedTypeName CN_List_Str = ParameterizedTypeName.get(List.class, String.class);

    ParameterizedTypeName CN_Optional_IMapping = ParameterizedTypeName.get(Optional.class, IMapping.class);

    ClassName CN_Long = ClassName.get(Long.class);

    ClassName CN_ClassMap = ClassName.get(KeyMap.class);

    static ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }
}