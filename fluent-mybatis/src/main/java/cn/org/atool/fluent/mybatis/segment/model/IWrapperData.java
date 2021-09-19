package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;

/**
 * IWrapperData: 提供给xml文件调用的方法
 *
 * @author darui.wu
 */
public interface IWrapperData {
    /**
     * 是否distinct查询
     *
     * @return ignore
     */
    boolean isDistinct();

    /**
     * 查询条件 SQL 片段
     *
     * @return 查询字段列表
     */
    IFragment select();

    /**
     * (update)
     * set
     * column1 = value1,
     * column2 = value2
     *
     * @return 更新语句
     */
    IFragment update();

    /**
     * 返回where部分sql
     *
     * @return ignore
     */
    default JoiningFrag where() {
        return this.segments().where.getSegments();
    }

    /**
     * 返回 groupBy + having  组合起来的语句
     *
     * @return ignore
     */
    default JoiningFrag groupBy() {
        return this.segments().groupBy.getSegments();
    }

    default JoiningFrag having() {
        return this.segments().having.getSegments();
    }

    /**
     * 返回 groupBy + having + last 组合起来的语句
     *
     * @return ignore
     */
    default JoiningFrag orderBy() {
        return this.segments().orderBy.getSegments();
    }

    /**
     * where + groupBy + having + orderBy + limit + last 语句部分
     *
     * @return where sql
     */
    default IFragment getMerged() {
        return segments().get();
    }

    /**
     * select ... from table where ...
     * 不包含分页部分
     *
     * @param withPaged 是否带上分页部分语法
     * @return select ... from table where ...
     */
    IFragment sql(boolean withPaged);

    /**
     * 返回last sql部分
     *
     * @return ignore
     */
    default IFragment last() {
        return CachedFrag.set(this.segments().last());
    }

    /**
     * 附加sql,只允许执行一次
     *
     * @param sql 附加sql
     */
    default void last(String sql) {
        this.segments().last(sql);
    }

    MergeSegments segments();
}