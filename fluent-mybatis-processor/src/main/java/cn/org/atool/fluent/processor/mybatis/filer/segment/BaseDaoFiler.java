package cn.org.atool.fluent.processor.mybatis.filer.segment;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.processor.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.processor.mybatis.filer.AbstractFiler;
import cn.org.atool.fluent.processor.mybatis.filer.ClassNames2;
import cn.org.atool.fluent.processor.mybatis.filer.FilerKit;
import com.squareup.javapoet.*;

import javax.annotation.PostConstruct;
import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_BaseDao;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_BaseDao;
import static cn.org.atool.fluent.processor.mybatis.filer.segment.MapperFiler.getMapperName;

/**
 * BaseDaoGenerator: *BaseDao文件生成
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
public class BaseDaoFiler extends AbstractFiler {
    public BaseDaoFiler(FluentEntity fluentEntity) {
        super(fluentEntity);
        this.packageName = fluentEntity.getPackageName(Pack_BaseDao);
        this.klassName = fluentEntity.getNoSuffix() + Suffix_BaseDao;
    }

    @Override
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(fluent.entityMapping(), "MAPPING");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addModifiers(Modifier.ABSTRACT)
            .superclass(paraType(BaseDao.class, fluent.entity(), fluent.query(), fluent.updater()));

        builder.addField(this.f_mapper())
            .addField(this.f_instance())
            .addMethod(this.m_initInstance())
            .addMethod(this.m_mapper())
            .addMethod(this.m_defaults())
            .addMethod(this.m_setMapper());
    }

    /**
     * protected EntityMapper mapper;
     * <pre>
     * 从 @Autowired + @Qualifier 改为 @Resource
     * 方便未依赖spring环境使用
     * </pre>
     *
     * @return FieldSpec
     */
    private FieldSpec f_mapper() {
        return FieldSpec.builder(fluent.mapper(), "mapper")
            .addModifiers(Modifier.PROTECTED)
            .build();
    }

    private FieldSpec f_instance() {
        return FieldSpec.builder(fluent.mapper(), "INSTANCE")
            .addModifiers(Modifier.PROTECTED, Modifier.STATIC)
            .build();
    }

    private MethodSpec m_initInstance() {
        return FilerKit.protectMethod("initInstance", Void.class)
            .addAnnotation(PostConstruct.class)
            .addStatement(super.codeBlock("INSTANCE = this"))
            .build();
    }

    private MethodSpec m_setMapper() {
        return FilerKit.publicMethod("setMapper", (Class) null)
            .addAnnotation(AnnotationSpec.builder(ClassNames2.Spring_Resource)
                .addMember("name", "$S", getMapperName(fluent)).build()
            )
            .addParameter(ParameterizedTypeName.get(ClassName.get(IMapper.class), fluent.entity()), "mapper")
            .addStatement("this.mapper = ($T)mapper", fluent.mapper())
            .build();
    }

    /**
     * public AddressMapper mapper() {}
     *
     * @return MethodSpec
     */
    private MethodSpec m_mapper() {
        return FilerKit.publicMethod("mapper", fluent.mapper())
            .addStatement(super.codeBlock("return mapper"))
            .build();
    }

    private MethodSpec m_defaults() {
        return FilerKit.protectMethod("defaults", ClassName.get(IDefaultGetter.class))
            .addStatement("return $T.MAPPING", fluent.entityMapping())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}