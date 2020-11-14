package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.entity.IEntityHelper;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * EntityHelperRef代码生成构造
 *
 * @author wudarui
 */
public class EntityHelperRefFiler extends AbstractFile {
    private static String EntityHelperRef = "EntityHelperRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), EntityHelperRef);
    }

    public EntityHelperRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = EntityHelperRef;
        this.comment = "所有EntityHelper构造";
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addField(this.f_entityHelper())
            .addMethod(this.m_entityHelper())
            .addMethod(this.m_initEntityHelper());
    }

    private FieldSpec f_entityHelper() {
        return FieldSpec.builder(parameterizedType(Map.class, Class.class, IEntityHelper.class), "EntityHelpers",
            Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL)
            .initializer("initEntityHelper()")
            .build();
    }

    private MethodSpec m_entityHelper() {
        return MethodSpec.methodBuilder("entityHelper")
            .addParameter(Class.class, "clazz")
            .returns(IEntityHelper.class)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .addStatement("return EntityHelpers.get(clazz)")
            .build();
    }


    private MethodSpec m_initEntityHelper() {
        MethodSpec.Builder spec = MethodSpec.methodBuilder("initEntityHelper")
            .addModifiers(Modifier.FINAL, Modifier.PRIVATE, Modifier.STATIC)
            .returns(parameterizedType(Map.class, Class.class, IEntityHelper.class))
            .addStatement("Map<Class, IEntityHelper> map = new $T<>()", HashMap.class);
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addStatement("map.put($T.class, new $T())", fluent.entity(), fluent.entityHelper());
        }
        return spec.addStatement("return map").build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}
