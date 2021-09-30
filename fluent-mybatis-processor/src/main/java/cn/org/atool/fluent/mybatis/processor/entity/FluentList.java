package cn.org.atool.fluent.mybatis.processor.entity;

import cn.org.atool.fluent.mybatis.metadata.DbType;
import cn.org.atool.fluent.mybatis.processor.filer.AbstractFiler;
import cn.org.atool.fluent.mybatis.processor.filer.refs.EntityRelationFiler;
import cn.org.atool.fluent.mybatis.processor.filer.refs.MapperRefFiler;
import cn.org.atool.fluent.mybatis.processor.filer.refs.QueryRefFiler;
import cn.org.atool.fluent.mybatis.processor.filer.refs.RefFiler;
import cn.org.atool.fluent.mybatis.processor.filer.segment.*;
import cn.org.atool.generator.javafile.AbstractFile;
import cn.org.atool.generator.util.GeneratorHelper;
import lombok.Getter;

import javax.annotation.processing.Filer;
import java.util.*;
import java.util.function.Consumer;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.NEWLINE;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.Ref_Package;
import static cn.org.atool.generator.util.GeneratorHelper.sameStartPackage;

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
     */
    @Getter
    private static final List<FluentEntity> fluents = new ArrayList<>();

    private static final Map<String, FluentEntity> map = new HashMap<>();

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
        return Ref_Package;
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
        fluents.sort(Comparator.comparing(FluentEntity::getNoSuffix));
        boolean first = true;
        for (FluentEntity fluent : FluentList.getFluents()) {
            try {
                List<AbstractFiler> javaFiles = generateJavaFile(fluent);
                for (AbstractFiler javaFile : javaFiles) {
                    javaFile.javaFile().writeTo(filer);
                }
            } catch (Exception e) {
                logger.accept("FluentEntityInfo:" + fluent + NEWLINE + GeneratorHelper.toString(e));
                throw new RuntimeException(e);
            }
            if (first) {
                dbType = fluent.getDbType();
            } else if (dbType != null && !Objects.equals(dbType, fluent.getDbType())) {
                // 如果有多个数据源, 设置为未知态
                dbType = null;
            }
            first = false;
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
            new EntityRelationFiler(),
            new QueryRefFiler(),
            new MapperRefFiler(),
            new RefFiler()
        );
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

    private static DbType dbType;

    public static String getDbType() {
        return dbType == null ? null : dbType.name();
    }
}