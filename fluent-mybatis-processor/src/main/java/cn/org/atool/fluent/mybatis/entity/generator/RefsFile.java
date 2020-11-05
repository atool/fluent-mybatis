package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.EntityLazyQuery;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.JoinBuilder;
import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.entity.field.EntityRefField;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Autowired;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.CN_Getter;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * Mappers 代码生成
 *
 * @author darui.wu
 */
public class RefsFile extends AbstractFile {

    private static String Refs = "Refs";

    public static ClassName getClassName() {
        return ClassName.get(FluentEntity.getSamePackage(), Refs);
    }

    public RefsFile() {
        this.packageName = FluentEntity.getSamePackage();
        this.klassName = Refs;
        this.comment = "" +
            "\n o - 查询器，更新器工厂类单例引用" +
            "\n o - 应用所有Mapper Bean引用" +
            "\n o - Entity关联对象延迟加载查询实现";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(EntityLazyQuery.class);
        builder.addModifiers(Modifier.ABSTRACT);
        for (FluentEntity fluent : FluentEntity.getFluents()) {
            builder.addField(this.m_factory(fluent));
        }
        builder.addMethod(this.m_instance());
        for (FluentEntity fluent : FluentEntity.getFluents()) {
            builder.addField(this.m_mapper(fluent));
        }
        for (FluentEntity fluent : FluentEntity.getFluents()) {
            for (EntityRefField refField : fluent.getRefFields()) {
                builder.addMethod(this.m_refMethod(fluent, refField));
            }
        }
    }

    private MethodSpec m_refMethod(FluentEntity fluent, EntityRefField refField) {
        MethodSpec methodSpec = this.m_refRealMethod(fluent, refField);
        if (methodSpec != null) {
            return methodSpec;
        }
        return MethodSpec.methodBuilder(refField.getRefMethod(fluent))
            .addParameter(fluent.entity(), "entity")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ClassName.get(refField.getJavaType()))
            .build();
    }

    private MethodSpec m_refRealMethod(FluentEntity right, EntityRefField refField) {
        if (!refField.isAutoMapping()) {
            return null;
        }
        FluentEntity left = FluentEntity.getFluentEntity(refField.getReturnEntity());
        if (left == null) {
            return null;
        }
        String lNoSuffix = left.lowerNoSuffix();
        String rNoSuffix = right.lowerNoSuffix();

        MethodSpec.Builder spec = MethodSpec.methodBuilder(refField.getRefMethod(right))
            .addParameter(right.entity(), "entity")
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.get(refField.getJavaType()))
            .addCode("$T $LQuery = $LDefault.defaultQuery($S)\n", left.query(), lNoSuffix, lNoSuffix, lNoSuffix)
            .addCode("\t.selectAll()\n");
        int index = 0;
        for (Map.Entry<String, String> pair : refField.getMapping().entrySet()) {
            spec.addCode(index == 0 ? "\t.where" : "\t.and")
                .addCode(".$L().eq(entity.get$L())\n", pair.getValue(), capitalFirst(pair.getKey(), ""));
            index++;
        }
        spec.addStatement("\t.end()");
        spec.addStatement("$T $LQuery = $LDefault.defaultQuery($S, $LQuery)", right.query(), rNoSuffix, rNoSuffix, rNoSuffix, lNoSuffix);
        spec.addCode("$T query = $T.from($LQuery)\n", IQuery.class, JoinBuilder.class, lNoSuffix)
            .addCode("\t.join($LQuery)\n", rNoSuffix)
            .addCode("\t.on((join, l, r) -> join\n");
        for (Map.Entry<String, String> pair : refField.getMapping().entrySet()) {
            spec.addCode("\t\t.on(l.where.$L(), r.where.$L())\n", pair.getValue(), pair.getKey());
        }
        spec.addStatement(").distinct().build()");
        if (refField.returnList()) {
            spec.addStatement("return $LMapper.listEntity(query)", lNoSuffix);
        } else {
            spec.addStatement("return $LMapper.findOne(query)", lNoSuffix);
        }
        return spec.build();
    }

    private MethodSpec m_instance() {
        return MethodSpec.methodBuilder("INSTANCE")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .addJavadoc("Wrapper单例")
            .returns(RefsFile.getClassName())
            .addStatement("return ($L) $T.query()", Refs, EntityLazyQuery.class)
            .build();
    }

    private FieldSpec m_mapper(FluentEntity fluent) {
        return FieldSpec.builder(fluent.mapper(), fluent.lowerNoSuffix() + "Mapper",
            Modifier.PROTECTED)
            .addAnnotation(CN_Getter)
            .addAnnotation(CN_Autowired)
            .build();
    }

    private FieldSpec m_factory(FluentEntity fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix() + "Default",
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$T.INSTANCE", fluent.wrapperFactory())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}