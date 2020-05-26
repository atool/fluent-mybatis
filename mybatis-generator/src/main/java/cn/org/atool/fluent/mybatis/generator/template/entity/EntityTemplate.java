package cn.org.atool.fluent.mybatis.generator.template.entity;

import org.test4j.generator.mybatis.config.TableInfo;
import org.test4j.generator.mybatis.template.BaseTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.test4j.generator.mybatis.config.constant.ConfigKey.KEY_ENTITY;

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
        if (this.interfaces.isEmpty()) {
            return;
        }
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
}
