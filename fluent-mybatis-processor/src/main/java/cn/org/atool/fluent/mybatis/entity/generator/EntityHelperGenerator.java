package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IEntityHelper;
import cn.org.atool.fluent.mybatis.base.model.EntityToMap;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_EntityHelper;

/**
 * EntityHelper类代码生成
 *
 * @author wudarui
 */
public class EntityHelperGenerator extends AbstractGenerator {
    public static String getClassName(FluentEntityInfo fluent) {
        return fluent.getClassName() + Suffix_EntityHelper;
    }

    public static String getPackageName(FluentEntityInfo fluent) {
        return fluent.getEntityPack();
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    public EntityHelperGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "Entity帮助类";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(MappingGenerator.className(fluent), "*");
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder
            .addSuperinterface(IEntityHelper.class)
            .addMethod(this.m_toMap(true))
            .addMethod(this.m_toMap(false))
            .addMethod(this.m_toEntity())
            .addMethod(this.m_copy());
    }

    /**
     * public static Map<String, Object> toEntityMap(Entity entity)
     *
     * @return
     */
    private MethodSpec m_toMap(boolean isProperty) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(isProperty ? "toEntityMap" : "toColumnMap")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(IEntity.class, "entity")
            .addAnnotation(Override.class)
            .returns(this.parameterizedType(Map.class, String.class, Object.class))
            .addStatement("$T et = ($T) entity", fluent.className(), fluent.className())
            .addCode("return new $T($L)\n", EntityToMap.class, isProperty);
        for (FieldColumn fc : fluent.getFields()) {
            String getMethod = fc.getMethodName();
            builder.addCode("\t.put($L, et.$L())\n", fc.getProperty(), getMethod);
        }
        return builder.addCode("\t.getMap();").build();
    }

    /**
     * public static Entity entity(Map<String, Object> map)
     *
     * @return
     */
    private MethodSpec m_toEntity() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("toEntity")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(this.parameterizedType(Map.class, String.class, Object.class), "map")
            .addTypeVariable(TypeVariableName.get("E", IEntity.class))
            .returns(TypeVariableName.get("E"))
            .addAnnotation(Override.class)
            .addStatement("$T entity = new $T()", fluent.className(), fluent.className());
        for (FieldColumn fc : fluent.getFields()) {
            String setMethod = fc.setMethodName();

            builder.addCode("if (map.containsKey($L.name)) {\n", fc.getProperty());
            builder.addCode("\tentity.$L(($T) map.get($L.name));\n",
                setMethod, fc.getJavaType(), fc.getProperty());
            builder.addCode("}\n");
        }
        return builder.addStatement("return (E)entity").build();
    }

    /**
     * public static Entity copy(Entity entity)
     *
     * @return
     */
    private MethodSpec m_copy() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("copy")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(IEntity.class, "iEntity")
            .returns(fluent.className())
            .addAnnotation(Override.class)
            .addStatement("$T entity = ($T) iEntity", fluent.className(), fluent.className())
            .addStatement("$T copy = new $T()", fluent.className(), fluent.className());
        builder.addCode("{\n");
        for (FieldColumn fc : fluent.getFields()) {
            builder.addStatement("\tcopy.$L(entity.$L())", fc.setMethodName(), fc.getMethodName());
        }
        builder.addCode("}\n");
        return builder.addStatement("return copy").build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}