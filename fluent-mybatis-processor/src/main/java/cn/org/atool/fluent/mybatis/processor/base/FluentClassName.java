package cn.org.atool.fluent.mybatis.processor.base;

import cn.org.atool.fluent.mybatis.processor.entity.CommonField;
import cn.org.atool.fluent.mybatis.processor.filer.segment.*;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.squareup.javapoet.ClassName;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * fluent entity构造各模块ClassName基类
 *
 * @author darui.wu
 */
@SuppressWarnings("unused")
public abstract class FluentClassName {

    public abstract String getNoSuffix();

    /**
     * 首字母小写,不带Entity后缀的entity名称
     *
     * @return ignore
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
     * @return Entity ClassName
     */
    public ClassName entity() {
        return ClassName.get(this.getEntityPack(), this.getClassName());
    }

    /**
     * ClassName of XyzUpdater
     *
     * @return Update ClassName
     */
    public ClassName updater() {
        return ClassName.get(
            UpdaterFiler.getPackageName(this),
            UpdaterFiler.getClassName(this));
    }

    /**
     * ClassName of XyzEntityKit
     *
     * @return EntityHelper ClassName
     */
    public ClassName entityKit() {
        return ClassName.get(
            EntityKitFiler.getPackageName(this),
            EntityKitFiler.getClassName(this));
    }

    /**
     * ClassName of XyzMapper
     *
     * @return Mapper ClassName
     */
    public ClassName mapper() {
        return ClassName.get(
            MapperFiler.getPackageName(this),
            MapperFiler.getClassName(this));
    }

    /**
     * ClassName of XyzQuery
     *
     * @return Query ClassName
     */
    public ClassName query() {
        return ClassName.get(
            QueryFiler.getPackageName(this),
            QueryFiler.getClassName(this));
    }

    public ClassName wrapperHelper() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this));
    }

    public ClassName queryWhere() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_QueryWhere);
    }

    public ClassName updateWhere() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_UpdateWhere);
    }

    public ClassName selector() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_Selector);
    }

    public ClassName groupBy() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_GroupBy);
    }

    public ClassName having() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_Having);
    }

    public ClassName queryOrderBy() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_QueryOrderBy);
    }

    public ClassName updateOrderBy() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_UpdateOrderBy);
    }

    public ClassName updateSetter() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_UpdateSetter);
    }

    public ClassName segment() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_ISegment);
    }

    public ClassName formSetter() {
        return ClassName.get(
            WrapperKitFiler.getPackageName(this),
            WrapperKitFiler.getClassName(this),
            Suffix_EntityFormSetter);
    }
}