package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.meta.*;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.EntityRefKit;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.model.op.SqlOps;
import cn.org.atool.fluent.mybatis.functions.RefKey;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.*;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.base.EntityRefKit.getRefKeyOfRefMethod;

/**
 * FormHelper辅助工具类
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormHelper {
    /**
     * 将表单Form转换为entityClass对应的Query
     *
     * @param entityClass class of entity
     * @param form        表单
     * @return IQuery
     */
    public static IQuery toQuery(Class entityClass, Form form) {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, entityClass);
        if (form.getId() != null && form.getCurrPage() != null) {
            throw new IllegalArgumentException("nextId and currPage can only have one value");
        }
        IQuery query = RefKit.byEntity(entityClass).query();
        where(entityClass, form, (IWrapper) query);
        oderBy(entityClass, form, query);
        pageBy(entityClass, form, query);
        return query;
    }

    public static IUpdate toUpdate(Class entityClass, Form form) {
        MybatisUtil.assertNotNull(FluentConst.F_Entity_Class, entityClass);
        IUpdate updater = RefKit.byEntity(entityClass).updater();

        updateBy(entityClass, form, updater);
        where(entityClass, form, (IWrapper) updater);
        return updater;
    }

    private static void updateBy(Class entityClass, Form form, IUpdate updater) {
        for (Map.Entry<String, Object> entry : form.getUpdate().entrySet()) {
            String column = RefKit.columnOfField(entityClass, entry.getKey());
            if (If.isBlank(column)) {
                throw fieldNotFoundException(entry.getKey(), entityClass);
            }
            updater.updateSet(column, entry.getValue());
        }
    }

    /**
     * 条件设置
     */
    private static void where(Class entityClass, Form form, IWrapper query) {
        WhereBase where = query.where();
        for (FormEntry item : form.getWhere()) {
            Object[] value = item.getValue();
            if (value == null) {
                continue;
            }
            String column = RefKit.columnOfField(entityClass, item.getField());
            if (If.isBlank(column)) {
                throw fieldNotFoundException(item.getField(), entityClass);
            }
            switch (item.getOp()) {
                case FormSqlOp.OP_LIKE_LEFT:
                    where.and.apply(column, SqlOp.LIKE, value[0] + "%");
                    break;
                case FormSqlOp.OP_LIKE:
                    where.and.apply(column, SqlOp.LIKE, "%" + value[0] + "%");
                    break;
                case FormSqlOp.OP_LIKE_RIGHT:
                    where.and.apply(column, SqlOp.LIKE, "%" + value[0]);
                    break;
                case FormSqlOp.OP_NOT_LIKE:
                    where.and.apply(column, SqlOp.NOT_LIKE, "%" + value[0] + "%");
                    break;
                default:
                    where.and.apply(column, SqlOps.get(item.getOp()), value);
            }
        }
    }

    /**
     * 分页设置
     */
    private static void pageBy(Class entityClass, Form form, IQuery query) {
        if (form.getCurrPage() != null) {
            int from = form.getPageSize() * (form.getCurrPage() - 1);
            query.limit(from, form.getPageSize());
        } else if (form.getId() != null) {
            String column = RefKit.primaryId(entityClass);
            query.where().and.apply(column, SqlOp.GE, form.getId());
            query.limit(form.getPageSize());
        }
    }

    /**
     * 排序设置
     */
    private static void oderBy(Class entityClass, Form form, IQuery query) {
        for (FormItemOrder item : form.getOrder()) {
            String column = RefKit.columnOfField(entityClass, item.getField());
            query.orderBy().apply(true, item.isAsc(), column);
        }
    }

    /**
     * 构造Entity对象
     *
     * @param method 方法定义元数据
     * @param args   表单对象元数据
     * @return Entity实例
     */
    public static <E extends IEntity> E newEntity(MethodMeta method, Object[] args) {
        AMapping mapping = RefKit.byEntity(method.entityClass);
        IEntity entity = mapping.newEntity();
        for (IEntryMeta entryMeta : method.metas().getMetas()) {
            EntryMeta meta = (EntryMeta) entryMeta;
            if (meta.getter != null) {
                FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.name);
                fm.setter.set(entity, meta.get(args));
            }
        }
        return (E) entity;
    }

    /**
     * 构造更新条件
     *
     * @param args 方法定义元数据
     * @return 更新条件
     */
    public static <E extends IEntity> IUpdate<E> newUpdate(MethodArgs args) {
        AMapping mapping = RefKit.byEntity(args.meta.entityClass);
        IUpdate<E> updater = mapping.updater();
        IQuery where = mapping.emptyQuery();

        wrapperByMetas(mapping, args.meta.metas(), args, (IWrapper) updater, where);
        // 和默认条件以 and 关联
        updater.where().and(where);
        return updater;
    }

    /**
     * 构造查询条件
     *
     * @param args 方法定义元数据
     * @return 查询条件
     */
    public static <E extends IEntity> IQuery<E> newQuery(MethodArgs args) {
        AMapping mapping = RefKit.byEntity(args.meta.entityClass);
        IQuery<E> query = mapping.query();
        IQuery<E> where = mapping.emptyQuery();

        wrapperByMetas(mapping, args.meta.metas(), args, (IWrapper) query, where);
        // 和默认条件以 and 关联
        query.where().and(where);
        limitAndOrderBy(query, args.meta.argNames);
        return query;
    }

    /**
     * 根据方法名称中的limit和order by设置query
     */
    private static <E extends IEntity> void limitAndOrderBy(IQuery<E> query, MethodArgNames argNames) {
        if (argNames == null) {
            return;
        }
        if (argNames.getTopN() > 0) {
            query.limit(argNames.getTopN());
        }
        for (MethodArgNames.OrderBy orderBy : argNames.orderBy) {
            query.orderBy().apply(true, orderBy.isAsc, orderBy.name);
        }
    }

    private static void wrapperByMetas(IMapping mapping, EntryMetas metas, MethodArgs args, IWrapper wrapper, IQuery where) {
        for (IEntryMeta entryMeta : metas.getMetas()) {
            if (entryMeta instanceof EntryMeta) {
                wrapperByMeta(mapping, metas.isAnd(), (EntryMeta) entryMeta, args, wrapper, where);
            } else if (entryMeta instanceof EntryMetas) {
                EntryMetas meta = (EntryMetas) entryMeta;
                IQuery nested = mapping.emptyQuery();
                wrapperByMetas(mapping, meta, args, wrapper, nested);
                if (metas.isAnd()) {
                    where.where().and(nested);
                } else {
                    where.where().or(nested);
                }
            }
        }
        if (wrapper instanceof IQuery) {
            for (EntryMeta meta : metas.getOrderBy()) {
                addOrderBy(mapping, (IQuery) wrapper, meta, args);
            }
            if (metas.getPageSize() != null) {
                setPaged(mapping, (IQuery) wrapper, args);
            }
        }
    }

    private static void wrapperByMeta(IMapping mapping, boolean isAnd, EntryMeta meta, MethodArgs args, IWrapper wrapper, IQuery where) {
        Object value = meta.get(args.args);
        if (meta.getter == null || meta.ignoreNull && value == null) {
            return;
        }
        FieldMapping fm = mapping.getFieldsMap().get(meta.name);
        if (fm == null) {
            throw fieldNotFoundException(meta.name, args.meta.entityClass);
        } else if (meta.entryType == EntryType.Update && args.meta.isUpdate() && wrapper instanceof IUpdate) {
            ((IUpdate) wrapper).updateSet(fm.column, value);
        } else {
            where(isAnd ? where.where().and : where.where().or, fm.column, meta, value);
        }
    }

    private static <E extends IEntity> void addOrderBy(IMapping mapping, IQuery<E> query, EntryMeta meta, MethodArgs args) {
        Boolean asc = meta.get(args.args);
        if (asc == null) {
            return;
        }
        FieldMapping fm = mapping.getFieldsMap().get(meta.name);
        if (fm == null) {
            throw fieldNotFoundException(meta.name, args.meta.entityClass);
        }
        query.orderBy().apply(true, asc, fm.column);
    }

    private static void setPaged(IMapping mapping, IQuery query, MethodArgs args) {
        int pageSize = args.getPageSize();
        if (pageSize < 1) {
            throw new IllegalArgumentException("PageSize must be greater than 0.");
        }
        Integer currPage = args.getCurrPage();
        if (currPage == null || currPage < 1) {
            currPage = 1;
        }
        query.paged(currPage, pageSize);
        Object pagedTag = args.getPagedTag();
        if (pagedTag != null) {
            String pk = mapping.primaryId(true);
            query.where().apply(pk, SqlOp.GE, pagedTag);
        }
    }

    private static void where(WhereBase where, String column, EntryMeta meta, Object value) {
        if (value == null || value instanceof String && isBlank((String) value)) {
            if (!meta.ignoreNull) {
                if (meta.entryType == EntryType.EQ) {
                    where.apply(column, SqlOp.IS_NULL);
                } else {
                    throw new IllegalArgumentException("Condition field[" + meta.name + "] not assigned.");
                }
            }
            return;
        }

        switch (meta.entryType) {
            case EQ:
                if (value instanceof Collection || value.getClass().isArray()) {
                    where.apply(column, SqlOp.IN, toArray(meta.name, value));
                } else {
                    where.apply(column, SqlOp.EQ, value);
                }
                break;
            case GT:
                where.apply(column, SqlOp.GT, value);
                break;
            case GE:
                where.apply(column, SqlOp.GE, value);
                break;
            case LT:
                where.apply(column, SqlOp.LT, value);
                break;
            case LE:
                where.apply(column, SqlOp.LE, value);
                break;
            case NE:
                where.apply(column, SqlOp.NE, value);
                break;
            case IN:
                where.apply(column, SqlOp.IN, toArray(meta.name, value));
                break;
            case Like:
                where.apply(column, SqlOp.LIKE, "%" + value + "%");
                break;
            case StartWith:
                where.apply(column, SqlOp.LIKE, value + "%");
                break;
            case EndWith:
                where.apply(column, SqlOp.LIKE, "%" + value);
                break;
            case Between:
                Object[] args = toArray(meta.name, value);
                between(where, meta, column, args);
                break;
            default:
                //throw new RuntimeException("there must be something wrong.");
        }
    }

    private static void between(WhereBase where, EntryMeta meta, String column, Object[] args) {
        if (args.length == 0 && meta.ignoreNull) {
            return;
        }
        if (args.length != 2) {
            throw new IllegalArgumentException("The value size of the between condition[" + meta.name + "] must be 2.");
        } else if (args[0] == null && args[1] != null) {
            where.apply(column, SqlOp.LE, args[1]);
        } else if (args[0] != null && args[1] == null) {
            where.apply(column, SqlOp.GE, args[0]);
        } else if (args[0] != null) {
            where.apply(column, SqlOp.BETWEEN, args);
        } else if (!meta.ignoreNull) {
            throw new IllegalArgumentException("The value of the between condition[" + meta.name + "] can't be null.");
        }
    }

    private static Object[] toArray(String methodName, Object object) {
        MybatisUtil.assertNotNull("result of method[" + methodName + "]", object);
        Class aClass = object.getClass();
        List list = new ArrayList();
        if (aClass.isArray()) {
            if (aClass == int[].class) {
                for (int i : (int[]) object) {
                    list.add(i);
                }
            } else if (aClass == long[].class) {
                for (long l : (long[]) object) {
                    list.add(l);
                }
            } else if (aClass == short[].class) {
                for (short s : (short[]) object) {
                    list.add(s);
                }
            } else if (aClass == double[].class) {
                for (double d : (double[]) object) {
                    list.add(d);
                }
            } else if (aClass == float[].class) {
                for (float f : (float[]) object) {
                    list.add(f);
                }
            } else if (aClass == boolean[].class) {
                for (boolean b : (boolean[]) object) {
                    list.add(b);
                }
            } else if (aClass == char[].class) {
                for (char c : (char[]) object) {
                    list.add(c);
                }
            } else if (aClass == byte[].class) {
                for (byte b : (byte[]) object) {
                    list.add(b);
                }
            } else {
                return (Object[]) object;
            }
        } else if (Collection.class.isAssignableFrom(aClass)) {
            list.addAll((Collection) object);
        } else {
            list.add(object);
        }
        return list.toArray();
    }

    /**
     * 将Entity列表转换为指定类的实例列表
     *
     * @param entities Entity列表
     * @param rClass   指定类类型
     * @return 转换后的列表
     */
    public static <R> List<R> entities2result(List<IEntity> entities, Class<R> rClass) {
        if (rClass == null) {
            return (List) entities;
        } else {
            EntryMetas metas = EntryMetas.getFormMeta(rClass);
            return entities2result(entities, metas);
        }
    }

    /**
     * 将Entity列表转换为指定类的实例列表
     *
     * @param entities Entity列表
     * @param metas    指定类元数据
     * @return 转换后的列表
     */
    private static List entities2result(List<IEntity> entities, EntryMetas metas) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        Class entityClass = entities.stream().filter(Objects::nonNull)
            .findFirst().map(IEntity::entityClass).orElse(null);
        if (entityClass == null) {
            return Collections.emptyList();
        }
        for (FormMetas form : metas.getForms()) {
            RefKey refKey = getRefKeyOfRefMethod(entityClass, form.entryName);
            if (refKey == null) {
                throw new RuntimeException("The @RefMethod[" + form.entryName + "] of Entity[" + entityClass.getName() + "] not found.");
            } else if (refKey.src != null && refKey.ref != null) {
                RefKit.invokeRefMethod(entityClass, refKey.refMethodName, entities);
            }
        }
        List list = new ArrayList();
        for (IEntity entity : entities) {
            list.add(entity2result(entity, metas));
        }
        return list;
    }

    /**
     * 将Entity实例转换为指定类的实例
     *
     * @param entity Entity实例
     * @param rClass 指定类类型
     * @return 转换后的实例
     */
    public static <R> R entity2result(IEntity entity, Class<R> rClass) {
        if (entity == null) {
            return null;
        } else {
            EntryMetas metas = EntryMetas.getFormMeta(rClass);
            return (R) entity2result(entity, metas);
        }
    }

    /**
     * 将Entity实例转换为指定类的实例
     *
     * @param entity Entity实例
     * @param metas  指定类元数据
     * @return 转换后的实例
     */
    private static Object entity2result(IEntity entity, EntryMetas metas) {
        if (entity == null || metas.objType == null) {
            return null;
        }
        try {
            Object target = metas.objType.getDeclaredConstructor().newInstance();
            Class entityClass = entity.entityClass();
            Map<String, FieldMapping> mapping = RefKit.byEntity(entityClass).getFieldsMap();
            for (IEntryMeta meta : metas.getMetas()) {
                setFieldValue(mapping, (EntryMeta) meta, entity, target);
            }
            for (FormMetas form : metas.getForms()) {
                Object data = EntityRefKit.findRefData(entityClass, entity, form.entryName);
                if (data instanceof Collection) {
                    List list = entities2result((List) data, form);
                    form.setValue(target, list);
                } else {
                    Object value = entity2result((IEntity) data, form);
                    form.setValue(target, value);
                }
            }
            return target;
        } catch (Exception e) {
            throw MybatisUtil.wrap(e);
        }
    }

    private static void setFieldValue(Map<String, FieldMapping> mapping, EntryMeta meta, IEntity source, Object target) {
        if (meta.setter == null) {
            return;
        }
        FieldMapping fm = mapping.get(meta.name);
        if (fm == null) {
            throw fieldNotFoundException(meta.name, source.entityClass());
        } else {
            Object value = fm.getter.get(source);
            meta.set(target, value);
        }
    }

    /**
     * 实体类找不到Entry定义的字段
     *
     * @param name        元数据
     * @param entityClass 实体类
     * @return 异常
     */
    private static RuntimeException fieldNotFoundException(String name, Class entityClass) {
        return new IllegalArgumentException("The field[" + name + "] of entity[" + entityClass.getName() + "] not found.");
    }
}