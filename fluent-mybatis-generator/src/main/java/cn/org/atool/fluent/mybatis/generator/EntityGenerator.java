package cn.org.atool.fluent.mybatis.generator;

import cn.org.atool.fluent.mybatis.annotation.ParaType;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.generator.annoatation.Interface;
import cn.org.atool.fluent.mybatis.generator.annoatation.Table;
import cn.org.atool.fluent.mybatis.generator.annoatation.Tables;
import org.test4j.generator.mybatis.config.IGlobalConfig;
import org.test4j.generator.mybatis.config.IGlobalConfigSet;
import org.test4j.generator.mybatis.config.ITableSetter;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NOT_DEFINED;
import static org.test4j.tools.commons.StringHelper.isBlank;
import static org.test4j.tools.commons.StringHelper.isNotBlank;

public class EntityGenerator {

    public static void generate(Class clazz) {
        Tables tables = (Tables) clazz.getAnnotation(Tables.class);
        if (tables.tables().length == 0) {
            throw new RuntimeException("the tables not found.");
        }
        EntityGenerator generator = new EntityGenerator(clazz, tables);
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
            t.setTablePrefix(value(table.tablePrefix(), tables.tablePrefix()));
            t.setGmtCreate(value(table.gmtCreated(), tables.gmtCreated()));
            t.setGmtModified(value(table.gmtModified(), tables.gmtModified()));
            t.setLogicDeleted(value(table.logicDeleted(), tables.logicDeleted()));
            t.setTablePrefix(value(table.tablePrefix(), tables.tablePrefix()));
            t.setMapperPrefix(value(table.mapperPrefix(), tables.mapperPrefix()));
            for (Interface dao : table.daoInterface()) {
                t.addBaseDaoInterface(dao.value(), value(dao.types()));
            }
            for (Interface entity : table.entityInterface()) {
                t.addEntityInterface(entity.value(), value(entity.types()));
            }
        };
    }

    private String[] value(ParaType[] types) {
        return Stream.of(types).map(ParaType::getVar).collect(Collectors.toList()).toArray(new String[0]);
    }

    private Class[] value(Class[] value1, Class[] value2) {
        Class[] value = isDefined(value1) ? value1 : isDefined(value2) ? value2 : new Class[0];
        return value;
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

    private boolean isDefined(Class[] value) {
        return value.length != 1 || !Objects.equals(value[0], Object.class);
    }

    private String[] getInterfaceParaTypes(Class dao) {
        TypeVariable[] types = dao.getTypeParameters();
        List<String> names = new ArrayList<>(types.length);
        for (TypeVariable type : types) {
            System.out.println(type.getName());
            if (type.getName().equals(IEntity.class.getSimpleName())) {
                System.out.println(type.getName());
            }
        }

        return names.toArray(new String[0]);
    }


    private IGlobalConfig globalConfig() {
        if (tables.test4j()) {
            return EntityGenerator2.buildWithTest();
        } else {
            return EntityGenerator2.build();
        }
    }

    private final Class mainKlass;

    private final Tables tables;

    private final String srcDir;

    private final String testDir;

    private EntityGenerator(Class mainKlass, Tables tables) {
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
