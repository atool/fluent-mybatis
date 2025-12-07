package cn.org.atool.fluent.processor.mybatis.entity;

import cn.org.atool.fluent.mybatis.annotation.*;
import cn.org.atool.fluent.mybatis.base.crud.IDefaultSetter;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.processor.mybatis.base.FluentClassName;
import cn.org.atool.fluent.processor.mybatis.filer.ClassNames2;
import cn.org.atool.fluent.processor.mybatis.scanner.ClassAttrParser;
import cn.org.atool.generator.database.model.FieldType;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.lang.model.element.*;
import java.util.*;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;
import static cn.org.atool.fluent.processor.mybatis.filer.ClassNames2.CN_Long;
import static cn.org.atool.fluent.processor.mybatis.scanner.ClassAttrParser.ATTR_DEFAULTS;
import static cn.org.atool.fluent.processor.mybatis.scanner.ClassAttrParser.ATTR_SUPER_MAPPER;
import static javax.lang.model.element.Modifier.*;

/**
 * fluent mybatis生成代码Entity信息
 *
 * @author wudarui
 */
@SuppressWarnings({"UnusedReturnValue", "unchecked"})
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

    public void parseEntity(TypeElement entity, Consumer<String> log) {
        FluentMybatis fluentMybatis = entity.getAnnotation(FluentMybatis.class);
        if (fluentMybatis == null) {
            log.accept("Error in: " + entity.getQualifiedName().toString());
        } else {
            ClassName className = ClassNames2.getClassName(entity.getQualifiedName().toString());
            this.setClassName(className.packageName(), className.simpleName());
            String defaults = ClassAttrParser.getClassAttr(entity, FluentMybatis.class, ATTR_DEFAULTS, IDefaultSetter.class);
            String superMapper = ClassAttrParser.getClassAttr(entity, FluentMybatis.class, ATTR_SUPER_MAPPER, IMapper.class);
            this.setFluentMyBatis(fluentMybatis, defaults, superMapper);
        }
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
    public FluentEntity sort() {
        this.fields.sort(Comparator.naturalOrder());
        return this;
    }

    @Override
    public int compareTo(FluentEntity fluentEntity) {
        return this.className.compareTo(fluentEntity.getClassName());
    }

    /**
     * 解析RefMethod方法
     *
     * @param element ExecutableElement
     */
    public void addMethod(ExecutableElement element) {
        RefMethod ref = element.getAnnotation(RefMethod.class);
        if (ref == null || !this.isPublicMethod(element)) {
            return;
        }
        String methodName = element.getSimpleName().toString();
        EntityRefMethod method = new EntityRefMethod(methodName, ClassName.get(element.getReturnType()));
        method.setValue(ref.value());
        this.addMethod(method);
    }

    public void visitVariable(VariableElement element) {
        if (this.isTableField(element)) {
            return;
        }
        TableId tableId = element.getAnnotation(TableId.class);
        String fieldName = element.getSimpleName().toString();

        if (tableId == null) {
            CommonField field = parseCommonField(fieldName, element);
            this.addField(field);
            this.setFieldType(field, element);
        } else {
            PrimaryField field = parsePrimaryField(fieldName, element, tableId);
            field.setType(field.isAutoIncrease() ? FieldType.PrimaryId : FieldType.PrimaryKey);
            this.addField(field);
        }
    }

    private boolean isTableField(VariableElement element) {
        return element.getKind() != ElementKind.FIELD ||
            element.getModifiers().contains(Modifier.STATIC) ||
            element.getModifiers().contains(Modifier.TRANSIENT) ||
            element.getAnnotation(NotField.class) != null;
    }

    private boolean isPublicMethod(ExecutableElement element) {
        Set<Modifier> set = element.getModifiers();
        return set.contains(PUBLIC) && !set.contains(STATIC) && !set.contains(ABSTRACT);
    }

    private void setFieldType(CommonField field, VariableElement element) {
        if (element.getAnnotation(LogicDelete.class) != null) {
            this.setLogicDelete(field.getName());
            this.setLongTypeOfLogicDelete(Objects.equals(field.getJavaType(), CN_Long));
            field.setType(FieldType.IsDeleted);
        }
        if (element.getAnnotation(Version.class) != null) {
            this.setVersionField(field.getName());
            field.setType(FieldType.Version);
        }
        if (element.getAnnotation(GmtCreate.class) != null) {
            field.setType(FieldType.GmtCreate);
        }
        if (element.getAnnotation(GmtModified.class) != null) {
            field.setType(FieldType.GmtModified);
        }
    }

    /**
     * 解析普通字段注解信息
     *
     * @param fieldName name of field
     * @param var       var
     * @return ignore
     */
    private CommonField parseCommonField(String fieldName, VariableElement var) {
        CommonField field = new CommonField(fieldName, ClassNames2.javaType(var));
        TableField tableField = var.getAnnotation(TableField.class);
        if (tableField == null) {
            return field;
        }
        field.setColumn(tableField.value());
        field.setInsert(tableField.insert());
        field.setUpdate(tableField.update());
        field.setNotLarge(tableField.notLarge());
        field.setNumericScale(tableField.numericScale());
        field.setJdbcType(tableField.jdbcType().name());
        field.setTypeHandler(getTypeHandler(var, TableField.class.getSimpleName()));

        return field;
    }

    /**
     * 解析主键主机信息
     *
     * @param fieldName name of field
     * @param var       var
     * @param tableId   Annotation
     * @return ignore
     */
    private PrimaryField parsePrimaryField(String fieldName, VariableElement var, TableId tableId) {
        PrimaryField field = new PrimaryField(fieldName, ClassNames2.javaType(var));
        field.setColumn(tableId.value());
        field.setAutoIncrease(tableId.auto());
        field.setSeqIsBeforeOrder(tableId.before());
        field.setJdbcType(tableId.jdbcType().name());
        field.setTypeHandler(getTypeHandler(var, TableId.class.getSimpleName()));
        field.setSeqName(tableId.seqName());
        return field;
    }

    /**
     * 获取 {@link TableId}和{@link TableField}上的typeHandler属性
     *
     * @param var   注解实例
     * @param aName "TableId" or "TableField"
     * @return TypeName
     */
    private TypeName getTypeHandler(VariableElement var, String aName) {
        for (AnnotationMirror annotationMirror : var.getAnnotationMirrors()) {
            String aTypeName = annotationMirror.getAnnotationType().toString();
            if (!aTypeName.contains(aName)) {
                continue;
            }
            Map<ExecutableElement, AnnotationValue> values = (Map<ExecutableElement, AnnotationValue>) annotationMirror.getElementValues();
            for (Map.Entry<ExecutableElement, AnnotationValue> entry : values.entrySet()) {
                String method = entry.getKey().getSimpleName().toString();
                AnnotationValue value = entry.getValue();
                if ("typeHandler".equals(method)) {
                    return ClassNames2.getClassName(value.getValue().toString());
                }
            }
        }
        return null;
    }
}