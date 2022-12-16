package org.apache.ibatis.builder.annotation;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.mapper.SqlSupplier.SqlSupplierByProviderContext;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * ProviderContextKit: 桥接类, 用于实例化ProviderContext
 *
 * @author darui.wu
 */
@SuppressWarnings("rawtypes")
public final class ProviderContextKit {
    public static ProviderContext getProviderContext(Class mapperClass, String methodName) {
        if (!PROVIDER_CONTEXT.containsKey(mapperClass)) {
            Method method = methods.get(methodName);
            if (method == null) {
                throw new RuntimeException("The method[" + methodName + "] of IEntityMapper not found.");
            }
            ProviderContext context = new ProviderContext(mapperClass, method, null);
            PROVIDER_CONTEXT.put(mapperClass, context);
        }
        return PROVIDER_CONTEXT.get(mapperClass);
    }

    public static SqlSupplierByProviderContext getSqlSupplier(String method) {
        if (sqlSupplierMap.containsKey(method)) {
            return sqlSupplierMap.get(method);
        } else {
            throw new RuntimeException("SqlSupplier not found for method:" + method);
        }
    }

    private static final KeyMap<ProviderContext> PROVIDER_CONTEXT = KeyMap.instance(32);

    private static Map<String, Method> init() {
        Map<String, Method> methods = new HashMap<>(32);
        for (Method m : IEntityMapper.class.getDeclaredMethods()) {
            methods.put(m.getName(), m);
        }
        return Collections.unmodifiableMap(methods);
    }

    private static final Map<String, Method> methods = init();

    public static final Map<String, SqlSupplierByProviderContext> sqlSupplierMap = KeyMap.<SqlSupplierByProviderContext>instance()
        .put(M_Insert, SqlProvider::insert)
        .put(M_InsertBatch, SqlProvider::insertBatch)
        .put(M_InsertWithPk, SqlProvider::insertWithPk)
        .put(M_InsertBatchWithPk, SqlProvider::insertBatchWithPk)
        .put(M_InsertSelect, SqlProvider::insertSelect)
        .put(M_UpdateBy, SqlProvider::updateBy)
        .put(M_ListEntity, SqlProvider::listEntity)
        .put(M_ListMaps, SqlProvider::listMaps)
        .put(M_ListObjs, SqlProvider::listObjs)
        .put(M_Count, SqlProvider::count)
        .put(M_CountNoLimit, SqlProvider::countNoLimit)
        .put(M_Delete, SqlProvider::delete)
        .put(M_BatchCrud, SqlProvider::batchCrud)
        .unmodified().map();
}