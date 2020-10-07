package cn.org.atool.fluent.mybatis.generator.template.entity;

import cn.org.atool.fluent.mybatis.annotation.DaoInterface;
import cn.org.atool.fluent.mybatis.annotation.ParaType;
import org.test4j.generator.mybatis.config.impl.TableField;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.commons.TextBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        this.putInterfaces(parent, ctx, table, table.getEntityInterfaces());

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
            this.addImport(parent, DaoInterface.class);
            this.addImport(parent, ParaType.class);
            buff.append(", daoInterface = {\n");
            for (Map.Entry<Class, String[]> entry : table.getBaseDaoInterfaces().entrySet()) {
                this.addImport(parent, entry.getKey());
                buff.append("\t@DaoInterface(");
                buff.append("value = ").append(entry.getKey().getSimpleName()).append(".class");
                buff.append(", args = {");
                buff.append(String.join(", ", entry.getValue()));
                buff.append("})\n");
            }
            buff.append("}");
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

    private void putInterfaces(Map<String, Object> parent, Map<String, Object> templateContext, TableSetter table, Map<Class, String[]> interfaces) {
        if (interfaces == null || interfaces.size() == 0) {
            return;
        }
        templateContext.put("interface", interfaces.keySet().stream().map(i -> "import " + i.getName() + ";").collect(joining("\n")));
        templateContext.put("interfaceName", interfaces.entrySet().stream()
            .map(e -> entityInterface(parent, e.getKey(), e.getValue()))
            .collect(joining(", ", ", ", ""))
        );
    }

    private String entityInterface(Map<String, Object> parent, Class klass, String[] types) {
        StringBuffer buff = new StringBuffer(klass.getSimpleName());
        if (types != null && types.length > 0) {
            String value = Stream.of(types)
                .map(var -> super.getConfig(parent, var))
                .collect(Collectors.joining(", ", "<", ">"));
            buff.append(value);
        }
        return buff.toString();
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
            if (isNotBlank(field.getTypeHandler())) {
                text.quotas(", typeHandler = '%s'", field.getTypeHandler());
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