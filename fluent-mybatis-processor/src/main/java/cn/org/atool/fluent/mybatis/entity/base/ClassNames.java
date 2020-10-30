package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.generator.EntityHelperGenerator;
import cn.org.atool.fluent.mybatis.entity.generator.MapperGenerator;
import cn.org.atool.fluent.mybatis.entity.generator.UpdaterGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.List;
import java.util.Map;

public class ClassNames {
    public static final ClassName CN_Qualifier = ClassName.get("org.springframework.beans.factory.annotation", "Qualifier");

    public static final ClassName CN_Component = ClassName.get("org.springframework.stereotype", "Component");

    public static final ClassName CN_Mapper = ClassName.get("org.apache.ibatis.annotations", "Mapper");

    public static final ClassName CN_Autowired = ClassName.get("org.springframework.beans.factory.annotation", "Autowired");

    public static final ParameterizedTypeName CN_Map_StrObj = ParameterizedTypeName.get(Map.class, String.class, Object.class);

    public static final ParameterizedTypeName CN_Map_StrStr = ParameterizedTypeName.get(Map.class, String.class, String.class);

    public static final ParameterizedTypeName CN_List_Str = ParameterizedTypeName.get(List.class, String.class);

    public static final ClassName getClassName(String fullClassName) {
        int index = fullClassName.lastIndexOf('.');
        String packName = index < 0 ? "" : fullClassName.substring(0, index);
        String klasName = index < 0 ? fullClassName : fullClassName.substring(index + 1);
        return ClassName.get(packName, klasName);
    }

    /**
     * ClassName of XyzUpdater
     *
     * @param fluent
     * @return
     */
    public static ClassName updater(FluentEntityInfo fluent) {
        return ClassName.get(
            UpdaterGenerator.getPackageName(fluent),
            UpdaterGenerator.getClassName(fluent));
    }

    /**
     * ClassName of XyzEntityHelper
     *
     * @param fluent
     * @return
     */
    public static ClassName entityHelper(FluentEntityInfo fluent) {
        return ClassName.get(
            EntityHelperGenerator.getPackageName(fluent),
            EntityHelperGenerator.getClassName(fluent));
    }

    /**
     * ClassName of XyzMapper
     *
     * @param fluent
     * @return
     */
    public static ClassName mapper(FluentEntityInfo fluent) {
        return ClassName.get(
            MapperGenerator.getPackageName(fluent),
            MapperGenerator.getClassName(fluent));
    }
}