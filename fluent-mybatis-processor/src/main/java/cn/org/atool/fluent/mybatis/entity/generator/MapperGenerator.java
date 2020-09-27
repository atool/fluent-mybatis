package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import cn.org.atool.fluent.mybatis.entity.EntityKlass;
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
    public MapperGenerator(TypeElement curElement, EntityKlass entityKlass) {
        super(curElement, entityKlass);
        this.packageName = getPackageName(entityKlass);
        this.klassName = getClassName(entityKlass);
        this.comment = "Mapper接口";
    }

    public static String getClassName(EntityKlass entityKlass) {
        return entityKlass.getNoSuffix() + "Mapper";
    }

    public static String getPackageName(EntityKlass entityKlass) {
        return entityKlass.getPackageName("mapper");
    }

    public static ClassName className(EntityKlass entityKlass) {
        return ClassName.get(getPackageName(entityKlass), getClassName(entityKlass));
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder
            .addSuperinterface(this.superMapperClass())
            .addAnnotation(ClassName.get(Mapper.class.getPackage().getName(), Mapper.class.getSimpleName()));
        if (isNotBlank(entityKlass.getMapperBeanPrefix())) {
            builder.addAnnotation(AnnotationSpec.builder(ClassName
                .get("org.springframework.beans.factory.annotation", "Qualifier"))
                .addMember("value", "$S", entityKlass.getMapperBeanPrefix() + this.klassName)
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
            entityKlass.className()
        );
    }
}