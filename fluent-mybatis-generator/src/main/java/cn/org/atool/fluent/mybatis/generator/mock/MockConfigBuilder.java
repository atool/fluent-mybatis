package cn.org.atool.fluent.mybatis.generator.mock;

import cn.org.atool.fluent.mybatis.generator.TableColumn;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.apache.commons.lang3.StringUtils;

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
    public TableInfo convertTableFields2(Invocation it, TableInfo tableInfo, NamingStrategy strategy) {
        TableInfo _tableInfo = it.proceed(tableInfo, strategy);
        _tableInfo.setFields(_tableInfo.getFields().stream()
                .filter(field -> !currTable().getColumn(field.getName())
                        .map(TableColumn::isExclude)
                        .orElse(false)
                ).collect(Collectors.toList())
        );
        return _tableInfo;
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
