package cn.org.atool.mybatis.fluent.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import org.apache.commons.lang3.StringUtils;
import org.test4j.module.core.utility.MessageHelper;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Accessors(chain = true)
@Slf4j
public class MybatisGenerator {
    private static ThreadLocal<Table> currTable = new ThreadLocal<>();

    /**
     * 代码作者
     */
    @Setter
    private String author = "generate code";
    /**
     * 代码生成路径
     */
    @Getter
    private String outputDir = System.getProperty("user.dir") + "/target/generate/base";

    /**
     * 测试代码生成路径
     */
    @Getter
    private String testOutputDir = System.getProperty("user.dir") + "/target/generate/test";

    /**
     * dao代码生成路径
     */
    @Getter
    private String daoOutputDir = System.getProperty("user.dir") + "/target/generate/dao";
    /**
     * 代码package前缀
     */
    private String basePackage;
    /**
     * 项目dao类的基础package
     */
    private String daoBasePackage;

    private DataSourceConfig dataSourceConfig;

    @Setter
    private boolean activeRecord = false;

    @Setter
    private List<Class> modelInterface = new ArrayList<>();

    /**
     * base dao 导入的自定义接口
     * key: implements 接口完整定义，包含泛型
     * value: 接口import完整路径
     */
    @Setter
    @Getter
    private Map<String, String> baseDaoInterfaces = new HashMap<>();

    @Setter
    private boolean isEntitySetChain = true;
    /**
     * mapper类bean名称前缀
     */
    @Setter
    private String mapperPrefix = "";


    public MybatisGenerator(String basePackage) {
        this.basePackage = basePackage;
        this.daoBasePackage = basePackage;
    }

    public MybatisGenerator(String basePackage, String daoBasePackage) {
        this.basePackage = basePackage;
        this.daoBasePackage = daoBasePackage;
    }

    public Table currTable() {
        return currTable.get();
    }

    public void generate(TableConvertor tables) {
        List<GenerateObj> generateObjs = new ArrayList<>();
        doMock();
        List<Table> list = new ArrayList<>();
        list.addAll(tables.getTables().values());
        Collections.sort(list);

        for (Table table : list) {
            currTable.set(table);
            MessageHelper.info("begin to generate table:" + table.getTableName());
            this.generate(tables, new String[]{table.getTableName()}, table.getVersionColumn());
            generateObjs.add(GenerateObj.init(table));
            log.info("generate table {} successful.", table.getTableName());
        }
        currTable.remove();
        GenerateObj.generate(generateObjs, testOutputDir, basePackage);
    }

    public MybatisGenerator addModelInterface(Class klass) {
        this.modelInterface.add(klass);
        return this;
    }

    public MybatisGenerator setOutputDir(String outputDir, String testOutputDir, String daoOutputDir) {
        if (StringUtils.isNotBlank(outputDir)) {
            this.outputDir = outputDir;
        }
        if (StringUtils.isNotBlank(testOutputDir)) {
            this.testOutputDir = testOutputDir;
        }
        if (StringUtils.isNotBlank(daoOutputDir)) {
            this.daoOutputDir = daoOutputDir;
        }
        return this;
    }

    public MybatisGenerator setDataSource(String url, String username, String password) {
        return this.setDataSource(url, username, password, null);
    }

    public MybatisGenerator setDataSource(String url, String username, String password, ITypeConvert typeConvert) {
        return this.setDataSource(DbType.MYSQL, "com.mysql.jdbc.Driver", url, username, password, typeConvert);
    }

    private MybatisGenerator setDataSource(DbType type, String driver, String url, String username, String password, ITypeConvert typeConvert) {
        if (url == null) {
            throw new RuntimeException("请设置数据库链接信息 url");
        }
        this.dataSourceConfig = new DataSourceConfig()
                .setDbType(type)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(driver);
        if (typeConvert != null) {
            this.dataSourceConfig.setTypeConvert(typeConvert);
        }
        return this;
    }

    /**
     * 生成mybatis模板
     * <p/>
     * 如果多张表的策略不一致， 可以把表分开重复调用此方法
     *
     * @param tables
     * @param tableNames 生成表列表
     * @param verField   乐观锁字段
     */
    private void generate(TableConvertor tables, String[] tableNames, String verField) {
        new AutoGenerator()
                .setGlobalConfig(this.initGlobalConfig(tables.getEntitySuffix()))
                .setDataSource(this.dataSourceConfig)
                .setPackageInfo(this.initPackageConfig())
                .setTemplate(this.initTemplate())
                .setStrategy(this.initStrategy(tables.getPrefix(), tableNames, verField))
                .setCfg(this.initInjectConfig())
                .execute();
    }

    public String getPackage(TemplateFile.TemplateType type) {
        if (TemplateFile.TemplateType.Dao.equals(type) && this.daoBasePackage != null) {
            return this.daoBasePackage;
        } else {
            return this.basePackage;
        }
    }

    /**
     * 初始化全局配置
     *
     * @param entitySuffix 数据库模型实体类后缀
     * @return
     */
    private GlobalConfig initGlobalConfig(String entitySuffix) {
        return new GlobalConfig()
                .setAuthor(this.author)
                .setOutputDir(this.outputDir)
                .setFileOverride(true)
                .setActiveRecord(this.activeRecord)
                .setEnableCache(false)
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setDateType(DateType.ONLY_DATE)
                .setOpen(false)
                .setEntityName("%s" + entitySuffix);
    }

