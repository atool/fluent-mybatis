package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.WildcardTypeName;

import javax.annotation.Resource;
import java.io.Serializable;
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

    ClassName Spring_Component = ClassName.get("org.springframework.stereotype", "Component");

    ClassName Mybatis_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    ParameterizedTypeName CN_Map_StrObj = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    ParameterizedTypeName CN_Map_StrStr = ParameterizedTypeName.get(Map.class, String.class, String.class);

    ParameterizedTypeName CN_List_Str = ParameterizedTypeName.get(List.class, String.class);

    ParameterizedTypeName CN_Supplier_Str = ParameterizedTypeName.get(Supplier.class, String.class);

    ParameterizedTypeName CN_Class_IEntity = ParameterizedTypeName.get(CN_Class, WildcardTypeName.subtypeOf(FM_IEntity));

    ParameterizedTypeName CN_Optional_Mapping = ParameterizedTypeName.get(Optional.class, FieldMapping.class);

    ArrayTypeName CN_SerializableArray = ArrayTypeName.of(Serializable.class);

    ClassName CN_String = ClassName.get(String.class);

    ClassName CN_Long = ClassName.get(Long.class);

    ClassName CN_Map = ClassName.get(Map.class);

    ClassName CN_Set = ClassName.get(Set.class);

    ClassName CN_List = ClassName.get(List.class);

    static ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }
}
