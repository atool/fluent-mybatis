package cn.org.atool.fluent.mybatis.generator.template.entity;

import org.test4j.generator.mybatis.config.impl.TableField;
import org.test4j.generator.mybatis.config.impl.TableSetter;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.commons.TextBuilder;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_ENTITY;

/**
 * EntityTemplate
 *
 * @author darui.wu
 */
public class EntityTemplate extends BaseTemplate {

    public EntityTemplate() {
        super("templates/entity/Entity.java.vm", "entity/*Entity.java");
    }

    @Override
    public String getTemplateId() {
        return KEY_ENTITY;
    }

    @Override
    protected void templateConfigs(TableSetter table, Map<String, Object> templateContext) {
        this.putInterfaces(templateContext, table, table.getEntityInterfaces());

        templateContext.put("primaryKey", this.findPrimaryKey(table));
        Map<String, String> annotation = new HashMap<>();
        for (TableField field : table.getFields()) {
            String text = this.fieldAnnotation(table, field);
            annotation.put(field.getName(), text);
        }
        templateContext.put("annotation", annotation);
    }

    private String findPrimaryKey(TableSetter table) {
        for (TableField field : table.getFields()) {
            if (field.isPrimary()) {
                return field.getName();
            }
        }
        return "null";
    }

    private void putInterfaces(Map<String, Object> templateContext, TableSetter table, Map<String, String> interfaces) {
        if (interfaces == null || interfaces.size() == 0) {
            return;
        }
        templateContext.put("interface", interfaces.values().stream().map(i -> "import " + i + ";").collect(joining("\n")));
        templateContext.put("interfaceName", interfaces.keySet().stream().map(str -> super.replace(str, table.getContext(), "${entity}"))
            .collect(joining(", ", ", ", ""))
        );
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
            switch (field.getCategory()) {
                case GmtCreate:
                    text.quotas(", insert = 'now()'");
                    break;
                case GmtModified:
                    text.quotas(", insert = 'now()', update = 'now()'");
                    break;
                case IsDeleted:
                    text.quotas(", insert = '0'");
                    break;
                default:
            }
            text.append(")");
        }
        return text.toString();
    }
}