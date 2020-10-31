package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.base.impl.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.ClassNames;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.entity.generator.MapperGenerator.getMapperName;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_BaseDao;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_BaseDao;

public class BaseDaoGenerator extends AbstractGenerator {
    public BaseDaoGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = fluentEntityInfo.getPackageName(Pack_BaseDao);
        this.klassName = fluentEntityInfo.getNoSuffix() + Suffix_BaseDao;
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.addModifiers(Modifier.ABSTRACT)
            .superclass(this.superBaseDaoImplKlass())
            .addSuperinterface(this.superMappingClass());
        for (String daoInterface : fluent.getDaoInterfaces()) {
            this.addInterface(builder, daoInterface);
        }
        builder.addField(this.f_mapper())
            .addMethod(this.m_mapper())
            .addMethod(this.m_newQuery())
            .addMethod(this.m_query())
            .addMethod(this.m_newUpdater())
            .addMethod(this.m_updater())
            .addMethod(this.m_primaryField());
    }

    private void addInterface(TypeSpec.Builder builder, String daoInterface) {
        int dot = daoInterface.lastIndexOf('.');
        String packageName = "";
        String simpleClassName = daoInterface;
        if (dot > 0) {
            packageName = daoInterface.substring(0, dot);
            simpleClassName = daoInterface.substring(dot + 1);
        }
        builder.addSuperinterface(parameterizedType(
            ClassName.get(packageName, simpleClassName),
            fluent.entity(),
            fluent.query(),
            fluent.updater()
        ));
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

    /**
     * public EntityQuery query() {}
     *
     * @return
     */
    private MethodSpec m_query() {
        return super.publicMethod("query", true, fluent.query())
            .addStatement("$T query = this.newQuery()", fluent.query())
            .addStatement("super.setDaoQueryDefault(query)")
            .addStatement("return query")
            .build();
    }

    private MethodSpec m_newQuery() {
        return super.protectedMethod("newQuery", true, fluent.query())
            .addStatement("return new $T()", fluent.query())
            .build();
    }

    private MethodSpec m_newUpdater() {
        return super.protectedMethod("newUpdater", true, fluent.updater())
            .addStatement("return new $T()", fluent.updater())
            .build();
    }

    /**
     * public AddressUpdate updater() {}
     *
     * @return
     */
    private MethodSpec m_updater() {
        return super.publicMethod("updater", true, fluent.updater())
            .addStatement("$T updater = this.newUpdater()", fluent.updater())
            .addStatement("super.setDaoUpdateDefault(updater)")
            .addStatement("return updater")
            .build();
    }

    /**
     * public String findPkColumn() {}
     *
     * @return
     */
    private MethodSpec m_primaryField() {
        MethodSpec.Builder builder = super.publicMethod("primaryField", false, FieldMapping.class)
            .addJavadoc("返回实体类主键值");
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