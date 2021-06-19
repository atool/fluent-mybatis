package cn.org.atool.fluent.mybatis.processor.filer;

import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.WildcardTypeName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.squareup.javapoet.ClassName.get;

/**
 * ClassName 定义
 */
public interface ClassNames2 extends ClassNames {
    ClassName FM_IRichMapper = get("cn.org.atool.fluent.mybatis.base.mapper", "IRichMapper");

    ClassName Spring_Component = ClassName.get("org.springframework.stereotype", "Component");

    ClassName Spring_Resource = ClassName.get("javax.annotation", "Resource");

    ClassName Spring_PostConstruct = ClassName.get("javax.annotation", "PostConstruct");

    ClassName Spring_BeanFactory = ClassName.get("org.springframework.beans.factory", "BeanFactory");

    ClassName Mybatis_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    ParameterizedTypeName CN_Map_StrObj = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    ParameterizedTypeName CN_Map_StrStr = ParameterizedTypeName.get(Map.class, String.class, String.class);

    ParameterizedTypeName CN_List_Str = ParameterizedTypeName.get(List.class, String.class);

    ParameterizedTypeName CN_Class_IEntity = ParameterizedTypeName.get(CN_Class, WildcardTypeName.subtypeOf(FM_IEntity));

    ArrayTypeName CN_SerializableArray = ArrayTypeName.of(Serializable.class);

    ClassName CN_String = ClassName.get(String.class);

    ClassName CN_Map = ClassName.get(Map.class);

    ClassName CN_Set = ClassName.get(Set.class);

    ClassName CN_HashMap = ClassName.get(HashMap.class);

    ClassName CN_List = ClassName.get(List.class);

    static ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }
}
