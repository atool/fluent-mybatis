package cn.org.atool.fluent.mybatis.base.provider;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.entity.TableId;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.base.provider.SqlKitFactory.factory;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Param_EW;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.COMMA;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.DOT;

/**
 * MappedStatement重建构造
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class StatementBuilder extends MappedStatement.Builder {
    private final String id;
    private final Configuration configuration;
    private final IMapping mapping;
    private final MappedStatement statement;
    private final TypeHandlerRegistry typeHandlerRegistry;

    public StatementBuilder(IMapping mapping, MappedStatement statement) {
        super(statement.getConfiguration(), statement.getId(), statement.getSqlSource(), statement.getSqlCommandType());
        this.mapping = mapping;
        this.id = statement.getId();
        this.statement = statement;
        this.configuration = statement.getConfiguration();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        // copy
        this.resource(statement.getResource())
            .fetchSize(statement.getFetchSize())
            .timeout(statement.getTimeout())
            .statementType(statement.getStatementType())
            .keyGenerator(statement.getKeyGenerator())
            .keyProperty(this.joining(statement.getKeyProperties()))
            .keyColumn(this.joining(statement.getKeyColumns()))
            .databaseId(statement.getDatabaseId())
            .lang(statement.getLang())
            .resultOrdered(statement.isResultOrdered())
            .resultSets(this.joining(statement.getResultSets()))
            .resultMaps(statement.getResultMaps())
            .resultSetType(statement.getResultSetType())
            .flushCacheRequired(statement.isFlushCacheRequired())
            .useCache(statement.isUseCache())
            .parameterMap(statement.getParameterMap())
            .cache(statement.getCache());
    }

    /**
     * 补充方法 {@link SqlProvider#insert(Map, ProviderContext)} 声明
     */
    public MappedStatement selectKeyStatementOfInsert() {
        FieldMapping primary = mapping.primaryMapping();
        TableId tableId = mapping.tableId();
        KeyGenerator keyGenerator = factory(mapping).insert(this, primary, tableId);
        this.keyProperty(primary.name);
        this.keyColumn(primary.column);
        this.keyGenerator(keyGenerator);

        return this.build();
    }

    /**
     * 补充方法 {@link SqlProvider#insertBatch(Map, ProviderContext)} 声明
     */
    public MappedStatement selectKeyStatementOfBatchInsert() {
        FieldMapping primary = mapping.primaryMapping();
        TableId tableId = mapping.tableId();
        KeyGenerator keyGenerator = factory(mapping).insertBatch(mapping, this, primary, tableId);
        this.keyProperty(primary.name);
        this.keyColumn(primary.column);
        this.keyGenerator(keyGenerator);
        return this.build();
    }

    /**
     * 补充方法 {@link SqlProvider#listEntity(Map, ProviderContext)} 声明
     */
    public MappedStatement listEntityStatement() {
        List<ResultMapping> resultMappings = new ArrayList<>();
        for (FieldMapping f : mapping.allFields()) {
            resultMappings.add(this.resultMapping(f));
        }
        Class type = this.mapping.entityClass();
        this.resultMaps(this.statementResultMaps(type.getName() + "-RM", type, resultMappings));
        return this.build();
    }

    /**
     * @see org.apache.ibatis.annotations.SelectKey
     */
    public KeyGenerator handleSelectKey(FieldMapping primary, TableId tableId) {
        String selectId = this.id + SelectKeyGenerator.SELECT_KEY_SUFFIX;

        boolean executeBefore = mapping.db().feature.isBefore();
        String seqName = mapping.db().feature.getSeq();
        if (notBlank(tableId.seqName)) {
            seqName = tableId.seqName;
            executeBefore = tableId.isSeqBefore(mapping.db());
        }
        if (isBlank(seqName)) {
            return NoKeyGenerator.INSTANCE;
        }
        /*
         * @see org.apache.ibatis.builder.annotation.MapperAnnotationBuilder#handleSelectKeyAnnotation(SelectKey, String, Class, LanguageDriver)
         */
        SqlSource sqlSource = statement.getLang().createSqlSource(configuration, seqName.trim(), primary.javaType);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, selectId, sqlSource, SqlCommandType.SELECT)
            .resource(statement.getResource())
            .fetchSize(null)
            .timeout(null)
            .statementType(StatementType.PREPARED)
            .keyGenerator(NoKeyGenerator.INSTANCE)
            .keyProperty(Param_EW + DOT + primary.name)
            .keyColumn(primary.column)
            .databaseId(statement.getDatabaseId())
            .lang(statement.getLang())
            .resultOrdered(false)
            .resultSets(null)
            .resultMaps(statementResultMaps(selectId + "-Inline", primary.javaType, new ArrayList<>()))
            .resultSetType(null)
            .flushCacheRequired(false)
            .useCache(false)
            .cache(statement.getCache());
        configuration.addMappedStatement(statementBuilder.build());

        MappedStatement keyStatement = configuration.getMappedStatement(selectId, false);

        SelectKeyGenerator keyGenerator = new SelectKeyGenerator(keyStatement, executeBefore);

        configuration.addKeyGenerator(selectId, keyGenerator);
        return keyGenerator;
    }

    /**
     * @see org.apache.ibatis.annotations.Result
     */
    private ResultMapping resultMapping(FieldMapping field) {
        List<ResultFlag> flags = new ArrayList<>();
        if (field.isPrimary()) {
            flags.add(ResultFlag.ID);
        }
        return new ResultMapping.Builder(this.configuration, field.name, field.column, field.javaType)
            .typeHandler(this.resolveTypeHandler(field.javaType, field.typeHandler))
            .flags(flags)
            .build();
    }

    @SuppressWarnings("all")
    /**
     * @see org.apache.ibatis.builder.BaseBuilder#resolveTypeHandler(Class, Class)
     */
    private TypeHandler<?> resolveTypeHandler(Class<?> javaType, Class<? extends TypeHandler<?>> typeHandlerType) {
        if (typeHandlerType == null) {
            return null;
        }
        // javaType ignored for injected handlers see issue #746 for full detail
        TypeHandler<?> handler = typeHandlerRegistry.getMappingTypeHandler(typeHandlerType);
        if (handler == null) {
            // not in registry, create a new one
            handler = typeHandlerRegistry.getInstance(javaType, typeHandlerType);
        }
        return handler;
    }

    private List<ResultMap> statementResultMaps(String statementId, Class<?> resultType, List<ResultMapping> resultMappings) {
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(configuration, statementId, resultType, resultMappings, null)
            .build();
        resultMaps.add(resultMap);
        return resultMaps;
    }

    private String joining(String[] arr) {
        return arr == null ? null : String.join(COMMA, arr);
    }
}