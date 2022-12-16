package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.intf.BatchCrud;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.typehandler.ConvertorKit;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;

/**
 * PrinterMapper: 不实际执行sql语句, 只输出sql语句
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public class PrinterMapper implements IWrapperMapper {
    @Setter
    private static Configuration configuration = new Configuration();

    private static final ThreadLocal<PrinterMapper> local = new ThreadLocal<>();

    private IMapping mapping;

    private final int mode;

    @Getter
    private final List<String> sql = new ArrayList<>();

    private PrinterMapper(int mode, IMapping mapping) {
        this.mode = mode;
        this.mapping(mapping);
    }

    public static boolean isPrint() {
        return local.get() != null;
    }

    @Override
    public int insert(IEntity entity) {
        return this.simulate(M_Insert, map(Param_EW, entity));
    }

    @Override
    public int insertBatch(Collection entities) {
        return this.simulate(M_InsertBatch, map(Param_List, entities));
    }

    @Override
    public int insertWithPk(IEntity entity) {
        return this.simulate(M_InsertWithPk, map(Param_EW, entity));
    }

    @Override
    public int insertBatchWithPk(Collection entities) {
        return this.simulate(M_InsertBatchWithPk, map(Param_List, entities));
    }

    @Override
    public int insertSelect(String[] fields, IQuery query) {
        Map<String, Object> map = this.map(Param_EW, query, Param_Fields, fields);
        return this.simulate(M_InsertSelect, map);
    }

    @Override
    public int updateBy(IUpdate... updates) {
        return this.simulate(M_UpdateBy, map(Param_EW, updates));
    }

    @Override
    public List internalListEntity(IQuery query) {
        this.simulate(M_ListEntity, map(Param_EW, query));
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> listMaps(IQuery query) {
        this.simulate(M_ListMaps, map(Param_EW, query));
        return Collections.emptyList();
    }

    @Override
    public List listObjs(IQuery query) {
        this.simulate(M_ListObjs, map(Param_EW, query));
        return Collections.emptyList();
    }

    @Override
    public int count(IQuery query) {
        return this.simulate(M_Count, map(Param_EW, query));
    }

    @Override
    public int countNoLimit(IQuery query) {
        return this.simulate(M_CountNoLimit, map(Param_EW, query));
    }

    @Override
    public int delete(IQuery query) {
        return this.simulate(M_Delete, map(Param_EW, query));
    }

    @Override
    public void batchCrud(BatchCrud crud) {
        this.simulate(M_BatchCrud, map(Param_EW, crud));
    }

    @Override
    public void callProcedure(String procedure, Object parameter) {
        this.addSQL(parameter, () -> "{CALL " + procedure + "}");
    }

    @Override
    public IMapping mapping() {
        return this.mapping;
    }


    private int simulate(String method, Map map) {
        Supplier<String> sqler = () -> SqlSupplier.get(this.mapping, method).apply(map);
        this.addSQL(map, sqler);
        return 1;
    }

    private Map<String, Object> map(Object... kvs) {
        Map<String, Object> map = new HashMap<>();
        for (int index = 1; index < kvs.length; index += 2) {
            map.put((String) kvs[index - 1], kvs[index]);
        }
        return map;
    }

    private <P> void addSQL(P object, Supplier<String> supplier) {
        String text = supplier.get();
        if (isBlank(text) || mode == 2) {
            this.sql.add(text);
            return;
        }
        TextSqlNode textSqlNode = new TextSqlNode(text);
        RawSqlSource sqlSource = new RawSqlSource(configuration, textSqlNode, object.getClass());
        BoundSql boundSql = sqlSource.getBoundSql(object);
        if (mode == 0) {
            this.sql.add(boundSql.getSql());
            return;
        }
        Map<String, Object> parameters = this.getParameters(boundSql);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String expression = "#{" + entry.getKey() + "}";
            Object value = entry.getValue();

            text = this.replaceBy(text, expression, value);
        }
        this.sql.add(text);
    }

    private String replaceBy(String text, String expression, Object value) {
        if (value == null) {
            text = text.replace(expression, "null");
        } else if (value.getClass().isPrimitive() || value instanceof Number || value instanceof Boolean) {
            text = text.replace(expression, String.valueOf(value));
        } else {
            String str = ConvertorKit.toString(value);
            text = text.replace(expression, "'" + str + "'");
        }
        return text;
    }

    private Map<String, Object> getParameters(BoundSql boundSql) {
        Map<String, Object> values = new HashMap<>();
        for (ParameterMapping pm : boundSql.getParameterMappings()) {
            if (pm.getMode() == ParameterMode.OUT) {
                continue;
            }
            values.put(pm.getProperty(), parseParameterValue(boundSql, pm));
        }
        return values;
    }

    private Object parseParameterValue(BoundSql boundSql, ParameterMapping pm) {
        Object parameterObject = boundSql.getParameterObject();
        String property = pm.getProperty();
        if (boundSql.hasAdditionalParameter(property)) {
            return boundSql.getAdditionalParameter(property);
        } else if (parameterObject == null) {
            return null;
        } else if (configuration.getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
            return parameterObject;
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            return metaObject.getValue(property);
        }
    }

    /**
     * 主动设置PrinterMapper
     *
     * @param mode    打印SQL模式
     * @param mapping 原生mapper实例
     * @return PrinterMapper
     */
    public static IWrapperMapper set(int mode, IMapping mapping) {
        local.set(new PrinterMapper(mode, mapping));
        return local.get();
    }

    /**
     * 清除线程中的PrinterMapper实例
     */
    public static void clear() {
        local.remove();
    }

    /**
     * 返回线程中的PrinterMapper
     *
     * @return PrinterMapper
     */
    public static IWrapperMapper get(IWrapperMapper mapper, Class eClass) {
        if (local.get() == null) {
            return mapper;
        } else {
            local.get().mapping(RefKit.byEntity(eClass));
            return local.get();
        }
    }

    private void mapping(IMapping mapping) {
        this.mapping = mapping;
    }

    //Fix #I56PNZ: the Generic varargs are NOT changed in this method, so it is type safe.
    @SafeVarargs
    public static List<String> print(int mode, IMapping mapping, Consumer<IWrapperMapper>... simulators) {
        try {
            PrinterMapper mapper = (PrinterMapper) PrinterMapper.set(mode, mapping);
            for (Consumer<IWrapperMapper> simulator : simulators) {
                simulator.accept(mapper);
            }
            return mapper.getSql();
        } finally {
            PrinterMapper.clear();
        }
    }
}