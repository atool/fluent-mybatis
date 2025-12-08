package cn.org.atool.fluent.processor.mybatis.base;

import cn.org.atool.fluent.processor.mybatis.entity.CommonField;
import cn.org.atool.fluent.processor.mybatis.filer.segment.*;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import com.palantir.javapoet.ClassName;
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

    /**
     * getNoSuffix
     *
     * @return String
     */
    public abstract String getNoSuffix();

    /**
     * 首字母小写,不带Entity后缀的entity名称
     *
     * @return ignore
     */
    public String lowerNoSuffix() {
        return MybatisUtil.lowerFirst(this.getNoSuffix(), "");
    }

    /**
     * getBasePack
     *
     * @return String
     */
    public abstract String getBasePack();

    /**
     * getEntityPack
     *
     * @return String
     */
    public abstract String getEntityPack();

    /**
     * getPackageName
     *
     * @param suffix 后缀
     * @return String
     */
    public String getPackageName(String suffix) {
        return this.getBasePack() + "." + suffix;
    }

    /**
     * getClassName
     *
     * @return String
     */
    public abstract String getClassName();

    /**
     * getFields
     *
     * @return List
     */
    public abstract List<CommonField> getFields();

    /**
     * 所有字段拼接在一起
     */
    @Getter(AccessLevel.NONE)
    private String All_Fields = null;

    /**
     * getAllFields
     *
     * @return String
     */
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
    public ClassName entityMapping() {
        return ClassName.get(
                EntityMappingFiler.getPackageName(this),
                EntityMappingFiler.getClassName(this));
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
     * ClassName of XyzBaseDao
     *
     * @return BaseDao ClassName
     */
    public ClassName baseDao() {
        return ClassName.get(
                BaseDaoFiler.getPackageName(this),
                BaseDaoFiler.getClassName(this));
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

    /**
     * ClassName of XyzQuery
     *
     * @return Query ClassName
     */
    public ClassName wrapperHelper() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this));
    }

    /**
     * ClassName of XyzQueryWhere
     *
     * @return Query ClassName
     */
    public ClassName queryWhere() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_QueryWhere);
    }

    /**
     * ClassName of XyzUpdateWhere
     *
     * @return Update ClassName
     */
    public ClassName updateWhere() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_UpdateWhere);
    }

    /**
     * ClassName of XyzSelector
     *
     * @return Selector ClassName
     */
    public ClassName selector() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_Selector);
    }

    /**
     * ClassName of XyzGroupBy
     *
     * @return GroupBy ClassName
     */
    public ClassName groupBy() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_GroupBy);
    }

    /**
     * ClassName of XyzHaving
     *
     * @return Having ClassName
     */
    public ClassName having() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_Having);
    }

    /**
     * ClassName of XyzQueryOrderBy
     *
     * @return QueryOrderBy ClassName
     */
    public ClassName queryOrderBy() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_QueryOrderBy);
    }

    /**
     * ClassName of XyzUpdateOrderBy
     *
     * @return UpdateOrderBy ClassName
     */
    public ClassName updateOrderBy() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_UpdateOrderBy);
    }

    /**
     * ClassName of XyzUpdateSetter
     *
     * @return UpdateSetter ClassName
     */
    public ClassName updateSetter() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_UpdateSetter);
    }

    /**
     * ClassName of XyzSegment
     *
     * @return Segment ClassName
     */
    public ClassName segment() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_ISegment);
    }

    /**
     * ClassName of XyzFormSetter
     *
     * @return FormSetter ClassName
     */
    public ClassName formSetter() {
        return ClassName.get(
                SegmentFiler.getPackageName(this),
                SegmentFiler.getClassName(this),
                Suffix_EntityFormSetter);
    }
}