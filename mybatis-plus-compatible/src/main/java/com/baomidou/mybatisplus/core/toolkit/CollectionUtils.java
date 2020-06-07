package com.baomidou.mybatisplus.core.toolkit;

import java.util.Collection;

/**
 * CollectionUtils
 *
 * @author darui.wu
 * @create 2020/6/3 3:26 下午
 */
@Deprecated
public class CollectionUtils {
    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }
}