    private InjectionConfig initInjectConfig() {
        Map<String, Object> config = new HashMap<>();
        {
            config.put("chainSet", this.isEntitySetChain);
            config.putAll(currTable.get().findFieldConfig());

        }
        if (CollectionUtils.isNotEmpty(modelInterface)) {
            config.put("interface", true);
            config.put("interfacePack", this.getInterfacePacks());
            config.put("interfaceName", this.getInterfaceNames());
        }
        if (this.mapperPrefix != null && !"".equals(this.mapperPrefix.trim())) {
            config.put("mapperPrefix", this.mapperPrefix);
        }
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(config);
            }
        };
        cfg.setFileOutConfigList(TemplateFile.parseConfigList(this, config));
        GenerateObj.setCurrConfig(config);
        return cfg;
    }



    private String getInterfacePacks() {
        return modelInterface.stream()
                .map(klass -> "import " + klass.getName() + ";")
                .collect(joining("\n"));
    }

    private String getInterfaceNames() {
        return modelInterface.stream()
                .map(Class::getSimpleName)
                .collect(joining(", "));
    }


    private StrategyConfig initStrategy(String[] tablePrefix, String[] tables, String verField) {
        StrategyConfig sc = new StrategyConfig();
        sc.setCapitalMode(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setEntityTableFieldAnnotationEnable(true);
        if (StringUtils.isNotBlank(verField)) {
            sc.setVersionFieldName(verField);
        }
        if (tables != null && tables.length > 0) {
            sc.setInclude(tables);
        }
        if (tablePrefix != null) {
            sc.setTablePrefix(tablePrefix);
        }
        return sc;
    }

    /**
     * 初始化模板路径，允许覆盖，可以拷贝源码 resources/templates下面文件修改
     * <p/>
     * 如果任何一个模板设置为空或者null， 则不生成模板
     *
     * @return
     */
    private TemplateConfig initTemplate() {
        TemplateConfig tc = new TemplateConfig();
        {
            tc.setEntity("/templates/entity2.java.vm");
            tc.setMapper("/templates/mapper2.java.vm");
            tc.setXml(null);
            tc.setController(null);
            tc.setService(null);
            tc.setServiceImpl(null);
        }
        return tc;
    }

    private PackageConfig initPackageConfig() {
        return new PackageConfig()
                .setParent(this.basePackage)
                .setEntity("entity")
                .setService("dao")
                .setServiceImpl("dao.impl");
    }

    public MybatisGenerator addBaseDaoInterface(String interfaceName, String interfacePackage) {
        this.baseDaoInterfaces.put(interfaceName, interfacePackage);
        return this;
    }

    /**
     * 如果相关类已经mock了，则不执行操作
     * <p/>
     * 这里设置为静态类，避免Generator被多次执行
     */
    private static void doMock() {
        if (MockFlag.flag()) {
            return;
        }
        new MockUp<ConfigBuilder>() {
            @Mock
            public String processName(Invocation it, String name, NamingStrategy strategy, String[] prefix) {
                if (prefix == null) {
                    return processColumnName(it, name, strategy);
                } else {
                    String _name = processTableName(it, name, strategy, prefix);
                    String withSuffix = NamingStrategy.capitalFirst(_name);
                    currTable.get().setWithoutSuffixEntity(withSuffix);
                    return _name;
                }
            }

            private String processColumnName(Invocation it, String name, NamingStrategy strategy) {
                String propertyName = currTable.get().getPropertyNameByColumn(name);
                if (StringUtils.isNotBlank(propertyName)) {
                    return propertyName;
                } else {
                    return it.proceed(name, strategy, null);
                }
            }

            private String processTableName(Invocation it, String name, NamingStrategy strategy, String[] prefix) {
                if (StringUtils.isNotBlank(currTable.get().getWithoutSuffixEntity())) {
                    return currTable.get().getWithoutSuffixEntity();
                } else {
                    return it.proceed(name, strategy, prefix);
                }
            }

            @Mock
            public List<TableInfo> processTable(Invocation it, List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
                it.proceed(tableList, strategy, config);
                for (TableInfo tableInfo : tableList) {
                    String entityName = currTable.get().getWithoutSuffixEntity();
                }
                return tableList;
            }

            @Mock
            public TableInfo convertTableFields(Invocation it, TableInfo tableInfo, NamingStrategy strategy) {
                TableInfo _tableInfo = it.proceed(tableInfo, strategy);
                _tableInfo.setFields(_tableInfo.getFields().stream()
                        .filter(field -> !currTable.get().getColumn(field.getName())
                                .map(TableColumn::isExclude)
                                .orElse(false)
                        ).collect(Collectors.toList())
                );
                return _tableInfo;
            }
        };

        new MockUp<TableField>() {
            @Mock
            public TableField setColumnType(Invocation it, final IColumnType columnType) {
                String name = ((TableField) it.getInvokedInstance()).getName();
                IColumnType specType = currTable.get().columnType(name);
                return it.proceed(specType == null ? columnType : specType);
            }

            @Mock
            public String getComment(Invocation it) {
                String comment = it.proceed();
                return comment == null ? null : comment.replaceAll("\\/\\\\", "");
            }

            @Mock
            public String getCapitalName(Invocation it) {
                String propertyName = ((TableField) it.getInvokedInstance()).getPropertyName();
                if (propertyName.length() <= 1) {
                    return propertyName.toUpperCase();
                }
                // 第一个字母小写， 第二个字母大写，特殊处理
                String firstChar = propertyName.substring(0, 1);
                if (Character.isLowerCase(firstChar.toCharArray()[0]) && Character.isUpperCase(propertyName.substring(1, 2).toCharArray()[0])) {
                    return firstChar.toLowerCase() + propertyName.substring(1);
                } else {
                    return firstChar.toUpperCase() + propertyName.substring(1);
                }
            }
        };

        new MockUp<MockFlag>() {
            @Mock
            public boolean flag() {
                return true;
            }
        };
    }
}