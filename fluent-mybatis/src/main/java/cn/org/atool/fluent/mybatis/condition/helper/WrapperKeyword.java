package cn.org.atool.fluent.mybatis.condition.helper;

import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;
import cn.org.atool.fluent.mybatis.util.Constants;

/**
 * wrapper 内部使用枚举
 *
 * @author darui.wu
 */
public enum WrapperKeyword implements ISqlSegment {
    /**
     * 只用作于辨识,不用于其他
     */
    APPLY(null),
    LEFT_BRACKET(Constants.LEFT_BRACKET),
    RIGHT_BRACKET(Constants.RIGHT_BRACKET);

    private final String keyword;

    WrapperKeyword(final String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getSqlSegment() {
        return keyword;
    }
}