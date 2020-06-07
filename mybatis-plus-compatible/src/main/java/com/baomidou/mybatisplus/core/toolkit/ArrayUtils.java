package com.baomidou.mybatisplus.core.toolkit;

import cn.org.atool.fluent.mybatis.util.MybatisUtil;

/**
 * ArrayUtils
 *
 * @author darui.wu
 * @create 2020/6/3 11:57 上午
 */
@Deprecated
public class ArrayUtils {
    public static boolean isNotEmpty(Object[] array) {
        return MybatisUtil.isNotEmpty(array);
    }
}