package cn.org.atool.fluent.mybatis.generator.mock;

import cn.org.atool.fluent.mybatis.generator.TableColumn;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.generator.MybatisGenerator.currTable;

public class MockConfigBuilder extends MockUp<CopyConfigBuilder> {
    @Mock
    public String processName2(Invocation it, String name, NamingStrategy strategy, String[] prefix) {
        if (prefix == null) {
            return processColumnName(it, name, strategy);
        } else {
            String _name = processTableName(it, name, strategy, prefix);
            String withSuffix = NamingStrategy.capitalFirst(_name);
            currTable().setWithoutSuffixEntity(withSuffix);
            return _name;
        }
    }


    @Mock
    public List<TableInfo> processTable2(Invocation it, List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        it.proceed(tableList, strategy, config);
        for (TableInfo tableInfo : tableList) {
            String entityName = currTable().getWithoutSuffixEntity();
        }
        return tableList;
    }

    @Mock
    public TableInfo convertTableFields2(Invocation it, TableInfo tableInfo, StrategyConfig config) {
        TableInfo _tableInfo = it.proceed(tableInfo, config);
        _tableInfo.setFields(_tableInfo.getFields().stream()
                .filter(field -> !currTable().getColumn(field.getName())
                        .map(TableColumn::isExclude)
                        .orElse(false)
                ).collect(Collectors.toList())
        );
        this.sortFields(_tableInfo);
        return _tableInfo;
    }

    /**
     * 对表字段进行排序
     * o 特殊字段
     * o 其他字段字母序
     *
     * @param tableInfo
     */
    private void sortFields(TableInfo tableInfo) {
        List<TableField> fields = tableInfo.getFields();
        List<TableField> sorts = new ArrayList<>();
        TableField pkField = null;
        TableField gmtCreatedField = null;
        TableField gmtModifiedField = null;
        TableField isDeletedField = null;
        for (TableField field : fields) {
            if (field.isKeyIdentityFlag()) {
                pkField = field;
            } else if (field.getName().equals(currTable().getGmtCreateColumn())) {
                gmtCreatedField = field;
            } else if (field.getName().equals(currTable().getGmtModifiedColumn())) {
                gmtModifiedField = field;
            } else if (field.getName().equals(currTable().getLogicDeletedColumn())) {
                isDeletedField = field;
            } else {
                sorts.add(field);
            }
        }
        Collections.sort(sorts, (field1, field2) -> field1.getName().compareTo(field2.getName()));
        fields.clear();
        if (pkField != null) {
            fields.add(pkField);
        }
        if (gmtCreatedField != null) {
            fields.add(gmtCreatedField);
        }
        if (gmtModifiedField != null) {
            fields.add(gmtModifiedField);
        }
        if (isDeletedField != null) {
            fields.add(isDeletedField);
        }
        fields.addAll(sorts);
        tableInfo.setFields(fields);
    }

    private String processColumnName(Invocation it, String name, NamingStrategy strategy) {
        String propertyName = currTable().getPropertyNameByColumn(name);
        if (StringUtils.isNotBlank(propertyName)) {
            return propertyName;
        } else {
            return it.proceed(name, strategy, null);
        }
    }

    private String processTableName(Invocation it, String name, NamingStrategy strategy, String[] prefix) {
        if (StringUtils.isNotBlank(currTable().getWithoutSuffixEntity())) {
            return currTable().getWithoutSuffixEntity();
        } else {
            return it.proceed(name, strategy, prefix);
        }
    }
}
