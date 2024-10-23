package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.entity.PkGeneratorKits;
import cn.org.atool.fluent.mybatis.base.intf.BatchCrud;
import cn.org.atool.fluent.mybatis.base.mapper.IEntityMapper;
import cn.org.atool.fluent.mybatis.base.provider.SqlKit;
import cn.org.atool.fluent.mybatis.segment.BaseWrapper;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.SEMICOLON_NEWLINE;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

/**
 * 批量增删改语句构造实现
 *
 * @author wudarui
 */
@Accessors(chain = true)
@SuppressWarnings({"rawtypes", "unchecked"})
public class BatchCrudImpl implements BatchCrud {
    @Getter
    protected final WrapperData data;

    private final List<BiFunction<IMapping, SqlKit, String>> list = new ArrayList<>();

    public BatchCrudImpl() {
        this.data = new WrapperData(EmptyWrapper.INSTANCE);
    }

    public String batchSql(IMapping mapping, SqlKit sqlKit) {
        return list.stream().map(fun -> fun.apply(mapping, sqlKit)).collect(joining(SEMICOLON_NEWLINE));
    }

    @Override
    public BatchCrud addUpdate(IUpdate... updates) {
        for (IUpdate updater : updates) {
            if (!(updater instanceof BaseWrapper)) {
                throw new IllegalArgumentException("the updater should be instance of BaseWrapper");
            }
            updater.data().sharedParameter(data);
            list.add((m, kit) -> {
                IMapping mp = updater.mapping().orElse(m);
                return kit.updateBy(mp, updater.data());
            });
            this.setMapperBy(updater);
        }
        return this;
    }

    @Override
    public BatchCrud addDelete(IQuery... deletes) {
        for (IQuery query : deletes) {
            if (!(query instanceof BaseWrapper)) {
                throw new IllegalArgumentException("the query should be instance of BaseWrapper");
            }
            query.data().sharedParameter(data);
            list.add((m, kit) -> {
                IMapping mp = query.mapping().orElse(m);
                return kit.deleteBy(mp, query.data());
            });
            this.setMapperBy(query);
        }
        return this;
    }

    private AMapping findMapping(Class<? extends IEntity> klass) {
        return RefKit.byEntity(klass);
    }

    private static final String ENTITY_LIST_KEY = "list";

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public BatchCrud addInsert(IEntity... entities) {
        for (IEntity entity : entities) {
            if (entity == null) {
                continue;
            }
            this.setMapperBy(entity);
            if (!data.getParameters().containsKey(ENTITY_LIST_KEY)) {
                data.getParameters().put(ENTITY_LIST_KEY, new ArrayList<>());
            }
            List values = (List) data.getParameters().get(ENTITY_LIST_KEY);
            int index = values.size();
            values.add(entity);
            String prefix = format("ew.data.parameters.%s[%d].", ENTITY_LIST_KEY, index);
            PkGeneratorKits.setPkByGenerator(entity);
            AMapping mapping = this.findMapping(entity.entityClass());
            list.add((m, kit) -> kit.insertEntity(mapping, prefix, entity, entity.findPk() != null));
        }
        return this;
    }

    @Override
    public BatchCrud addInsertSelect(String insertTable, String[] fields, IQuery query) {
        assertNotNull("query", query);
        query.data().sharedParameter(data);
        list.add((m, kit) -> kit.insertSelect(m, insertTable, fields, query));
        this.setMapperBy(query);
        return this;
    }

    private IEntityMapper mapper;

    public void execute(IEntityMapper mapper) {
        this.mapper = mapper;
        this.execute();
    }

    @Override
    public void execute() {
        if (isEmpty()) {
            return;
        }
        if (mapper != null) {
            mapper.batchCrud(this);
        } else {
            throw new RuntimeException("execute mapper can't be null.");
        }
    }

    private void setMapperBy(IEntity entity) {
        if (mapper == null) {
            mapper = RefKit.mapperByEntity(entity.entityClass());
        }
    }

    private void setMapperBy(IUpdate updater) {
        if (mapper == null) {
            updater.mapping().ifPresent(m -> mapper = RefKit.mapperByEntity(((IMapping) m).entityClass()));
        }
    }

    private void setMapperBy(IQuery query) {
        if (mapper != null) {
            query.mapping().ifPresent(m -> mapper = RefKit.mapperByEntity(((IMapping) m).entityClass()));
        }
    }
}