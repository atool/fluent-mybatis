package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import com.sun.source.tree.Tree.Kind;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isBlank;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

@Getter
@Accessors(chain = true)
public class FluentEntityInfo {
    /**
     * package
     */
    @Getter(AccessLevel.NONE)
    private String basePack;

    @Getter(AccessLevel.NONE)
    private String entityPack;
    /**
     * entity class name
     */
    private String className;
    /**
     * 无后缀的entity name
     */
    private String noSuffix;
    /**
     * 表名
     */
    private String tableName;
    /**
     * dao自定义扩展接口
     */
    @Getter(AccessLevel.NONE)
    private Map<String, List<String>> daoInterfaces;
    /**
     * 表名称前缀
     */
    private String prefix;

    /**
     * Entity类名后缀
     */
    private String suffix;
    /**
     * mapper bean名称前缀
     */
    private String mapperBeanPrefix;
    /**
     * 主键字段
     */
    private FieldColumn primary;
    /**
     * Entity类字段列表
     */
    private List<FieldColumn> fields = new ArrayList<>();

    private DbType dbType = DbType.MYSQL;

    public String getPackageName(String suffix) {
        return this.basePack + "." + suffix;
    }

    public FluentEntityInfo setClassName(String packageName, String className) {
        this.className = className;
        int dot = packageName.lastIndexOf('.');
        if (dot > 0) {
            this.basePack = packageName.substring(0, dot);
        } else {
            this.basePack = packageName;
        }
        this.entityPack = packageName;
        return this;
    }

    public Map<String, List<String>> getDaoInterfaces() {
        return daoInterfaces == null ? new HashMap<>() : daoInterfaces;
    }

    public FluentEntityInfo setFields(List<JCVariableDecl> fields) {
        for (JCVariableDecl variable : fields) {
            FieldColumn field = this.parseField(variable);
            if (field.isPrimary() && this.primary == null) {
                this.primary = field;
            }
            this.fields.add(field);
        }
        return this;
    }

    /**
     * 解析字段
     *
     * @param variable
     * @return
     */
    private FieldColumn parseField(JCVariableDecl variable) {
        FieldColumn field = new FieldColumn()
            .setProperty(variable.getName().toString())
            .setType(variable.getType().type);
        for (JCTree.JCAnnotation annotation : variable.mods.annotations) {
            String type = annotation.type.toString();
            if (!type.contains(TableField.class.getSimpleName()) && !type.contains(TableId.class.getSimpleName())) {
                continue;
            }
            field.setPrimary(type.contains(TableId.class.getSimpleName()));
            for (JCTree.JCExpression expression : annotation.args) {
                if (!expression.getKind().equals(Kind.ASSIGNMENT)) {
                    continue;
                }
                JCTree.JCAssign assign = (JCTree.JCAssign) expression;
                if (!assign.lhs.getKind().equals(Kind.IDENTIFIER)) {
                    continue;
                }
                this.setValue(assign, "value", field::setColumn);
                this.setValue(assign, "auto", v -> field.setAutoIncrease(Boolean.valueOf(v)));
                this.setValue(assign, "insert", field::setInsert);
                this.setValue(assign, "update", field::setUpdate);
                this.setValue(assign, "notLarge", v -> field.setNotLarge(Boolean.valueOf(v)));
                this.setValue(assign, "numericScale", field::setNumericScale);
                this.setValue(assign, "seqName", field::setSeqName);
            }
        }
        return field;
    }


    /**
     * 获取 @TableField 或 @TableId 上定义的属性值
     *
     * @param assign
     * @param method
     * @return
     */
    private void setValue(JCTree.JCAssign assign, String method, Consumer<String> consumer) {
        if (!(((JCIdent) assign.lhs).name).toString().equals(method)) {
            return;
        }
        String value = String.valueOf(((JCLiteral) assign.rhs).value);
        consumer.accept(value);
    }

    /**
     * 设置对应的表名称
     *
     * @param fluentMyBatis
     * @return
     */
    public FluentEntityInfo setFluentMyBatis(FluentMybatis fluentMyBatis, Map<String, List<String>> daoInterfaces) {
        this.prefix = fluentMyBatis.prefix();
        this.suffix = fluentMyBatis.suffix();
        this.noSuffix = this.className.replace(this.suffix, "");
        this.daoInterfaces = daoInterfaces;
        this.tableName = fluentMyBatis.table();
        if (isBlank(this.tableName)) {
            this.tableName = MybatisUtil.tableName(this.className, fluentMyBatis.prefix(), fluentMyBatis.suffix());
        }
        this.mapperBeanPrefix = fluentMyBatis.mapperBeanPrefix();
        this.dbType = fluentMyBatis.dbType();
        return this;
    }

    public ClassName className() {
        return ClassName.get(this.entityPack, this.className);
    }

    @Getter(AccessLevel.NONE)
    private String All_Fields = null;

    public String getAllFields() {
        if (this.All_Fields == null) {
            All_Fields = this.fields.stream().map(FieldColumn::getColumn).collect(Collectors.joining(", "));
        }
        return All_Fields;
    }
}