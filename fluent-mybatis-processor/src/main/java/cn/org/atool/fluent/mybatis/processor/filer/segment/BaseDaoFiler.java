package cn.org.atool.fluent.mybatis.processor.filer.segment;

import cn.org.atool.fluent.mybatis.base.dao.BaseDao;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.ClassNames2;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
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
    protected void staticImport(JavaFile.Builder spec) {
        spec.addStaticImport(fluent.entityKit(), Suffix_EntityKit);
        super.staticImport(spec);
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

    /**
     * 超类定义
     * <pre>
     * XyzBaseDao extends BaseDao<XyzEntity>
     * </pre>
     *
     * @return TypeName
     */
    private TypeName superBaseDaoImplKlass() {
        ClassName baseImpl = ClassName.get(BaseDao.class.getPackage().getName(), BaseDao.class.getSimpleName());
        ClassName entity = fluent.entity();
        return ParameterizedTypeName.get(baseImpl, entity);
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

    private MethodSpec m_newQuery() {
        return super.protectedMethod(M_NEW_QUERY, fluent.query())
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    /**
     * public EntityQuery query() {}
     *
     * @return MethodSpec
     */
    private MethodSpec m_defaultQuery() {
        return super.protectedMethod(M_DEFAULT_QUERY, fluent.query())
            .addStatement("return Kit.$L()", M_DEFAULT_QUERY)
            .build();
    }

    private MethodSpec m_newUpdater() {
        return super.protectedMethod(M_NEW_UPDATER, fluent.updater())
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    /**
     * public AddressUpdate updater() {}
     *
     * @return MethodSpec
     */
    private MethodSpec m_defaultUpdater() {
        return super.protectedMethod(M_DEFAULT_UPDATER, fluent.updater())
            .addStatement("return Kit.$L()", M_DEFAULT_UPDATER)
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}