package cn.org.atool.fluent.mybatis.generator.template.entity;

import cn.org.atool.fluent.mybatis.base.IEntity;
import org.test4j.generator.mybatis.config.impl.TableField;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.commons.TextBuilder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static java.util.stream.Collectors.joining;
import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_ENTITY;
import static org.test4j.tools.commons.StringHelper.isNotBlank;

/**
 * EntityTemplate
 *
 * @author darui.wu
 */
public class EntityTemplate extends BaseTemplate {

    public EntityTemplate() {
        super("templates/entity/Entity.java.vm", "*Entity.java");
    }

    @Override
    public String getTemplateId() {
        return KEY_ENTITY;
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> parent, Map<String, Object> ctx) {
        this.putInterfaces(parent, ctx, table.getEntityInterfaces());

        ctx.put("primaryKey", this.findPrimaryKey(table));
        Map<String, String> annotation = new HashMap<>();
        for (TableField field : table.getFields()) {
            String text = this.fieldAnnotation(table, field);
            annotation.put(field.getName(), text);
        }
        ctx.put("annotation", annotation);
        StringBuffer buff = new StringBuffer();
        buff.append("table = ").append('"').append(table.getTableName()).append('"');
        if (isNotBlank(table.getMapperBeanPrefix())) {
            buff.append(", mapperBeanPrefix = ").append('"').append(table.getMapperBeanPrefix()).append('"');
        }

        if (table.getBaseDaoInterfaces() != null && !table.getBaseDaoInterfaces().isEmpty()) {
            String daos = table.getBaseDaoInterfaces().stream()
                .map(dao -> dao.getSimpleName() + ".class")
                .collect(joining(", ", "{", "}"));
            buff.append(",\n\tdaoInterface = ").append(daos);
            for (Class dao : table.getBaseDaoInterfaces()) {
                this.addImport(parent, dao);
            }
        }
        ctx.put("fluentMybatis", buff.toString());
    }

    private void addImport(Map<String, Object> parent, Class klass) {
        parent.put("importTypes", parent.get("importTypes") + "\nimport " + klass.getName() + ";");
    }

    private String findPrimaryKey(TableSetter table) {
        for (TableField field : table.getFields()) {
            if (field.isPrimary()) {
                return field.getName();
            }
        }
        return "null";
    }

    private void putInterfaces(Map<String, Object> parent, Map<String, Object> ctx, List<Class> interfaces) {
        if (interfaces == null || interfaces.size() == 0) {
            return;
        }
        ctx.put("interface", interfaces.stream().map(i -> "import " + i.getName() + ";").collect(joining("\n")));
        ctx.put("interfaceName", interfaces.stream()
            .map(clazz -> {
                String name = clazz.getSimpleName();
                if (clazz.getTypeParameters().length != 1) {
                    return name;
                }
                boolean useEntityType = false;
                for (Type bound : clazz.getTypeParameters()[0].getBounds()) {
                    String tn = bound.getTypeName();
                    if (Objects.equals(tn, name) || Allow_Entity_Bounds.contains(tn)) {
                        useEntityType = true;
                        break;
                    }
                }
                if (useEntityType) {
                    name += "<" + super.getConfig(parent, "entity.name") + ">";
                }
                return name;
            })
            .collect(joining(", ", ", ", ""))
        );
    }

    /**
     * Entity自定义接口的泛型如果是下列类型子类型，可以使用Entity作为泛型参数
     */
    private static Set<String> Allow_Entity_Bounds = new HashSet<>();

    static {
        Allow_Entity_Bounds.add(IEntity.class.getName());
        Allow_Entity_Bounds.add(Object.class.getName());
        Allow_Entity_Bounds.add(Serializable.class.getName());
    }

    private String fieldAnnotation(TableSetter table, TableField field) {
        TextBuilder text = TextBuilder.build();
        if (field.isPrimary()) {
            text.quotas("@TableId(value = '%s'", field.getColumnName());
            if (!field.isPrimaryId()) {
                text.append(", auto = false");
            }
            if (!StringHelper.isBlank(table.getSeqName())) {
                text.quotas(", seqName='%s'", table.getSeqName());
            }
            text.append(")");
        } else {
            text.quotas("@TableField(value = '%s'", field.getColumnName());
            if (isNotBlank(field.getInsert())) {
                text.quotas(", insert = '%s'", field.getInsert());
            }
            if (isNotBlank(field.getUpdate())) {
                text.quotas(", update = '%s'", field.getUpdate());
            }
            if (field.getIsLarge() != null && !field.getIsLarge()) {
                text.quotas(", notLarge = false");
            }
            if (field.getTypeHandler() != null) {
                text.quotas(", typeHandler = %s.class", field.getTypeHandler().getName());
            }
            text.append(")");
        }
        return text.toString();
    }

    @Override
    protected String getPackage(TableSetter table) {
        String pack = super.getPackage(table);
        return pack.endsWith(".") ? pack.substring(0, pack.length() - 1) : pack;
    }
}