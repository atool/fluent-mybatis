package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IRelation;
import cn.org.atool.fluent.mybatis.processor.entity.EntityRefMethod;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.capitalFirst;

/**
 * IEntityRelation 接口类生成
 *
 * @author darui.wu
 */
public class EntityRelationFiler extends AbstractFile {
    private static final String EntityRelation = "IEntityRelation";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), EntityRelation);
    }

    public EntityRelationFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = EntityRelation;
        this.comment = "实体类间自定义的关联关系接口";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addSuperinterface(IRelation.class);
        for (FluentEntity fluent : FluentList.getFluents()) {
            for (EntityRefMethod method : fluent.getRefMethods()) {
                spec.addMethod(this.buildMethod(fluent, method));
            }
        }
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
            .addJavadoc("{@link $L#$L}", fluent.getClassName(), refField.getName())
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

    @Override
    protected boolean isInterface() {
        return true;
    }
}