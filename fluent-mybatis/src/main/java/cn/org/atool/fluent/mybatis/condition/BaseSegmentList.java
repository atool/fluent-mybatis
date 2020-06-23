package cn.org.atool.fluent.mybatis.condition;

import cn.org.atool.fluent.mybatis.interfaces.ISqlSegment;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.condition.model.StrConstant.EMPTY;
import static java.util.stream.Collectors.joining;

/**
 * SQL 片段集合 处理的抽象类
 *
 * @author darui.wu
 */
public abstract class BaseSegmentList {
    /**
     * sql片段列表
     */
    protected final List<ISqlSegment> segments = new ArrayList<>();

    /**
     * 添加sql片段
     *
     * @param first       sql片段
     * @param sqlSegments sql片段列表
     * @return self
     */
    public abstract BaseSegmentList add(ISqlSegment first, ISqlSegment... sqlSegments);

    /**
     * 添加sql片段
     *
     * @param sqlSegments 元素集合
     * @return self
     */
    protected final BaseSegmentList addAll(ISqlSegment... sqlSegments) {
        Stream.of(sqlSegments).forEach(this.segments::add);
        cache = null;
        return this;
    }

    /**
     * 缓存构建好的sql
     */
    @Getter(AccessLevel.NONE)
    protected String cache = null;

    /**
     * 返回合并后的sql语句
     *
     * @return sql
     */
    public final String sql() {
        if (cache == null) {
            cache = this.build();
        }
        return cache;
    }

    /**
     * 构建sql语句
     *
     * @return sql
     */
    protected abstract String build();

    /**
     * 合并处理
     *
     * @param prefix 前缀
     * @return sql
     */
    protected String merge(String prefix, String delimiter) {
        if (segments.isEmpty()) {
            return EMPTY;
        } else {
            return segments.stream()
                .map(ISqlSegment::getSqlSegment)
                .filter(sql -> !sql.isEmpty())
                .collect(joining(delimiter, prefix, EMPTY));
        }
    }
}