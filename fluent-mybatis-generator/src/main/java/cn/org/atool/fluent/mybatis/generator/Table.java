package cn.org.atool.fluent.mybatis.generator;

import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Accessors(chain = true)
public class Table implements Comparable<Table> {
    @Setter
    private TableConvertor convertor;

    private final String tableName;

    @Setter
    private String withoutSuffixEntity;

    private String gmtCreateColumn;

    private String gmtModifiedColumn;

    private String logicDeletedColumn;

    private String versionColumn;

    @Setter
    private boolean isPartition = false;

    /**
     * mapper类bean名称前缀
     */
    @Setter
    private String mapperPrefix = "";

    /**
     * base dao 导入的自定义接口
     * key: implements 接口完整定义，包含泛型
     * value: 接口import完整路径
     */
    @Setter
    @Getter
    private Map<String, String> baseDaoInterfaces = new HashMap<>();

    private Map<String, TableColumn> columns = new HashMap<String, TableColumn>();

    public Table(String tableName) {
        this.tableName = tableName;
    }

    public Table(TableConvertor convertor, String tableName) {
        this.convertor = convertor;
        this.tableName = tableName;
    }

    public Table(TableConvertor convertor, String tableName, String withoutSuffixEntity) {
        this.convertor = convertor;
        this.tableName = tableName;
        this.withoutSuffixEntity = withoutSuffixEntity;
    }

    public String getWithSuffixEntity() {
        return this.withoutSuffixEntity + convertor.getEntitySuffix();
    }

    /**
     * 指定特殊字段
     *
     * @param gmtCreateColumn    记录创建时间
     * @param gmtModifiedColumn  记录修改时间
     * @param logicDeletedColumn 记录逻辑删除字段
     * @return
     */
    public Table specColumn(String gmtCreateColumn, String gmtModifiedColumn, String logicDeletedColumn) {
        this.setGmtCreateColumn(gmtCreateColumn);
        this.setGmtModifiedColumn(gmtModifiedColumn);
        this.setLogicDeletedColumn(logicDeletedColumn);
        return this;
    }

    /**
     * 指定特殊字段
     *
     * @param gmtCreateColumn    记录创建时间
     * @param gmtModifiedColumn  记录修改时间
     * @param logicDeletedColumn 记录逻辑删除字段
     * @param versionColumn      记录版本锁字段
     * @return
     */
    public Table specColumn(String gmtCreateColumn, String gmtModifiedColumn, String logicDeletedColumn, String versionColumn) {
        this.setGmtCreateColumn(gmtCreateColumn);
        this.setGmtModifiedColumn(gmtModifiedColumn);
        this.setLogicDeletedColumn(logicDeletedColumn);
        this.versionColumn = versionColumn;
        return this;
    }


    public Table setGmtCreateColumn(String gmtCreateColumn) {
        if (StringUtils.isEmpty(this.gmtCreateColumn)) {
            this.gmtCreateColumn = gmtCreateColumn;
        }
        return this;
    }

    public Table setGmtModifiedColumn(String gmtModifiedColumn) {
        if (StringUtils.isEmpty(this.gmtModifiedColumn)) {
            this.gmtModifiedColumn = gmtModifiedColumn;
        }
        return this;
    }

    public Table setLogicDeletedColumn(String logicDeletedColumn) {
        if (StringUtils.isEmpty(this.logicDeletedColumn)) {
            this.logicDeletedColumn = logicDeletedColumn;
        }
        return this;
    }

    public Table setVersionColumn(String versionColumn) {
        if (StringUtils.isEmpty(this.versionColumn)) {
            this.versionColumn = versionColumn;
        }
        return this;
    }


    public Map<String, String> findFieldConfig() {
        Map<String, String> config = new HashMap<>();
        {
            config.put("entityPrefixName", this.withoutSuffixEntity);
            config.put("field_gmtCreated", this.gmtCreateColumn);
            config.put("field_gmtModified", this.gmtModifiedColumn);
            config.put("field_logicDeleted", this.logicDeletedColumn);
            config.put("field_version", this.versionColumn);
        }
        return config;
    }

    public Table column(String columnName, IColumnType columnType) {
        this.columns.put(columnName, new TableColumn(columnName, null, columnType));
        return this;
    }

    public Table column(String columnName, String propertyName) {
        this.columns.put(columnName, new TableColumn(columnName, propertyName, null));
        return this;
    }

    public Table exclude(String... columnNames) {
        for (String column : columnNames) {
            this.columns.put(column, new TableColumn(column).setExclude(true));
        }
        return this;
    }

    public Optional<TableColumn> getColumn(String column) {
        return Optional.ofNullable(this.columns.get(column));
    }

    public IColumnType columnType(String column) {
        return this.getColumn(column).map(TableColumn::getColumnType).orElse(null);
    }


    public String getPropertyNameByColumn(String column) {
        return this.getColumn(column).map(TableColumn::getPropertyName).orElse(null);
    }

    @Override
    public int compareTo(Table table) {
        return this.tableName.compareTo(table.getTableName());
    }

    public Table addBaseDaoInterface(String interfaceName, String interfacePackage) {
        this.baseDaoInterfaces.put(interfaceName, interfacePackage);
        return this;
    }
}
