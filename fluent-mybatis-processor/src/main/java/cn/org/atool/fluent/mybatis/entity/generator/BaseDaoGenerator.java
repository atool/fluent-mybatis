package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.ClassNames;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.entity.base.MethodName.*;
import static cn.org.atool.fluent.mybatis.entity.generator.MapperGenerator.getMapperName;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_BaseDao;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_BaseDao;

/**
 * BaseDaoGenerator: *BaseDao文件生成
 *
 * @author wudarui
 */
public class BaseDaoGenerator extends AbstractGenerator {
    public BaseDaoGenerator(FluentEntity fluentEntity) {
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
            .superclass(this.superBaseDaoImplKlass())
            .addSuperinterface(this.superMappingClass());

        builder.addField(this.f_mapper())
            .addMethod(this.m_mapper())
            .addMethod(this.m_newQuery())
            .addMethod(this.m_defaultQuery())
            .addMethod(this.m_newUpdater())
            .addMethod(this.m_defaultUpdater())
            .addMethod(this.m_setEntityDefault())
            .addMethod(this.m_primaryField());
    }

    private MethodSpec m_setEntityDefault() {
        return MethodSpec.methodBuilder("setEntityDefault")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .addParameter(fluent.entity(), "entity")
            .addStatement("INSTANCE.setInsertDefault(entity)")
            .build();
    }


    private TypeName superMappingClass() {
        return ClassName.get(MappingGenerator.getPackageName(fluent), MappingGenerator.getClassName(fluent));
    }

    private TypeName superBaseDaoImplKlass() {
        ClassName baseImpl = ClassName.get(BaseDaoImpl.class.getPackage().getName(), BaseDaoImpl.class.getSimpleName());
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
            .addAnnotation(ClassNames.CN_Autowired)
            .addAnnotation(AnnotationSpec.builder(ClassNames.CN_Qualifier)
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
        return super.publicMethod(M_DEFAULT_QUERY, true, fluent.query())
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
        return super.publicMethod(M_DEFAULT_UPDATER, true, fluent.updater())
            .addStatement("return INSTANCE.$L()", M_DEFAULT_UPDATER)
            .build();
    }

    /**
     * public String findPkColumn() {}
     *
     * @return
     */
    private MethodSpec m_primaryField() {
        MethodSpec.Builder builder = super.publicMethod("primaryField", true, FieldMapping.class);
        if (fluent.getPrimary() == null) {
            super.throwPrimaryNoFound(builder);
        } else {
            builder.addStatement("return $T.$L", fluent.mapping(), fluent.getPrimary().getProperty());
        }
        return builder.build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}