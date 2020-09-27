package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.ibatis.annotations.Mapper;

import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * 生成Entity对应的Mapper类
 *
 * @author darui.wu
 */
public class MapperGenerator extends AbstractGenerator {
    public MapperGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "Mapper接口";
    }

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + "Mapper";
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName("mapper");
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder
            .addSuperinterface(this.superMapperClass())
            .addAnnotation(ClassName.get(Mapper.class.getPackage().getName(), Mapper.class.getSimpleName()));
        if (isNotBlank(fluentEntityInfo.getMapperBeanPrefix())) {
            builder.addAnnotation(AnnotationSpec.builder(ClassName
                .get("org.springframework.beans.factory.annotation", "Qualifier"))
                .addMember("value", "$S", fluentEntityInfo.getMapperBeanPrefix() + this.klassName)
                .build()
            );
        }
    }

    @Override
    protected boolean isInterface() {
        return true;
    }

    private TypeName superMapperClass() {
        return super.parameterizedType(
            ClassName.get(IEntityMapper.class),
            fluentEntityInfo.className()
        );
    }
}