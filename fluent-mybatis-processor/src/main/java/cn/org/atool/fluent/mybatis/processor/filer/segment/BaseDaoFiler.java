package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.crud.IDefaultGetter;
import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_BaseDao;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_BaseDao;
import static cn.org.atool.fluent.mybatis.processor.filer.segment.MapperFiler.getMapperName;

/**
 * BaseDaoGenerator: *BaseDao文件生成
 *
 * @author wudarui
 */
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
            .addMethod(this.m_mapper())
            .addMethod(this.m_defaults());
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
            .addAnnotation(AnnotationSpec.builder(ClassNames2.Spring_Resource)
                .addMember("name", "$S", getMapperName(fluent)).build()
            )
            .build();
    }

    /**
     * public AddressMapper mapper() {}
     *
     * @return MethodSpec
     */
    private MethodSpec m_mapper() {
        return super.publicMethod("mapper", true, fluent.mapper())
            .addStatement(super.codeBlock("return mapper"))
            .build();
    }

    private MethodSpec m_defaults() {
        return super.protectedMethod("defaults", ClassName.get(IDefaultGetter.class))
            .addStatement("return $T.MAPPING", fluent.entityMapping())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}