package cn.org.atool.fluent.mybatis.generator.generator;

import cn.org.atool.fluent.mybatis.generator.annoatation.Column;
import cn.org.atool.fluent.mybatis.generator.annoatation.Table;
import cn.org.atool.fluent.mybatis.generator.annoatation.Tables;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.test4j.generator.mybatis.config.IGlobalConfig;
import org.test4j.generator.mybatis.config.IGlobalConfigSet;
import org.test4j.generator.mybatis.config.ITableSetter;

import java.util.Objects;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NOT_DEFINED;
import static org.test4j.tools.commons.StringHelper.isBlank;

/**
 * 根据注解生成Entity文件
 *
 * @author wudarui
 */
public class EntityAnnotationGenerator {

    public static void generate(Class clazz) {
        Tables tables = (Tables) clazz.getAnnotation(Tables.class);
        if (tables.tables().length == 0) {
            throw new RuntimeException("the @Tables Annotation not found.");
        }
        EntityAnnotationGenerator generator = new EntityAnnotationGenerator(clazz, tables);
        generator.globalConfig()
            .globalConfig(generator.getGlobalConfig(tables))
            .tables(tc -> {
                for (Table table : tables.tables()) {
                    for (String tableName : table.value()) {
                        Consumer<ITableSetter> consumer = generator.getTableConfig(table);
                        tc.table(tableName, consumer);
                    }
                }
            })
            .execute();
    }

    private Consumer<IGlobalConfigSet> getGlobalConfig(Tables tables) {
        return g -> {
            g.setDataSource(tables.url(), tables.username(), tables.password());
            g.setOutputDir(this.srcDir, this.testDir, this.srcDir);
            g.setBasePackage(tables.entityPack());
            g.setDaoPackage(tables.daoPack());
        };
    }

    private Consumer<ITableSetter> getTableConfig(Table table) {
        return t -> {
            if (table.excludes().length > 0) {
                t.setExcludes(table.excludes());
            }
            t.setGmtCreate(value(table.gmtCreated(), tables.gmtCreated()));
            t.setGmtModified(value(table.gmtModified(), tables.gmtModified()));
            t.setLogicDeleted(value(table.logicDeleted(), tables.logicDeleted()));
            t.setSeqName(table.seqName());
            t.setTablePrefix(value(table.tablePrefix(), tables.tablePrefix()));
            t.setMapperPrefix(value(table.mapperPrefix(), tables.mapperPrefix()));
            for (Class dao : table.daoInterface()) {
                t.addBaseDaoInterface(dao);
            }
            for (Class entity : table.entityInterface()) {
                t.addEntityInterface(entity);
            }
            for (Column column : table.columns()) {
                t.setColumn(column.value(), c -> {
                    c.setFieldName(column.property());
                    c.setInsert(column.insert());
                    c.setUpdate(column.update());
                    if (column.isLarge()) {
                        c.setLarge();
                    }
                    if (!Objects.equals(column.javaType(), Object.class)) {
                        c.setJavaType(column.javaType());
                    }
                    if (!Objects.equals(column.typeHandler(), UnknownTypeHandler.class)) {
                        c.setTypeHandler(column.typeHandler());
                    }
                });
            }
        };
    }

    private String value(String value1, String value2) {
        String value = !NOT_DEFINED.equals(value1) ? value1 : NOT_DEFINED.equals(value2) ? "" : value2;
        return value;
    }

    private String[] value(String[] value1, String[] value2) {
        String[] value = isDefined(value1) ? value1 : isDefined(value2) ? value2 : new String[0];
        return value;
    }

    private boolean isDefined(String[] value) {
        return value.length != 1 || !Objects.equals(value[0], NOT_DEFINED);
    }

    private IGlobalConfig globalConfig() {
        if (isBlank(tables.testDir())) {
            return EntityApiGenerator.build();
        } else {
            return EntityApiGenerator.buildWithTest();
        }
    }

    private final Class mainKlass;

    private final Tables tables;

    private final String srcDir;

    private final String testDir;

    private EntityAnnotationGenerator(Class mainKlass, Tables tables) {
        this.mainKlass = mainKlass;
        this.tables = tables;
        this.srcDir = System.getProperty("user.dir") + "/" + tables.srcDir() + "/";
        this.testDir = System.getProperty("user.dir") + "/" + tables.testDir() + "/";
    }

    private String getPath(String pack, boolean isSrc) {
        if (isBlank(pack)) {
            pack = this.mainKlass.getPackage().getName();
        }
        return (isSrc ? this.srcDir : this.testDir) + pack.replace('.', '/');
    }

    private String testPah() {
        return getPath(tables.entityPack(), false);
    }

    private String daoPath() {
        return getPath(tables.daoPack(), true);
    }

    private String entityPath() {
        return getPath(tables.entityPack(), true);
    }
}
