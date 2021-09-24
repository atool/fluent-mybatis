package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.BatchCrud;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.mapper.IWrapperMapper;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import lombok.Getter;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.*;
import static org.apache.ibatis.builder.annotation.ProviderContextKit.newProviderContext;

/**
 * PrinterMapper: 不实际执行sql语句, 只输出sql语句
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PrinterMapper implements IWrapperMapper {

    private IWrapperMapper delegate;

    private Class<? extends IWrapperMapper> klass;

    private final int mode;

    private Configuration configuration;

    private TypeHandlerRegistry typeHandlerRegistry;

    @Getter
    private final List<String> sql = new ArrayList<>();

    private PrinterMapper(int mode, IWrapperMapper mapper) {
        this.mode = mode;
        this.setDelegate(mapper);
    }

    private PrinterMapper setDelegate(IWrapperMapper mapper) {
        this.delegate = mapper;
        this.klass = (Class) this.delegate.getClass().getInterfaces()[0];
        SqlSessionTemplate sqlSession = this.value(this.delegate, f_proxyHandler, f_sqlSession);
        configuration = sqlSession.getConfiguration();
        typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        return this;
    }

    @Override
    public int insert(IEntity entity) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_Insert));
        Map<String, Object> map = ew(entity);
        this.addSQL(map, () -> SqlProvider.insert(map, context));
        return 1;
    }

    @Override
    public int insertBatch(Collection entities) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_InsertBatch));
        Map<String, Object> map = new HashMap<>();
        map.put(Param_List, entities);
        this.addSQL(map, () -> SqlProvider.insertBatch(map, context));
        return 1;
    }

    @Override
    public int insertWithPk(IEntity entity) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_insertWithPk));
        Map<String, Object> map = ew(entity);
        this.addSQL(map, () -> SqlProvider.insertWithPk(map, context));
        return 1;
    }

    @Override
    public int insertBatchWithPk(Collection entities) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_insertBatchWithPk));
        Map<String, Object> map = new HashMap<>();
        map.put(Param_List, entities);
        this.addSQL(map, () -> SqlProvider.insertBatchWithPk(map, context));
        return 1;
    }

    @Override
    public int insertSelect(String[] fields, IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_insertSelect));
        Map<String, Object> map = this.ew(query);
        map.put(Param_Fields, fields);
        this.addSQL(map, () -> SqlProvider.insertSelect(map, context));
        return 1;
    }

    @Override
    public int updateBy(IUpdate... updates) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_updateBy));
        Map<String, Object> map = new HashMap<>();
        map.put(Param_EW, updates);
        this.addSQL(map, () -> SqlProvider.updateBy(map, context));
        return 1;
    }

    @Override
    public List listEntity(IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_listEntity));
        Map<String, Object> map = this.ew(query);
        this.addSQL(map, () -> SqlProvider.listEntity(map, context));
        return Collections.emptyList();
    }

    @Override
    public List<Map<String, Object>> listMaps(IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_listMaps));
        Map<String, Object> map = this.ew(query);
        this.addSQL(map, () -> SqlProvider.listMaps(map, context));
        return Collections.emptyList();
    }

    @Override
    public List listObjs(IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_listObjs));
        Map<String, Object> map = this.ew(query);
        this.addSQL(map, () -> SqlProvider.listObjs(map, context));
        return Collections.emptyList();
    }

    @Override
    public int count(IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_count));
        Map<String, Object> map = this.ew(query);
        this.addSQL(map, () -> SqlProvider.count(map, context));
        return 1;
    }

    @Override
    public int countNoLimit(IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_countNoLimit));
        Map<String, Object> map = this.ew(query);
        this.addSQL(map, () -> SqlProvider.countNoLimit(map, context));
        return 1;
    }

    @Override
    public int delete(IQuery query) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_delete));
        Map<String, Object> map = this.ew(query);
        this.addSQL(map, () -> SqlProvider.delete(map, context));
        return 1;
    }

    @Override
    public void callProcedure(String procedure, Object parameter) {
        this.addSQL(parameter, () -> "{CALL " + procedure + "}");
    }

    @Override
    public void batchCrud(BatchCrud crud) {
        ProviderContext context = newProviderContext(this.klass, this.method(M_batchCrud));
        Map<String, Object> map = this.ew(crud);
        this.addSQL(map, () -> SqlProvider.batchCrud(map, context));
    }

    @Override
    public IMapping mapping() {
        return this.delegate.mapping();
    }

    /* ============================================= */

    private static Map<String, Method> methods = null;

    private static final Field f_proxyHandler = field(Proxy.class, "h");

    private static final Field f_sqlSession = field(MapperProxy.class, "sqlSession");

    private Method method(String methodName) {
        if (methods == null) {
            methods = new HashMap<>(16);
            for (Method m : IEntityMapper.class.getDeclaredMethods()) {
                methods.put(m.getName(), m);
            }
        }
        return methods.get(methodName);
    }

    private Map<String, Object> ew(Object query) {
        Map<String, Object> map = new HashMap<>();
        map.put(Param_EW, query);
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
            text = text.replace(expression, "'" + value + "'");
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
        } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            return parameterObject;
        } else {
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            return metaObject.getValue(property);
        }
    }

    private static Field field(Class declared, String fieldName) {
        try {
            Field field = declared.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取target的属性的属性
     *
     * @param target Object
     * @param fields 级联属性
     * @param <T>    属性值
     * @return Object
     */
    private <T> T value(Object target, Field... fields) {
        Object value = target;
        try {
            for (Field f : fields) {
                value = f.get(value);
            }
            return (T) value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final ThreadLocal<PrinterMapper> local = new ThreadLocal<>();

    /**
     * 设置PrinterMapper
     *
     * @param mode   打印SQL模式
     * @param mapper 原生mapper实例
     * @return PrinterMapper
     */
    public static IWrapperMapper set(int mode, IWrapperMapper mapper) {
        local.set(new PrinterMapper(mode, mapper));
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
    public static IWrapperMapper get(IWrapperMapper mapper) {
        PrinterMapper printerMapper = local.get();
        return printerMapper == null ? mapper : printerMapper.setDelegate(mapper);
    }
}