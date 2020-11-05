package cn.org.atool.fluent.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IDefault;
import cn.org.atool.fluent.mybatis.entity.base.FieldColumnParser;
import cn.org.atool.fluent.mybatis.entity.base.FluentClassName;
import cn.org.atool.fluent.mybatis.entity.base.IProcessor;
import cn.org.atool.fluent.mybatis.entity.field.BaseField;
import cn.org.atool.fluent.mybatis.entity.field.CommonField;
import cn.org.atool.fluent.mybatis.entity.field.EntityRefField;
import cn.org.atool.fluent.mybatis.entity.field.PrimaryField;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;

/**
 * fluent mybatis生成代码Entity信息
 *
 * @author wudarui
 */
@Getter
@ToString
public class FluentEntity extends FluentClassName implements Comparable<FluentEntity> {
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
    private PrimaryField primary;
    /**
     * Entity类字段列表
     */
    private List<CommonField> fields = new ArrayList<>();
    /**
     * Entity关联查询信息
     */
    private List<EntityRefField> refFields = new ArrayList<>();

    private DbType dbType = DbType.MYSQL;

    public FluentEntity setClassName(String entityPack, String className) {
        this.className = className;
        this.entityPack = entityPack;
        this.basePack = this.getParentPackage(entityPack);
        return this;
    }

    private String getParentPackage(String entityPack) {
        int index = entityPack.lastIndexOf('.');
        return index > 0 ? entityPack.substring(0, index) : entityPack;
    }

    /**
     * 设置对应的表名称
     *
     * @param fluentMyBatis
     * @return
     */
    public FluentEntity setFluentMyBatis(FluentMybatis fluentMyBatis, String defaults) {
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

    public FluentEntity setFields(List<JCVariableDecl> fields, IProcessor processor) {
        FieldColumnParser parser = new FieldColumnParser(processor);
        for (JCVariableDecl variable : fields) {
            BaseField field = parser.valueOf(variable);
            if (field instanceof EntityRefField) {
                this.refFields.add((EntityRefField) field);
            } else if (field instanceof CommonField) {
                this.addFieldColumn((CommonField) field);
            }
        }
        return this;
    }

    private void addFieldColumn(CommonField field) {
        if (field.isPrimary() && this.primary == null) {
            this.primary = (PrimaryField) field;
        }
        this.fields.add(field);
    }

    @Override
    public int compareTo(FluentEntity fluentEntity) {
        return this.className.compareTo(fluentEntity.getClassName());
    }
}