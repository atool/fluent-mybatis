package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import cn.org.atool.fluent.mybatis.processor.filer.FilerKit;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.*;
import org.apache.ibatis.annotations.CacheNamespace;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_Consumer_Mapper;
import static cn.org.atool.fluent.mybatis.processor.filer.ClassNames2.CN_List_Str;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * 生成Entity对应的Mapper类
 *
 * @author darui.wu
 */
public class MapperFiler extends AbstractFiler {

    public MapperFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = getPackageName(fluentEntity);
        this.klassName = getClassName(fluentEntity);
        this.comment = "Mapper接口";
    }

    public static String getClassName(FluentClassName fluentEntity) {
        return fluentEntity.getNoSuffix() + Suffix_Mapper;
    }

    public static String getPackageName(FluentClassName fluentEntity) {
        return fluentEntity.getPackageName(Pack_Mapper);
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(fluent.entityMapping(), Suffix_MAPPING);
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.addSuperinterface(paraType(ClassName.get(IWrapperMapper.class), fluent.entity(), fluent.query(), fluent.updater()))
            .addSuperinterface(paraType(ClassNames2.getClassName(fluent.getSuperMapper()), fluent.entity()))
            .addAnnotation(ClassNames2.Mybatis_Mapper)
            .addAnnotation(AnnotationSpec.builder(ClassNames2.Spring_Component)
                .addMember("value", "$S", getMapperName(this.fluent)).build()
            );
        if (fluent.isUsedCached()) {
            spec.addAnnotation(AnnotationSpec.builder(CacheNamespace.class)
                .addMember("blocking", "true").build()
            );
        }
        spec.addMethod(this.m_mapping())
            .addMethod(this.m_print());
    }

    @Override
    protected MethodSpec m_mapping() {
        return FilerKit.publicMethod(Suffix_mapping, IMapping.class)
            .addModifiers(Modifier.DEFAULT, Modifier.PUBLIC)
            .addStatement("return $L", Suffix_MAPPING)
            .build();
    }

    private MethodSpec m_print() {
        return FilerKit.publicMethod("print", false, CN_List_Str)
            .addModifiers(Modifier.STATIC)
            .addParameter(int.class, "mode")
            .addParameter(CN_Consumer_Mapper, "simulators")
            .addStatement("return $T.print(mode, MAPPING, simulators)", PrinterMapper.class)
            .build();
    }

    /**
     * 返回对应的Mapper Bean名称
     *
     * @param fluentEntity 实体原数据
     * @return mapper bean name
     */
    public static String getMapperName(FluentEntity fluentEntity) {
        String className = fluentEntity.getNoSuffix() + Suffix_Mapper;
        if (isBlank(fluentEntity.getMapperBeanPrefix())) {
            return MybatisUtil.lowerFirst(className, EMPTY);
        } else {
            return fluentEntity.getMapperBeanPrefix() + className;
        }
    }

    @Override
    protected boolean isInterface() {
        return true;
    }
}