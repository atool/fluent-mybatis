package cn.org.atool.fluent.mybatis.entity.generator;

import cn.org.atool.fluent.mybatis.entity.FluentEntityInfo;
import cn.org.atool.fluent.mybatis.entity.base.AbstractGenerator;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.ibatis.jdbc.SQL;

import javax.lang.model.element.TypeElement;

import static cn.org.atool.fluent.mybatis.entity.base.ClassNameConst.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

public class SqlProviderGenerator extends AbstractGenerator {

    public static String getClassName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getNoSuffix() + Suffix_SqlProvider;
    }

    public static String getPackageName(FluentEntityInfo fluentEntityInfo) {
        return fluentEntityInfo.getPackageName(Pack_Helper);
    }

    public static ClassName className(FluentEntityInfo fluentEntityInfo) {
        return ClassName.get(getPackageName(fluentEntityInfo), getClassName(fluentEntityInfo));
    }

    public SqlProviderGenerator(TypeElement curElement, FluentEntityInfo fluentEntityInfo) {
        super(curElement, fluentEntityInfo);
        this.packageName = getPackageName(fluentEntityInfo);
        this.klassName = getClassName(fluentEntityInfo);
        this.comment = "动态语句封装";
    }

    @Override
    protected void build(TypeSpec.Builder builder) {
        //builder.addSuperinterface(parameterizedType(ClassName.get(ISqlProvider.class), fluentEntityInfo.className()));
        builder.addMethod(this.m_insert());
    }

    private MethodSpec m_insert() {
        MethodSpec.Builder builder = super.sqlMethod("insert")
            .addParameter(fluentEntityInfo.className(), "entity")
            .addStatement("$T sql = new SQL()", SQL.class)
            .addStatement("sql.INSERT_INTO($S);", fluentEntityInfo.getTableName());
        for (FieldColumn field : this.fluentEntityInfo.getFields()) {
            builder.addCode("if (entity.$L() != null) {\n", field.getMethodName());
            builder.addCode("\tsql.VALUES($S, $S);\n", field.getColumn(), "#{" + field.getProperty() + "}");
//            if (isNotBlank(field.getInsert())) {
//                builder.addCode("} else {\n");
//                builder.addCode("\tsql.VALUES($S, $S);\n", field.getColumn(), field.getInsert());
//            }
            builder.addCode("}\n");
        }
        return builder.addStatement("return sql.toString()")
            .build();
    }

    @Override
    protected boolean isInterface() {
        return false;
    }
}