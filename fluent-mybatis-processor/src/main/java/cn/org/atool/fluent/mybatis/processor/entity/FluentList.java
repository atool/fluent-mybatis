package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.refs.RefFiler;
import cn.org.atool.fluent.mybatis.processor.filer.refs.RelationFiler;
import cn.org.atool.fluent.mybatis.processor.filer.segment.*;
import cn.org.atool.generator.javafile.AbstractFile;
import cn.org.atool.generator.util.GeneratorHelper;

import javax.annotation.processing.Filer;
import java.util.*;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.NEWLINE;

/**
 * 所有Entity的FluentEntity信息列表
 *
 * @author darui.wu
 */
public class FluentList {
    /*
     * FluentEntity收集器
     */
    /**
     * 项目所有编译Entity类列表
     * key: base package
     */
    private static final Map<String, List<FluentEntity>> fluents = new HashMap<>();

    private static final Map<String, FluentEntity> map = new HashMap<>();

    public static void addFluent(FluentEntity fluent) {
        map.put(fluent.getClassName(), fluent);
        String _package = fluent.getBasePack();
        if (!fluents.containsKey(_package)) {
            fluents.put(_package, new ArrayList<>());
        }
        fluents.get(_package).add(fluent);
    }

    public static FluentEntity getFluentEntity(String entityName) {
        return map.get(entityName);
    }

    /**
     * 生成java文件
     *
     * @param filer  Filer
     * @param logger Consumer
     */
    public static void generate(Filer filer, Consumer<String> logger) {
        for (Map.Entry<String, List<FluentEntity>> entry : fluents.entrySet()) {
            String basePackage = entry.getKey();
            List<FluentEntity> fluents = entry.getValue();
            fluents.sort(Comparator.comparing(FluentEntity::getNoSuffix));
            for (FluentEntity fluent : fluents) {
                generate(filer, logger, fluent);
            }
            for (AbstractFile file : refFiles(basePackage, fluents)) {
                generateRef(filer, logger, file);
            }
        }
    }

    private static void generateRef(Filer filer, Consumer<String> logger, AbstractFile file) {
        try {
            file.writeTo(filer);
        } catch (Exception e) {
            logger.accept("Generate Refs error:\n" + GeneratorHelper.toString(e));
            throw new RuntimeException(e);
        }
    }

    private static void generate(Filer filer, Consumer<String> logger, FluentEntity fluent) {
        try {
            List<AbstractFiler> javaFiles = generateJavaFile(fluent);
            for (AbstractFiler javaFile : javaFiles) {
                javaFile.javaFile().writeTo(filer);
            }
        } catch (Exception e) {
            logger.accept("FluentEntityInfo:" + fluent + NEWLINE + GeneratorHelper.toString(e));
            throw new RuntimeException(e);
        }
    }

    private static List<AbstractFile> refFiles(String _package, List<FluentEntity> fluents) {
        boolean hasRefMethods = false;
        for (FluentEntity fluent : fluents) {
            /* 如果没有关联关系, 不生成IEntityRelation接口 */
            if (fluent.getRefMethods().size() > 0) {
                hasRefMethods = true;
                break;
            }
        }
        List<AbstractFile> files = new ArrayList<>();
        if (hasRefMethods) {
            files.add(new RelationFiler(_package, fluents));
        }
        files.add(new RefFiler(_package, fluents));
        return files;
    }

    /**
     * 生成java文件
     *
     * @param fluent FluentEntity
     */
    private static List<AbstractFiler> generateJavaFile(FluentEntity fluent) {
        return Arrays.asList(
            new MapperFiler(fluent),
            new EntityMappingFiler(fluent),
            new SegmentFiler(fluent),
            new QueryFiler(fluent),
            new UpdaterFiler(fluent),
            new BaseDaoFiler(fluent)
        );
    }
}