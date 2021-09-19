package cn.org.atool.fluent.mybatis.segment.fragment;

import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.*;

/**
 * 常量代码片段
 *
 * @author darui.wu
 */
public interface Fragments {
    Column EMPTY_COLUMN = Column.set(EMPTY, EMPTY);

    CachedFrag SEG_SPACE = CachedFrag.set(SPACE);
    /**
     * 单字符 '*'
     */
    CachedFrag SEG_ASTERISK = CachedFrag.set(ASTERISK);
    /**
     * 空字符
     */
    CachedFrag SEG_EMPTY = CachedFrag.set(EMPTY);
    /**
     * count(1)
     */
    CachedFrag SEG_COUNT_1 = CachedFrag.set("COUNT(1)");

    Function<IFragment, FormatFrag> SUM = frag -> FormatFrag.format("SUM(%)", frag);
}