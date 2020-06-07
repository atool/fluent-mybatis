package com.baomidou.mybatisplus.core.toolkit;

import cn.org.atool.fluent.mybatis.util.MybatisUtil;

/**
 * StringUtils
 *
 * @author darui.wu
 * @create 2020/6/3 3:27 下午
 */
@Deprecated
public class StringUtils {
    public static boolean isEmpty(final CharSequence cs) {
        return MybatisUtil.isEmpty(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return MybatisUtil.isNotEmpty(cs);
    }
}