package cn.org.atool.fluent.mybatis.processor.filer.refs;

import cn.org.atool.fluent.mybatis.base.IRefs;
import cn.org.atool.fluent.mybatis.base.entity.IEntityHelper;
import cn.org.atool.fluent.mybatis.processor.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.processor.entity.FluentList;
import cn.org.atool.generator.javafile.AbstractFile;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static cn.org.atool.fluent.mybatis.processor.filer.refs.MappingRefFiler.m_findColumnByField;
import static cn.org.atool.fluent.mybatis.processor.filer.refs.MappingRefFiler.m_findPrimaryColumn;
import static cn.org.atool.fluent.mybatis.processor.filer.refs.QueryRefFiler.*;
import static cn.org.atool.fluent.mybatis.processor.filer.refs.SetterRefFiler.m_newFormSetter;
import static cn.org.atool.generator.util.ClassNames.Lombok_Getter;
import static cn.org.atool.generator.util.ClassNames.Spring_Autowired;

/**
 * IMapperRef 文件构造
 *
 * @author darui.wu
 */
public class MapperRefFiler extends AbstractFile {
    private static String MapperRef = "MapperRef";

    public static ClassName getClassName() {
        return ClassName.get(FluentList.refsPackage(), MapperRef);
    }

    public MapperRefFiler() {
        this.packageName = FluentList.refsPackage();
        this.klassName = MapperRef;
        this.comment = "应用所有Mapper Bean引用";
    }

    @Override
    protected void build(TypeSpec.Builder spec) {
        spec.superclass(IRefs.class)
            .addModifiers(Modifier.ABSTRACT);
        for (FluentEntity fluent : FluentList.getFluents()) {
            spec.addField(this.f_mapper(fluent));
        }
        spec.addMethod(m_findColumnByField(true))
            .addMethod(m_findPrimaryColumn(true))
            .addMethod(m_findDefaultGetter(true))
            .addMethod(m_defaultQuery(true))
            .addMethod(m_defaultUpdater(true))
            .addMethod(m_setEntityByDefault(true))
            .addMethod(m_newFormSetter(true))
            .addMethod(this.m_entityHelper())
            .addMethod(this.m_initEntityMapper());
        spec.addType(this.class_mapping())
            .addType(this.class_query())
            .addType(this.class_setter());
    }

    private FieldSpec f_mapper(FluentEntity fluent) {
        return FieldSpec.builder(fluent.mapper(), fluent.lowerNoSuffix() + "Mapper",
            Modifier.PROTECTED)
            .addAnnotation(Lombok_Getter)
            .addAnnotation(Spring_Autowired)
            .build();
    }

    private MethodSpec m_entityHelper() {
        return MethodSpec.methodBuilder("entityHelper")
            .addParameter(Class.class, "clazz")
            .returns(IEntityHelper.class)
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PROTECTED)
            .addStatement("return $T.entityHelper(findFluentEntityClass(clazz))", EntityHelperRefFiler.getClassName())
            .build();
    }

    private MethodSpec m_initEntityMapper() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("initEntityMapper")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.FINAL, Modifier.PROTECTED);

        for (FluentEntity fluent : FluentList.getFluents()) {
            builder.addStatement("this.entityMappers.put($T.class, this.$LMapper)", fluent.entity(), fluent.lowerNoSuffix());
        }
        return builder.build();
    }


    private TypeSpec class_mapping() {
        return TypeSpec.classBuilder("Column")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(MappingRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_query() {
        return TypeSpec.classBuilder("Query")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(QueryRefFiler.getClassName())
            .build();
    }

    private TypeSpec class_setter() {
        return TypeSpec.classBuilder("Setter")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .superclass(SetterRefFiler.getClassName())
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }

    protected String generatorName() {
        return "FluentMybatis";
    }
}