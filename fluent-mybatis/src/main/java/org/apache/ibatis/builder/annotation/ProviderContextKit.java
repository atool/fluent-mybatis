package org.apache.ibatis.builder.annotation;

import java.lang.reflect.Method;

/**
 * ProviderContextKit: 桥接类, 用于实例化ProviderContext
 *
 * @author darui.wu
 */
public interface ProviderContextKit {
    static ProviderContext newProviderContext(Class<?> mapperType, Method mapperMethod) {
        return new ProviderContext(mapperType, mapperMethod, null);
    }
}