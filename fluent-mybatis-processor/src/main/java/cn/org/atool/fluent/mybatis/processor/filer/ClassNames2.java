package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.model.ClassMap;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.WildcardTypeName;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * ClassName 定义
 */
public interface ClassNames2 extends ClassNames {

    ClassName Spring_Resource = ClassName.get(Resource.class);

    ClassName FM_IRichMapper = ClassName.get(IRichMapper.class);

    ClassName FM_MapperFactory = ClassName.get(MapperFactory.class);

    ClassName FN_FieldMapping = ClassName.get(FieldMapping.class);

    ClassName Spring_Component = ClassName.get("org.springframework.stereotype", "Component");

    ClassName Mybatis_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    ClassName Mybatis_UnknownTypeHandler = ClassName.get("org.apache.ibatis.type", "UnknownTypeHandler");

    ParameterizedTypeName CN_Map_StrObj = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    ParameterizedTypeName CN_Map_AMapping = ParameterizedTypeName.get(ClassMap.class, AMapping.class);

    ParameterizedTypeName CN_Map_Provider = ParameterizedTypeName.get(ClassMap.class, SqlProvider.class);

    ParameterizedTypeName CN_Set_ClassName = ParameterizedTypeName.get(Set.class, String.class);

    ParameterizedTypeName CN_Supplier_Str = ParameterizedTypeName.get(Supplier.class, String.class);

    ParameterizedTypeName CN_Class_IEntity = ParameterizedTypeName.get(CN_Class, WildcardTypeName.subtypeOf(FM_IEntity));

    ParameterizedTypeName CN_List_FMapping = ParameterizedTypeName.get(List.class, FieldMapping.class);

    ParameterizedTypeName CN_Optional_IMapping = ParameterizedTypeName.get(Optional.class, IMapping.class);

    ClassName CN_Long = ClassName.get(Long.class);

    ClassName CN_ClassMap = ClassName.get(ClassMap.class);

    static ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }
}