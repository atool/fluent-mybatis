package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.processor.filer.*;
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
        if (!fluents.isEmpty()) {
            try {
                new RefsFile().writeTo(filer);
            } catch (Exception e) {
                logger.accept("Generate Refs error:\n" + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 生成java文件
     *
     * @param fluent
     */
    private static List<JavaFile> generateJavaFile(FluentEntity fluent) {
        List<JavaFile> files = new ArrayList<>();
        files.add(new MapperFiler(fluent).javaFile());
        files.add(new MappingFiler(fluent).javaFile());
        files.add(new EntityHelperFiler(fluent).javaFile());
        files.add(new SqlProviderFiler(fluent).javaFile());
        files.add(new WrapperHelperFiler(fluent).javaFile());
        files.add(new QueryFiler(fluent).javaFile());
        files.add(new UpdaterFiler(fluent).javaFile());
        files.add(new BaseDaoFiler(fluent).javaFile());
        files.add(new WrapperDefaultFiler(fluent).javaFile());
        return files;
    }
}