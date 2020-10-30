package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.impl.BaseUpdate;
import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.ClassNames;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Pack_Wrapper;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Update;

/**
 * updater代码生成
 *
 * @author wudarui
 */
public class UpdaterGenerator extends AbstractGenerator {
    public UpdaterGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "更新构造";
    }

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_Update;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Wrapper);
    }

    @Override
    protected void staticImport(JavaFile.Builder builder) {
        builder.addStaticImport(If.class, "notBlank");
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        builder.superclass(this.superKlass())
            .addField(this.f_setter())
            .addField(this.f_where())
            .addField(this.f_orderBy())
            .addMethod(this.constructor0())
            .addMethod(this.m_where())
            .addMethod(this.m_primary())
            .addMethod(this.m_allFields());
    }

    /**
     * public final UpdateSetter update = new UpdateSetter(this);
     *
     * @return
     */
    private FieldSpec f_setter() {
        return FieldSpec.builder(WrapperHelperGenerator.updateSetter(fluent),
            "update", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateSetter(this)")
            .build();
    }

    /**
     * public final UpdateWhere where = new UpdateWhere(this);
     *
     * @return
     */
    private FieldSpec f_where() {
        return FieldSpec.builder(WrapperHelperGenerator.updateWhere(fluent),
            "where", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateWhere(this)")
            .build();
    }

    /**
     * public final UpdateOrderBy orderBy = new UpdateOrderBy(this);
     *
     * @return
     */
    private FieldSpec f_orderBy() {
        return FieldSpec.builder(WrapperHelperGenerator.updateOrderBy(fluent),
            "orderBy", Modifier.PUBLIC, Modifier.FINAL)
            .initializer("new UpdateOrderBy(this)")
            .build();
    }

    /**
     * public EntityUpdate() {}
     *
     * @return
     */
    private MethodSpec constructor0() {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("super($T.Table_Name, $T.class, $T.class)",
                MappingGenerator.className(fluent),
                fluent.className(),
                QueryGenerator.className(fluent)
            )
            .build();
    }

    private ParameterizedTypeName superKlass() {
        ClassName base = ClassName.get(BaseUpdate.class);
        ClassName entity = fluent.className();
        ClassName updater = ClassNames.updater(fluent);
        ClassName query = QueryGenerator.className(fluent);
        return ParameterizedTypeName.get(base, entity, updater, query);
    }

    /**
     * public QueryWhere where() {}
     *
     * @return
     */
    private MethodSpec m_where() {
        return MethodSpec.methodBuilder("where")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(WrapperHelperGenerator.updateWhere(fluent))
            .addStatement("return this.where")
            .build();
    }

    private MethodSpec m_allFields() {
        return MethodSpec.methodBuilder("allFields")
            .addModifiers(Modifier.PROTECTED)
            .returns(parameterizedType(List.class, String.class))
            .addStatement("return $T.ALL_COLUMNS", MappingGenerator.className(fluent))
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}