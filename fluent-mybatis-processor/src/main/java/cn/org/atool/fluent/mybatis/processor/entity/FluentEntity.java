package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.IDefaultSetter;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.isBlank;

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
    private List<EntityRefMethod> refMethods = new ArrayList<>();

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
        this.defaults = isBlank(defaults) ? IDefaultSetter.class.getName() : defaults;
        this.tableName = fluentMyBatis.table();
        if (isBlank(this.tableName)) {
            this.tableName = MybatisUtil.tableName(this.className, fluentMyBatis.prefix(), fluentMyBatis.suffix());
        }
        this.mapperBeanPrefix = fluentMyBatis.mapperBeanPrefix();
        this.dbType = fluentMyBatis.dbType();
        return this;
    }

    public FluentEntity addMethod(EntityRefMethod method) {
        this.refMethods.add(method);
        return this;
    }

    public void addField(CommonField field) {
        if (this.fields.contains(field)) {
            return;
        }
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