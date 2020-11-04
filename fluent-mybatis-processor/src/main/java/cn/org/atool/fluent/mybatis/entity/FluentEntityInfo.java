package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IDefault;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumn;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumnParser;
import cn.org.atool.fluent.mybatis.entity.generator.*;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

@Getter
@ToString
public class FluentEntityInfo {
    /**
     * package
     */
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
    private List<String> daoInterfaces;
    /**
     * 默认值实现
     */
    private String defaults;
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
        this.basePack = this.getParentPackage(entityPack);
        return this;
    }

    private String getParentPackage(String entityPack) {
        int index = entityPack.lastIndexOf('.');
        return index > 0 ? entityPack.substring(0, index) : entityPack;
    }

    public List<String> getDaoInterfaces() {
        return daoInterfaces == null ? Collections.EMPTY_LIST : daoInterfaces;
    }

    public FluentEntityInfo setFields(List<JCVariableDecl> fields) {
        for (JCVariableDecl variable : fields) {
            FieldColumn field = FieldColumnParser.valueOf(variable);
            if (field == null) {
                continue;
            }
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
    public FluentEntityInfo setFluentMyBatis(FluentMybatis fluentMyBatis, String defaults) {
        this.prefix = fluentMyBatis.prefix();
        this.suffix = fluentMyBatis.suffix();
        this.noSuffix = this.className.replace(this.suffix, "");
        this.defaults = isBlank(defaults) ? IDefault.class.getName() : defaults;
        this.tableName = fluentMyBatis.table();
        if (isBlank(this.tableName)) {
            this.tableName = MybatisUtil.tableName(this.className, fluentMyBatis.prefix(), fluentMyBatis.suffix());
        }
        this.mapperBeanPrefix = fluentMyBatis.mapperBeanPrefix();
        this.dbType = fluentMyBatis.dbType();
        return this;
    }

    @Getter(AccessLevel.NONE)
    private String All_Fields = null;

    public String getAllFields() {
        if (this.All_Fields == null) {
            All_Fields = this.fields.stream().map(FieldColumn::getColumn).collect(Collectors.joining(", "));
        }
        return All_Fields;
    }

    /**
     * 首字母小写,不带Entity后缀的entity名称
     *
     * @return
     */
    public String lowerNoSuffix() {
        return MybatisUtil.lowerFirst(this.noSuffix, "");
    }

    // all ClassName

    /**
     * ClassName of XyzEntity
     *
     * @return
     */
    public ClassName entity() {
        return ClassName.get(this.getEntityPack(), this.getClassName());
    }

    /**
     * ClassName of XyzUpdater
     *
     * @return
     */
    public ClassName updater() {
        return ClassName.get(
            UpdaterGenerator.getPackageName(this),
            UpdaterGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzEntityHelper
     *
     * @return
     */
    public ClassName entityHelper() {
        return ClassName.get(
            EntityHelperGenerator.getPackageName(this),
            EntityHelperGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzMapper
     *
     * @return
     */
    public ClassName mapper() {
        return ClassName.get(
            MapperGenerator.getPackageName(this),
            MapperGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzMapping
     *
     * @return
     */
    public ClassName mapping() {
        return ClassName.get(
            MappingGenerator.getPackageName(this),
            MappingGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzQuery
     *
     * @return
     */
    public ClassName query() {
        return ClassName.get(
            QueryGenerator.getPackageName(this),
            QueryGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzSqlProvider
     *
     * @return
     */
    public ClassName sqlProvider() {
        return ClassName.get(
            SqlProviderGenerator.getPackageName(this),
            SqlProviderGenerator.getClassName(this));
    }


    /**
     * ClassName of XyzWrapperFactory
     *
     * @return
     */
    public ClassName wrapperFactory() {
        return ClassName.get(
            WrapperDefaultGenerator.getPackageName(this),
            WrapperDefaultGenerator.getClassName(this)
        );
    }

    public ClassName queryWhere() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_QueryWhere);
    }

    public ClassName updateWhere() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_UpdateWhere);
    }

    public ClassName selector() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_Selector);
    }

    public ClassName groupBy() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_GroupBy);
    }

    public ClassName having() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_Having);
    }

    public ClassName queryOrderBy() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_QueryOrderBy);
    }

    public ClassName updateOrderBy() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_UpdateOrderBy);
    }

    public ClassName updateSetter() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_UpdateSetter);
    }

    public ClassName segment() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_ISegment);
    }
}