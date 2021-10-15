package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.intf.IRelation;
import cn.org.atool.fluent.mybatis.processor.entity.EntityRefMethod;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.processor.filer.FilerKit.publicMethod;
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
            .addParameter(fluent.entity(), "entity")
            .addModifiers(Modifier.PUBLIC, Modifier.DEFAULT)
            .returns(refMethod.getJavaType())
            .addJavadoc("{@link $L#$L}", fluent.getClassName(), refMethod.getName());
        String method = refMethod.returnList() ? ".to().listEntity()" : ".to().findOne().orElse(null)";
        spec.addCode("return new $T()\n", ref.query());
        int index = 0;
        for (Map.Entry<String, String> pair : refMethod.getMapping().entrySet()) {
            spec.addCode(index == 0 ? "\t.where" : "\t.and")
                .addCode(".$L().eq(entity.get$L())\n", pair.getKey(), capitalFirst(pair.getValue(), ""));
            index++;
        }
        spec.addCode("\t.end()\n", method);
        spec.addStatement("\t" + method);
        return spec.build();
    }

    private MethodSpec m_initialize() {
        MethodSpec.Builder spec = publicMethod("initialize", (TypeName) null)
            .addModifiers(Modifier.DEFAULT);
        List<CodeBlock> codes = new ArrayList<>();
        for (FluentEntity fluent : this.fluents) {
            for (EntityRefMethod method : fluent.getRefMethods()) {
                codes.add(CodeBlock.of("put(this::$L);", method.getRefMethod(fluent)));
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