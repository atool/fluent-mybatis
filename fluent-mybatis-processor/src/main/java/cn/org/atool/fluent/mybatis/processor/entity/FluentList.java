package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.RefsFile;
import cn.org.atool.fluent.mybatis.processor.filer.refs.*;
import cn.org.atool.fluent.mybatis.processor.filer.segment.*;
import cn.org.atool.generator.javafile.AbstractFile;
import cn.org.atool.generator.util.GeneratorHelper;
import lombok.Getter;

import javax.annotation.processing.Filer;
import java.util.*;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.base.IRefs.Fix_Package;
import static cn.org.atool.generator.util.GeneratorHelper.sameStartPackage;

/**
 * 所有Entity的FluentEntity信息列表
 *
 * @author darui.wu
 */
public class FluentList {
    /**
     * FluentEntity收集器
     */
    /**
     * 项目所有编译Entity类列表
     */
    @Getter
    private static List<FluentEntity> fluents = new ArrayList<>();

    private static Map<String, FluentEntity> map = new HashMap<>();

    /**
     * 所有entity对象的共同基础package
     */
    @Getter
    private static String samePackage = null;

    public static void addFluent(FluentEntity fluent) {
        map.put(fluent.getClassName(), fluent);
        fluents.add(fluent);
        samePackage = sameStartPackage(samePackage, fluent.getBasePack());
    }

    public static String refsPackage() {
//        return getSamePackage() + ".refs";
        return Fix_Package;
    }

    public static FluentEntity getFluentEntity(String entityName) {
        return map.get(entityName);
    }

    /**
     * 生成java文件
     *
     * @param filer
     * @param logger
     */
    public static void generate(Filer filer, Consumer<String> logger) {
        fluents.sort(Comparator.comparing(FluentEntity::getNoSuffix));
        for (FluentEntity fluent : FluentList.getFluents()) {
            try {
                List<AbstractFiler> javaFiles = generateJavaFile(fluent);
                for (AbstractFiler javaFile : javaFiles) {
                    javaFile.javaFile().writeTo(filer);
                }
            } catch (Exception e) {
                logger.accept("FluentEntityInfo:" + fluent + "\n" + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
        }
        if (fluents.isEmpty()) {
            return;
        }
        for (AbstractFile file : refFiles()) {
            try {
                file.writeTo(filer);
            } catch (Exception e) {
                logger.accept("Generate Refs error:\n" + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
        }
    }

    private static List<AbstractFile> refFiles() {
        return Arrays.asList(
            new AllRefFiler(),
            new MappingRefFiler(),
            new QueryRefFiler(),
            new FormRefFiler(),
            new RefsFile(),
            new MapperRefFiler()
        );
    }

    /**
     * 生成java文件
     *
     * @param fluent
     */
    private static List<AbstractFiler> generateJavaFile(FluentEntity fluent) {
        return Arrays.asList(
            new MapperFiler(fluent),
            new MappingFiler(fluent),
            new EntityHelperFiler(fluent),
            new SqlProviderFiler(fluent),
            new WrapperHelperFiler(fluent),
            new QueryFiler(fluent),
            new UpdaterFiler(fluent),
            new BaseDaoFiler(fluent),
            new DefaultsFiler(fluent),
            new FormSetterFiler(fluent)
        );
    }
}