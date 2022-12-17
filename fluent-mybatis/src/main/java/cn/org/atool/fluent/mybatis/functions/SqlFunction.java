package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.common.kits.KeyVal;
import cn.org.atool.fluent.common.kits.StrKey;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.Inserter;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.intf.IOptMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;
import com.sun.tools.javac.util.List;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static cn.org.atool.fluent.mybatis.functions.SqlFunctions.WRAPPER_PARAMETER;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public interface SqlFunction<E extends IOptMapping> extends BiFunction<IRichMapper, E, Object> {
    @FunctionalInterface
    interface IQueryFunction extends SqlFunction<IQuery>, Serializable {
    }

    @FunctionalInterface
    interface IUpdateFunction extends SqlFunction<IUpdate>, Serializable {
    }

    @FunctionalInterface
    interface IInsertFunction extends SqlFunction<Inserter>, Serializable {
    }

    /**
     * 获取sql语句和输入的Context上下文对象
     *
     * @param queryUpdaterInserter IQuery/IUpdate/Inserter对象
     * @param mapperFunction       IEntityMapper方法函数
     * @return SQL语句+入参
     */
    static StrKey sql(IOptMapping queryUpdaterInserter, SqlFunction mapperFunction) {
        IMapping mapping = queryUpdaterInserter.mapping().orElseThrow(() -> new RuntimeException("IMapping not found."));
        PrinterMapper mapper = (PrinterMapper) PrinterMapper.set(2, mapping);
        try {
            mapperFunction.apply(mapper, queryUpdaterInserter);
            String sql = mapper.getSql().get(0);
            String method = LambdaUtil.lambdaName(mapperFunction);
            Function<Object, Map> wrapper = WRAPPER_PARAMETER.get(method);
            Map data = wrapper.apply(queryUpdaterInserter);
            return new StrKey(sql, data);
        } finally {
            PrinterMapper.clear();
        }
    }

    static Map wrapperParameter(String method, Object parameter) {
        return WRAPPER_PARAMETER.get(method).apply(parameter);
    }
}

@SuppressWarnings({"unchecked", "rawtypes"})
class SqlFunctions {
    static final Function<Object, Map> EW_FUNCTION = obj -> wrapper(Param_EW, obj);
    static final KeyMap<Function<Object, Map>> WRAPPER_PARAMETER = KeyMap.<Function<Object, Map>>instance()
        .put(M_Insert, SqlFunctions::insert)
        .put(M_InsertWithPk, SqlFunctions::insert)
        .put(M_InsertBatch, SqlFunctions::insert)
        .put(M_ListEntity, EW_FUNCTION)
        .put(M_ListMaps, EW_FUNCTION)
        .put(M_ListObjs, EW_FUNCTION)
        .put(M_Count, EW_FUNCTION)
        .put(M_CountNoLimit, EW_FUNCTION)
        .put(M_UpdateBy, SqlFunctions::updateBy)
        .put(M_Delete, EW_FUNCTION)
        .put(M_BatchCrud, EW_FUNCTION)
        .put(M_InsertSelect, SqlFunctions::insertSelectFunction)
        .put(M_InsertBatchWithPk, entities -> wrapper(Param_List, entities));

    private static Map<String, Object> updateBy(Object obj) {
        if (obj instanceof IUpdate) {
            return wrapper(Param_EW, List.of(obj));
        } else {
            return wrapper(Param_EW, obj);
        }
    }

    private static Map<String, Object> insert(Object obj) {
        if (obj instanceof IEntity) {
            return wrapper(Param_EW, obj);
        } else if (obj instanceof Collection) {
            return wrapper(Param_List, obj);
        } else if (obj instanceof Inserter) {
            return wrapper(Param_List, ((Inserter) obj).entities());
        } else {
            throw new RuntimeException("Not support");
        }
    }

    private static Map<String, Object> insertSelectFunction(Object obj) {
        KeyVal kv = (KeyVal) obj;
        return KeyMap.instance()
            .put(Param_EW, kv.key())
            .put(Param_List, kv.val())
            .map();
    }

    private static Map<String, Object> wrapper(String param_List, Object entities) {
        KeyMap kv = KeyMap.instance().put(param_List, entities);
        return kv.map();
    }
}
