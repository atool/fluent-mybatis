package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumnParser;
import cn.org.atool.fluent.mybatis.method.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

@Getter
@Accessors(chain = true)
public class FluentEntityInfo {
    /**
     * package
     */
    @Getter(AccessLevel.NONE)
    private String basePack;

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

    public FluentEntityInfo setClassName(String entityPack, String className) {
        this.className = className;
        this.entityPack = entityPack;
//        int index = entityPack.lastIndexOf('.');
//        this.basePack = index > 0 ? entityPack.substring(0, index) : entityPack;
        this.basePack = this.entityPack;
        return this;
    }

    public Map<String, List<String>> getDaoInterfaces() {
        return daoInterfaces == null ? new HashMap<>() : daoInterfaces;
    }

    public FluentEntityInfo setFields(List<JCVariableDecl> fields) {
        for (JCVariableDecl variable : fields) {
            FieldColumn field = FieldColumnParser.valueOf(variable);
            if (field.isPrimary() && this.primary == null) {
                this.primary = field;
            }
            this.fields.add(field);
        }
        return this;
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