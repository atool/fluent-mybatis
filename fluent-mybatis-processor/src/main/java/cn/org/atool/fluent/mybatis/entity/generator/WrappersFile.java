package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Getter;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNames.*;

/**
 * WrapperFactory代码生成
 *
 * @author darui.wu
 */
public class WrappersFile extends AbstractFile {
    /**
     * 所有entity对象的共同基础package
     */
    private static String samePackage = null;

    private static String Wrappers = "Wrappers";

    public static ClassName getClassName() {
        return ClassName.get(samePackage, Wrappers);
    }

    public WrappersFile() {
        this.packageName = samePackage;
        this.klassName = Wrappers;
        this.comment = "应用查询器，更新器工厂类";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        for (FluentEntityInfo fluent : this.fluents) {
            builder.addField(this.m_factory(fluent));
        }
//        for (FluentEntityInfo fluent : this.fluents) {
//            builder.addField(this.m_mapper(fluent));
//        }
    }

    private MethodSpec m_instance() {
        return MethodSpec.methodBuilder("instance")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(getClassName())
            .addCode("if(INSTANCE==null){\n")
            .addStatement("\tthrow new $T($S)", RuntimeException.class, "the Wrapper must be defined as a spring bean.")
            .addCode("}\n")
            .addStatement("return INSTANCE")
            .build();
    }

    private FieldSpec m_mapper(FluentEntityInfo fluent) {
        return FieldSpec.builder(fluent.mapper(), fluent.lowerNoSuffix() + "Mapper", Modifier.PRIVATE)//, Modifier.FINAL
            .addAnnotation(CN_Getter)
            .addAnnotation(CN_Autowired)
            .build();
    }

    private FieldSpec m_factory(FluentEntityInfo fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix(),
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

    @Getter
    private static List<FluentEntityInfo> fluents = new ArrayList<>();

    public static void addFluent(FluentEntityInfo fluent) {
        fluents.add(fluent);
        samePackage = sameStartPackage(samePackage, fluent.getBasePack());
    }

    public static boolean notEmpty() {
        return !fluents.isEmpty();
    }

    /**
     * 返回base1和base2的共同package路径
     *
     * @param base1
     * @param base2
     * @return
     */
    static String sameStartPackage(String base1, String base2) {
        if (base1 == null || base2 == null) {
            return base1 == null ? base2 : base1;
        }
        String base = base1;
        while (!base2.startsWith(base)) {
            int last = base.lastIndexOf('.');
            base = base.substring(0, last);
        }
        return base;
    }
}