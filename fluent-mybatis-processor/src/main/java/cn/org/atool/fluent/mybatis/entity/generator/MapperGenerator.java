package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.IEntityMapper;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.ClassNameConst;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;
import org.apache.ibatis.annotations.InsertProvider;

import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.Pack_Mapper;
import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.Suffix_Mapper;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isBlank;

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
        return fluentEntityInfo.getNoSuffix() + Suffix_Mapper;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Mapper);
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addSuperinterface(this.superMapperClass()).addAnnotation(ClassNameConst.Mapper);
        builder.addAnnotation(AnnotationSpec.builder(ClassNameConst.Qualifier)
            .addMember("value", "$S", getMapperName(this.fluentEntityInfo)).build()
        );
        builder.addMethod(this.m_insert());
    }

    public MethodSpec m_insert() {
        return super.publicOverrideAbstractMethod("insert")
            .addParameter(fluentEntityInfo.className(), "entity")
            .returns(TypeName.INT)
            .addAnnotation(AnnotationSpec.builder(InsertProvider.class)
                .addMember("type", "$T.class", SqlProviderGenerator.className(fluentEntityInfo))
                .addMember("method", "$S", "insert")
                .build())
            .build();
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

    /**
     * 返回对应的Mapper Bean名称
     *
     * @param fluentEntityInfo
     * @return
     */
    public static String getMapperName(FluentEntityInfo fluentEntityInfo) {
        String className = fluentEntityInfo.getNoSuffix() + Suffix_Mapper;
        if (isBlank(fluentEntityInfo.getMapperBeanPrefix())) {
            return MybatisUtil.lowerFirst(className, "");
        } else {
            return fluentEntityInfo.getMapperBeanPrefix() + className;
        }
    }
}