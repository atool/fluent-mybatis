package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.processor.filer.RefsFile;
import cn.org.atool.fluent.mybatis.processor.filer.refs.*;
import cn.org.atool.fluent.mybatis.processor.filer.segment.*;
import cn.org.atool.generator.javafile.AbstractFile;
import cn.org.atool.generator.util.GeneratorHelper;
import com.squareup.javapoet.JavaFile;
import lombok.Getter;

import javax.annotation.processing.Filer;
import java.util.*;
import java.util.function.Consumer;

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
        return getSamePackage() + ".refs";
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
                List<JavaFile> javaFiles = generateJavaFile(fluent);
                for (JavaFile javaFile : javaFiles) {
                    javaFile.writeTo(filer);
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
            new MapperRefFiler(),
            new MappingRefFiler(),
            new QueryRefFiler(),
            new SetterRefFiler(),
            new RefsFile()
        );
    }

    /**
     * 生成java文件
     *
     * @param fluent
     */
    private static List<JavaFile> generateJavaFile(FluentEntity fluent) {
        return Arrays.asList(
            new MapperFiler(fluent).javaFile(),
            new MappingFiler(fluent).javaFile(),
            new EntityHelperFiler(fluent).javaFile(),
            new SqlProviderFiler(fluent).javaFile(),
            new WrapperHelperFiler(fluent).javaFile(),
            new QueryFiler(fluent).javaFile(),
            new UpdaterFiler(fluent).javaFile(),
            new BaseDaoFiler(fluent).javaFile(),
            new DefaultsFiler(fluent).javaFile()
        );
    }
}