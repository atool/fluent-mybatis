package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * WrapperFactory代码生成
 *
 * @author darui.wu
 */
public class WrappersFile extends AbstractFile {
    private static String firstPackage = null;

    private static String Wrappers = "Wrappers";

    public static ClassName getClassName() {
        return ClassName.get(firstPackage, Wrappers);
    }

    public WrappersFile() {
        this.packageName = firstPackage;
        this.klassName = Wrappers;
        this.comment = "应用查询器，更新器工厂类";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        for (FluentEntityInfo fluent : this.fluents) {
            builder.addField(this.m_factory(fluent));
        }
    }

    private FieldSpec m_factory(FluentEntityInfo fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix(),
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("new $T()", fluent.wrapperFactory())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }

    private static List<FluentEntityInfo> fluents = new ArrayList<>();

    public static void addFluent(FluentEntityInfo fluent) {
        fluents.add(fluent);
        if (firstPackage == null) {
            firstPackage = fluent.getBasePack();
        }
    }

    public static boolean notEmpty() {
        return !fluents.isEmpty();
    }
}