package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.generator.util.ClassNames;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_BaseDao;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_BaseDao;
import static cn.org.atool.fluent.mybatis.processor.base.MethodName.*;
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
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(fluent.wrapperFactory(), "INSTANCE");
        super.staticImport(builder);
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addModifiers(Modifier.ABSTRACT)
            .superclass(this.superBaseDaoImplKlass());

        builder.addField(this.f_mapper())
            .addMethod(this.m_mapper())
            .addMethod(this.m_newQuery())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_newUpdater())
            .addMethod(this.m_defaultUpdater());
    }

    private TypeName superBaseDaoImplKlass() {
        ClassName baseImpl = ClassName.get(BaseDao.class.getPackage().getName(), BaseDao.class.getSimpleName());
        ClassName entity = fluent.entity();
        return ParameterizedTypeName.get(baseImpl, entity);
    }

    /**
     * protected EntityMapper mapper;
     *
     * @return
     */
    private FieldSpec f_mapper() {
        return FieldSpec.builder(fluent.mapper(), "mapper")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(ClassNames.Spring_Autowired)
            .addAnnotation(AnnotationSpec.builder(ClassNames.Spring_Qualifier)
                .addMember("value", "$S", getMapperName(fluent)).build()
            )
            .build();
    }

    /**
     * public AddressMapper mapper() {}
     *
     * @return
     */
    private MethodSpec m_mapper() {
        return super.publicMethod("mapper", true, fluent.mapper())
            .addStatement(super.codeBlock("return mapper"))
            .build();
    }

    private MethodSpec m_newQuery() {
        return super.protectedMethod(M_NEW_QUERY, true, fluent.query())
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    /**
     * public EntityQuery query() {}
     *
     * @return
     */
    private MethodSpec m_defaultQuery() {
        return super.protectedMethod(M_DEFAULT_QUERY, true, fluent.query())
            .addStatement("return INSTANCE.$L()", M_DEFAULT_QUERY)
            .build();
    }

    private MethodSpec m_newUpdater() {
        return super.protectedMethod(M_NEW_UPDATER, true, fluent.updater())
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    /**
     * public AddressUpdate updater() {}
     *
     * @return
     */
    private MethodSpec m_defaultUpdater() {
        return super.protectedMethod(M_DEFAULT_UPDATER, true, fluent.updater())
            .addStatement("return INSTANCE.$L()", M_DEFAULT_UPDATER)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}