package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.base.FluentClassName;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * fluent mybatis生成代码Entity信息
 *
 * @author wudarui
 */
@SuppressWarnings({"UnusedReturnValue"})
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
     * 使用mybatis的二级缓存
     */
    private boolean usedCached;
    /**
     * 无后缀的entity name
     */
    private String noSuffix;
    /**
     * 表名
     */
    private String tableName;
    /**
     * schema
     */
    private String schema = "";
    /**
     * 默认值实现
     */
    private String defaults;
    /**
     * 自定义的通用mapper基类
     */
    private String superMapper;
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
    private final List<CommonField> fields = new ArrayList<>();
    /**
     * 逻辑删除字段
     */
    @Setter
    private String logicDelete;
    /**
     * 逻辑删除字段时 Long 型
     */
    @Setter
    private boolean longTypeOfLogicDelete = false;
    /**
     * 乐观锁字段
     */
    @Setter
    private String versionField;
    /**
     * Entity关联查询信息
     */
    private final List<EntityRefMethod> refMethods = new ArrayList<>();

    private DbType dbType = DbType.MYSQL;

    private boolean useDao = true;

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
     * @param fluentMyBatis Annotation @FluentMybatis
     * @return ignore
     */
    public FluentEntity setFluentMyBatis(FluentMybatis fluentMyBatis, String defaults, String superMapper) {
        this.prefix = fluentMyBatis.prefix();
        this.suffix = fluentMyBatis.suffix();
        this.noSuffix = this.className.replace(this.suffix, EMPTY);
        this.defaults = isBlank(defaults) ? IDefaultSetter.class.getName() : defaults;
        this.superMapper = isBlank(superMapper) ? IMapper.class.getName() : superMapper;
        this.tableName = fluentMyBatis.table();
        if (isBlank(this.tableName)) {
            this.tableName = MybatisUtil.tableName(this.className, fluentMyBatis.prefix(), fluentMyBatis.suffix());
        }
        this.schema = fluentMyBatis.schema();
        this.mapperBeanPrefix = fluentMyBatis.mapperBeanPrefix();
        this.dbType = fluentMyBatis.dbType();
        this.usedCached = fluentMyBatis.useCached();
        this.useDao = fluentMyBatis.useDao();
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

    /**
     * 对字段进行排序
     */
    public FluentEntity sortFields() {
        this.fields.sort(Comparator.naturalOrder());
        return this;
    }

    @Override
    public int compareTo(FluentEntity fluentEntity) {
        return this.className.compareTo(fluentEntity.getClassName());
    }
}