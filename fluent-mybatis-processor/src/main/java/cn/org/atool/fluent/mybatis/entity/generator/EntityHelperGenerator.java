package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IEntityHelper;
import cn.org.atool.fluent.mybatis.base.model.EntityToMap;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.ClassNames;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Map_StrObj;
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

    public EntityHelperGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "Entity帮助类";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(ClassNames.mapping(fluent), "*");
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder
            .addSuperinterface(IEntityHelper.class)
            .addMethod(this.m_toColumnMap())
            .addMethod(this.m_toEntityMap())
            .addMethod(this.m_toMap())
            .addMethod(this.m_toEntity())
            .addMethod(this.m_copy());
    }

    private MethodSpec m_toColumnMap() {
        return super.publicMethod("toColumnMap", true, CN_Map_StrObj)
            .addParameter(IEntity.class, "entity")
            .addStatement("return this.toMap(($T)entity, false)", fluent.className())
            .build();
    }

    private MethodSpec m_toEntityMap() {
        return super.publicMethod("toEntityMap", true, CN_Map_StrObj)
            .addParameter(IEntity.class, "entity")
            .addStatement("return this.toMap(($T)entity, true)", fluent.className())
            .build();
    }

    /**
     * public static Map<String, Object> toEntityMap(Entity entity)
     *
     * @return
     */
    private MethodSpec m_toMap() {
        MethodSpec.Builder builder = super.publicMethod("toMap", false, CN_Map_StrObj)
            .addParameter(fluent.className(), "entity")
            .addParameter(TypeName.BOOLEAN, "isProperty")
            .addCode("return new $T(isProperty)\n", EntityToMap.class);
        for (FieldColumn fc : fluent.getFields()) {
            String getMethod = fc.getMethodName();
            builder.addCode("\t.put($L, entity.$L())\n", fc.getProperty(), getMethod);
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