package cn.org.atool.fluent.mybatis.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;

/**
 * @author darui.wu
 */
@Accessors(chain = true)
public class TableConvertor {
    @Getter
    private Map<String, Table> tables = new HashMap<>();

    @Setter
    @Getter
    private String[] prefix;

    @Setter
    @Getter
    private String entitySuffix = "Entity";

    @Setter
    private DataSourceConfig dataSourceConfig;

    @Setter
    private MybatisGenerator mybatisGenerator;

    @Setter
    @Getter
    private List<Class> modelInterface = new ArrayList<>();

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

    public TableConvertor addTable(Table table) {
        table.setConvertor(this);
        this.tables.put(table.getTableName(), table);
        return this;
    }

    public TableConvertor addTable(String tableName) {
        Table table = new Table(this, tableName);
        this.tables.put(tableName, table);
        return this;
    }

    public TableConvertor addTable(String tableName, boolean isPartition) {
        Table table = new Table(this, tableName);
        if (isPartition) {
            table.isPartition();
        }
        this.tables.put(tableName, table);
        return this;
    }

    public TableConvertor setDataSource(String url, String username, String password) {
        return this.setDataSource(url, username, password, null);
    }

    public TableConvertor setDataSource(String url, String username, String password, ITypeConvert typeConvert) {
        this.dataSourceConfig = MybatisGenerator.buildDataSourceConfig(DbType.MYSQL, "com.mysql.jdbc.Driver", url, username, password, typeConvert);
        return this;
    }

    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig == null ? mybatisGenerator.getDataSourceConfig() : this.dataSourceConfig;
    }

    public TableConvertor addModelInterface(Class klass) {
        this.modelInterface.add(klass);
        return this;
    }

    public String getInterfacePacks() {
        return modelInterface.stream()
                .map(klass -> "import " + klass.getName() + ";")
                .collect(joining("\n"));
    }

    public String getInterfaceNames() {
        return modelInterface.stream()
                .map(Class::getSimpleName)
                .collect(joining(", "));
    }
}

