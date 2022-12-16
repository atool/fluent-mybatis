package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.Map;
import java.util.function.BiFunction;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class WrapperPrinter {
    private final IMapping mapping;

    private final Class<? extends IWrapperMapper> mapperClass;

    public WrapperPrinter(IWrapper wrapper) {
        this.mapping = (IMapping) wrapper.mapping().orElseGet(() -> {
            String err = "Mapping not found";
            throw new RuntimeException(err);
        });
        this.mapperClass = mapping.mapperClass();
    }

    public static SqlSupplier getSqlSupplier(String method) {
        if (sqlSupplierMap.containsKey(method)) {
            return sqlSupplierMap.get(method);
        } else {
            throw new RuntimeException("SqlSupplier not found for method:" + method);
        }
    }

    public static final Map<String, SqlSupplier> sqlSupplierMap = KeyMap.<SqlSupplier>instance()
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

    @FunctionalInterface
    public interface SqlSupplier extends BiFunction<Map, ProviderContext, String> {
    }
}
