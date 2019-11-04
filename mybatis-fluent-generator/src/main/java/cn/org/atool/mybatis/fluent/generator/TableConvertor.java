package cn.org.atool.mybatis.fluent.generator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
@Accessors(chain = true)
public class TableConvertor {
    private Map<String, Table> tables = new HashMap<>();

    @Setter
    private String[] prefix;

    @Setter
    @Getter
    private String entitySuffix = "Entity";

    public TableConvertor() {
        this.prefix = new String[0];
    }

    public TableConvertor(String... prefix) {
        this.prefix = prefix;
    }

    public Table table(String tableName, String entityName) {
        Table table = new Table(this, tableName, precessEntityName(entityName));
        this.tables.put(tableName, table);
        return table;
    }

    private String precessEntityName(String entityName) {
        if (entityName.endsWith(entitySuffix)) {
            return entityName.substring(0, entityName.length() - entitySuffix.length());
        } else {
            return entityName;
        }
    }

    /**
     * 对所有表统一处理
     *
     * @param consumer
     * @return
     */
    public TableConvertor allTable(Consumer<Table> consumer) {
        this.tables.values().stream().forEach(table -> consumer.accept(table));
        return this;
    }

    public Table table(String tableName) {
        Table table = new Table(this, tableName);
        this.tables.put(tableName, table);
        return table;
    }
}

