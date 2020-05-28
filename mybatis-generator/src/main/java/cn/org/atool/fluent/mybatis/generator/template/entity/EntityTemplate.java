package cn.org.atool.fluent.mybatis.generator.template.entity;

import org.test4j.generator.mybatis.config.TableField;
import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;
import org.test4j.tools.commons.StringHelper;
import org.test4j.tools.commons.TextBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_ENTITY;

/**
 * EntityTemplate
 *
 * @author darui.wu
 */
public class EntityTemplate extends BaseTemplate {
    /**
     * entity类继承的接口全称
     */
    private List<String> interfaces = new ArrayList<>();

    public EntityTemplate() {
        super("templates/entity/Entity.java.vm", "entity/*Entity.java");
    }

    @Override
    public String getTemplateId() {
        return KEY_ENTITY;
    }

    public EntityTemplate addInterface(String fullName) {
        this.interfaces.add(fullName);
        return this;
    }

    @Override
    protected void templateConfigs(TableInfo table, Map<String, Object> context) {
        if (!this.interfaces.isEmpty()) {
            this.putInterfaces(context);
        }
        context.put("primaryKey", this.findPrimaryKey(table));
        Map<String, String> annotation = new HashMap<>();
        for (TableField field : table.getFields()) {
            String text = this.fieldAnnotation(table, field);
            annotation.put(field.getName(), text);
        }
        context.put("annotation", annotation);
    }

    private String findPrimaryKey(TableInfo table) {
        for (TableField field : table.getFields()) {
            if (field.isPrimary()) {
                return field.getName();
            }
        }
        return "null";
    }

    private void putInterfaces(Map<String, Object> context) {
        context.put("interface",
            this.interfaces.stream().map(i -> "import " + i + ";").collect(joining("\n"))
        );
        context.put("interfaceName",
            this.interfaces.stream().map(i -> {
                int last = i.lastIndexOf('.');
                return i.substring(last + 1);
            }).collect(joining(" ,"))
        );
    }

    private String fieldAnnotation(TableInfo table, TableField field) {
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