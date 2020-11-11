package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * IWrapperDefaultRef 构造
 *
 * @author darui.wu
 */
public class IWrapperDefaultRefFiler extends AbstractFile {
    private static String IWrapperDefault = "IWrapperDefaultRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), IWrapperDefault);
    }

    public IWrapperDefaultRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = IWrapperDefault;
        this.comment = "更新器工厂类单例引用";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        for (FluentEntity fluent : FluentList.getFluents()) {
            builder.addField(this.f_factory(fluent));
        }
    }

    private FieldSpec f_factory(FluentEntity fluent) {
        return FieldSpec.builder(fluent.wrapperFactory(), fluent.lowerNoSuffix() + "Default",
            Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer("$T.INSTANCE", fluent.wrapperFactory())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}