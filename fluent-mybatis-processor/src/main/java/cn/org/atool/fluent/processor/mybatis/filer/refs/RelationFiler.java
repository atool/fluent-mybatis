package cn.org.atool.fluent.processor.mybatis.filer.refs;

import cn.org.atool.fluent.mybatis.base.EntityRefKit;
import cn.org.atool.fluent.mybatis.base.intf.IRelation;
import cn.org.atool.fluent.processor.mybatis.entity.EntityRefMethod;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.processor.mybatis.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.processor.mybatis.filer.ClassNames2.CN_List;
import static cn.org.atool.fluent.processor.mybatis.filer.FilerKit.publicMethod;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * IEntityRelation 接口类生成
 *
 * @author darui.wu
 */
public class RelationFiler extends AbstractFile {
    private static final String EntityRelation = "IEntityRelation";

    private final List<FluentEntity> fluents;

    public RelationFiler(String basePackage, List<FluentEntity> fluents) {
        this.packageName = basePackage;
        this.fluents = fluents;
        this.klassName = EntityRelation;
        this.comment = "实体类间自定义的关联关系接口";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(RefKit.class, "put");
        builder.addStaticImport(EntityRefKit.class, "values");
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addSuperinterface(IRelation.class);
        for (FluentEntity fluent : this.fluents) {
            for (EntityRefMethod method : fluent.getRefMethods()) {
                spec.addMethod(this.buildMethod(fluent, method));
            }
        }
        spec.addMethod(this.m_initialize());
    }

    private MethodSpec buildMethod(FluentEntity fluent, EntityRefMethod method) {
        if (method.isAbstractMethod()) {
            return this.m_abstractMethod(fluent, method);
        } else {
            return this.m_defaultMethod(fluent, method);
        }
    }

    private MethodSpec m_abstractMethod(FluentEntity fluent, EntityRefMethod refField) {
        return MethodSpec.methodBuilder(refField.getRefMethod(fluent))
            .addParameter(fluent.entity(), "entity")
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(refField.getJavaType())
            .addJavadoc("{@link $L#$L()}", fluent.getClassName(), refField.getName())
            .build();
    }

    private MethodSpec m_defaultMethod(FluentEntity fluent, EntityRefMethod refMethod) {
        FluentEntity ref = FluentList.getFluentEntity(refMethod.getReturnEntity());

        MethodSpec.Builder spec = MethodSpec.methodBuilder(refMethod.getRefMethod(fluent))
            .returns(ParameterizedTypeName.get(CN_List, refMethod.getReturnType()))
            .addParameter(ParameterizedTypeName.get(CN_List, fluent.entity()), "entities")
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .addJavadoc("{@link $L#$L()}", fluent.getClassName(), refMethod.getName());

        spec.addCode("return new $T()\n", ref.query());
        int index = 0;
        for (Map.Entry<String, String> entry : refMethod.getMapping().entrySet()) {
            spec.addCode(index == 0 ? "\t.where" : "\t.and")
                .addCode(".$L().in(values(entities, $T::get$L))\n",
                    entry.getKey(), fluent.entity(), capitalFirst(entry.getValue()));
            index++;
        }
        spec.addStatement("\t.end().to().listEntity()");
        return spec.build();
    }

    private MethodSpec m_initialize() {
        MethodSpec.Builder spec = publicMethod("initialize", (TypeName) null)
            .addModifiers(Modifier.DEFAULT);
        List<CodeBlock> codes = new ArrayList<>();
        for (FluentEntity fluent : this.fluents) {
            for (EntityRefMethod method : fluent.getRefMethods()) {
                if (method.isAbstractMethod()) {
                    codes.add(CodeBlock.of("put(this::$L);", method.getRefMethod(fluent)));
                } else {
                    codes.add(CodeBlock.of("put($T.class, $S, this::$L);", fluent.entity(), method.getName(), method.getRefMethod(fluent)));
                }
            }
        }
        spec.addCode(CodeBlock.join(codes, "\n"));
        return spec.build();
    }

    @Override
    protected boolean isInterface() {
        return true;
    }
}