package cn.org.atool.fluent.mybatis.entity.base;

import cn.org.atool.fluent.mybatis.entity.FluentEntity;
import cn.org.atool.fluent.mybatis.entity.field.CommonField;
import cn.org.atool.fluent.mybatis.entity.generator.*;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static cn.org.atool.generator.util.GeneratorHelper.sameStartPackage;

/**
 * fluent entity构造各模块ClassName基类
 *
 * @author darui.wu
 */
public abstract class FluentClassName {

    public abstract String getNoSuffix();

    /**
     * 首字母小写,不带Entity后缀的entity名称
     *
     * @return
     */
    public String lowerNoSuffix() {
        return MybatisUtil.lowerFirst(this.getNoSuffix(), "");
    }


    public abstract String getBasePack();

    public abstract String getEntityPack();

    public String getPackageName(String suffix) {
        return this.getBasePack() + "." + suffix;
    }

    public abstract String getClassName();

    public abstract List<CommonField> getFields();

    /**
     * 所有字段拼接在一起
     */
    @Getter(AccessLevel.NONE)
    private String All_Fields = null;

    public String getAllFields() {
        if (this.All_Fields == null) {
            All_Fields = this.getFields().stream().map(CommonField::getColumn).collect(Collectors.joining(", "));
        }
        return All_Fields;
    }

    // all ClassName

    /**
     * ClassName of XyzEntity
     *
     * @return
     */
    public ClassName entity() {
        return ClassName.get(this.getEntityPack(), this.getClassName());
    }

    /**
     * ClassName of XyzUpdater
     *
     * @return
     */
    public ClassName updater() {
        return ClassName.get(
            UpdaterGenerator.getPackageName(this),
            UpdaterGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzEntityHelper
     *
     * @return
     */
    public ClassName entityHelper() {
        return ClassName.get(
            EntityHelperGenerator.getPackageName(this),
            EntityHelperGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzMapper
     *
     * @return
     */
    public ClassName mapper() {
        return ClassName.get(
            MapperGenerator.getPackageName(this),
            MapperGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzMapping
     *
     * @return
     */
    public ClassName mapping() {
        return ClassName.get(
            MappingGenerator.getPackageName(this),
            MappingGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzQuery
     *
     * @return
     */
    public ClassName query() {
        return ClassName.get(
            QueryGenerator.getPackageName(this),
            QueryGenerator.getClassName(this));
    }

    /**
     * ClassName of XyzSqlProvider
     *
     * @return
     */
    public ClassName sqlProvider() {
        return ClassName.get(
            SqlProviderGenerator.getPackageName(this),
            SqlProviderGenerator.getClassName(this));
    }


    /**
     * ClassName of XyzWrapperFactory
     *
     * @return
     */
    public ClassName wrapperFactory() {
        return ClassName.get(
            WrapperDefaultGenerator.getPackageName(this),
            WrapperDefaultGenerator.getClassName(this)
        );
    }

    public ClassName queryWhere() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_QueryWhere);
    }

    public ClassName updateWhere() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_UpdateWhere);
    }

    public ClassName selector() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_Selector);
    }

    public ClassName groupBy() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_GroupBy);
    }

    public ClassName having() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_Having);
    }

    public ClassName queryOrderBy() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_QueryOrderBy);
    }

    public ClassName updateOrderBy() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_UpdateOrderBy);
    }

    public ClassName updateSetter() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_UpdateSetter);
    }

    public ClassName segment() {
        return ClassName.get(
            WrapperHelperGenerator.getPackageName(this)
                + "." +
                WrapperHelperGenerator.getClassName(this), Suffix_ISegment);
    }

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

    /**
     * 排序
     */
    public static void sort() {
        fluents.sort(Comparator.comparing(FluentEntity::getNoSuffix));
    }

    public static void addFluent(FluentEntity fluent) {
        map.put(fluent.getClassName(), fluent);
        fluents.add(fluent);
        samePackage = sameStartPackage(samePackage, fluent.getBasePack());
    }

    public static boolean notEmpty() {
        return !fluents.isEmpty();
    }

    public static FluentEntity getFluentEntity(String entityName) {
        return map.get(entityName);
    }
}