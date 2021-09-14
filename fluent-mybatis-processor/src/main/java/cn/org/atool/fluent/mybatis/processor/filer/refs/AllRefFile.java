package cn.org.atool.fluent.mybatis.processor.filer.refs;

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
 * Mappers 代码生成
 *
 * @author darui.wu
 */
public class AllRefFile extends AbstractFile {

    private static final String AllRef = "AllRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), AllRef);
    }

    public AllRefFile() {
        this.packageName = FluentList.refsPackage();
        this.klassName = AllRef;
        this.comment = "" +
            "\n o - 查询器，更新器工厂类单例引用" +
            "\n o - 应用所有Mapper Bean引用" +
            "\n o - Entity关联对象延迟加载查询实现";
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.skipJavaLangImports(true);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(RefFiler.getClassName())
            .addModifiers(Modifier.FINAL)
            .addMethod(this.m_assertRelation());
        for (FluentEntity fluent : FluentList.getFluents()) {
            for (EntityRefMethod refMethod : fluent.getRefMethods()) {
                if (refMethod.isAbstractMethod()) {
                    spec.addMethod(this.m_refMethod(fluent, refMethod));
                } else {
                    spec.addMethod(this.m_refRealMethod(fluent, refMethod));
                }
            }
        }
    }

    private MethodSpec m_assertRelation() {
        return MethodSpec.methodBuilder("relation")
            .addModifiers(Modifier.PRIVATE)
            .returns(EntityRelationFiler.getClassName())
            .beginControlFlow("if (relation instanceof IEntityRelation)")
            .addStatement("return (IEntityRelation)relation")
            .endControlFlow()
            .addStatement("throw new $T($S)", RuntimeException.class,
                "It must implement IEntityRelation and add the implementation to spring management.")
            .build();
    }

    private MethodSpec m_refMethod(FluentEntity fluent, EntityRefMethod refMethod) {
        String methodName = refMethod.getRefMethod(fluent);
        return MethodSpec.methodBuilder(methodName)
            .addParameter(fluent.entity(), "entity")
            .addModifiers(Modifier.PUBLIC)
            .returns(refMethod.getJavaType())
            .addJavadoc("{@link $L#$L}", fluent.getClassName(), refMethod.getName())
            .addStatement("return this.relation().$L(entity)", methodName)
            .build();
    }

    private MethodSpec m_refRealMethod(FluentEntity fluent, EntityRefMethod refMethod) {
        FluentEntity ref = FluentList.getFluentEntity(refMethod.getReturnEntity());

        MethodSpec.Builder spec = MethodSpec.methodBuilder(refMethod.getRefMethod(fluent))
            .addParameter(fluent.entity(), "entity")
            .addModifiers(Modifier.PUBLIC)
            .returns(refMethod.getJavaType())
            .addJavadoc("{@link $L#$L}", fluent.getClassName(), refMethod.getName());
        String method = refMethod.returnList() ? "listEntity" : "findOne";
        spec.addCode("return mapper().$LMapper.$L(new $T()\n", ref.lowerNoSuffix(), method, ref.query());
        int index = 0;
        for (Map.Entry<String, String> pair : refMethod.getMapping().entrySet()) {
            spec.addCode(index == 0 ? "\t.where" : "\t.and")
                .addCode(".$L().eq(entity.get$L())\n", pair.getKey(), capitalFirst(pair.getValue(), ""));
            index++;
        }
        spec.addStatement("\t.end())");
        return spec.build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}