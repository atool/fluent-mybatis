package cn.org.atool.fluent.form.setter;

import cn.org.atool.fluent.form.Form;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.MethodType;
import cn.org.atool.fluent.form.meta.EntryMeta;
import cn.org.atool.fluent.form.meta.EntryMetas;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.model.op.SqlOps;
import cn.org.atool.fluent.mybatis.mapper.FluentConst;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
            if (item.getValue() == null) {
                continue;
            }
            String column = RefKit.columnOfField(entityClass, item.getField());
            if (If.isBlank(column)) {
                throw fieldNotFoundException(item.getField(), entityClass);
            }
            switch (item.getOp()) {
                case FormSqlOp.OP_LIKE_LEFT:
                    where.and.apply(column, SqlOp.LIKE, item.getValue()[0] + "%");
                    break;
                case FormSqlOp.OP_LIKE:
                    where.and.apply(column, SqlOp.LIKE, "%" + item.getValue()[0] + "%");
                    break;
                case FormSqlOp.OP_LIKE_RIGHT:
                    where.and.apply(column, SqlOp.LIKE, "%" + item.getValue()[0]);
                    break;
                case FormSqlOp.OP_NOT_LIKE:
                    where.and.apply(column, SqlOp.NOT_LIKE, "%" + item.getValue()[0] + "%");
                    break;
                default:
                    where.and.apply(column, SqlOps.get(item.getOp()), item.getValue());
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
     * @param metas  表单对象元数据
     * @return Entity实例
     */
    public static <E extends IEntity> E newEntity(MethodMeta method, EntryMetas metas) {
        AMapping mapping = RefKit.byEntity(method.entityClass);
        IEntity entity = mapping.newEntity();
        for (EntryMeta meta : metas.getMetas()) {
            FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.name);
            fm.setter.set(entity, meta.get(method));
        }
        return (E) entity;
    }

    /**
     * 构造更新条件
     *
     * @param method 方法定义元数据
     * @param metas  表单对象元数据
     * @return 更新条件
     */
    public static <E extends IEntity> IUpdate<E> newUpdate(MethodMeta method, EntryMetas metas) {
        AMapping mapping = RefKit.byEntity(method.entityClass);
        IUpdate<E> updater = mapping.updater();
        for (EntryMeta meta : metas.getMetas()) {
            initWrapper(method, mapping, (IWrapper) updater, meta);
        }
        return updater;
    }

    /**
     * 构造查询条件
     *
     * @param method 方法定义元数据
     * @param metas  表单对象元数据
     * @return 查询条件
     */
    public static <E extends IEntity> IQuery<E> newQuery(MethodMeta method, EntryMetas metas) {
        AMapping mapping = RefKit.byEntity(method.entityClass);
        IQuery<E> query = mapping.query();
        for (EntryMeta meta : metas.getMetas()) {
            initWrapper(method, mapping, (IWrapper) query, meta);
        }
        for (EntryMeta meta : metas.getOrderBy()) {
            addOrderBy(method, mapping, query, meta);
        }
        if (metas.getPageSize() != null) {
            setPaged(method, metas, mapping, query);
        }
        return query;
    }

    private static <E extends IEntity> void addOrderBy(MethodMeta method, AMapping mapping, IQuery<E> query, EntryMeta meta) {
        Boolean asc = meta.get(method);
        if (asc == null) {
            return;
        }
        FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.name);
        if (fm == null) {
            throw fieldNotFoundException(meta.name, method.entityClass);
        }
        query.orderBy().apply(true, asc, fm.column);
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

    private static void initWrapper(MethodMeta method, AMapping mapping, IWrapper wrapper, EntryMeta meta) {
        Object value = meta.get(method);
        if (meta.ignoreNull && value == null) {
            return;
        }
        FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.name);
        if (fm == null) {
            throw fieldNotFoundException(meta.name, method.entityClass);
        } else if (meta.type == EntryType.Update && method.methodType == MethodType.Update) {
            ((IUpdate) wrapper).updateSet(fm.column, value);
        } else {
            where(wrapper, fm.column, meta, value);
        }
    }

    private static <E extends IEntity> void setPaged(MethodMeta method, EntryMetas metas, AMapping mapping, IQuery<E> query) {
        int pageSize = metas.getPageSize(method);
        if (pageSize < 1) {
            throw new IllegalArgumentException("PageSize must be greater than 0.");
        }
        Integer currPage = metas.getCurrPage(method);
        query.limit(currPage == null ? 0 : currPage * pageSize, pageSize);
        Object pagedTag = metas.getPagedTag(method);
        if (pagedTag != null) {
            String pk = mapping.primaryId(true);
            query.where().apply(pk, SqlOp.GE, pagedTag);
        }
    }

    private static void where(IWrapper wrapper, String column, EntryMeta meta, Object value) {
        if (meta.type == EntryType.EQ) {
            if (value != null) {
                wrapper.where().apply(column, SqlOp.EQ, value);
            } else if (!meta.ignoreNull) {
                wrapper.where().apply(column, SqlOp.IS_NULL);
            }
            return;
        }
        if (value == null) {
            throw new IllegalArgumentException("Condition field[" + meta.name + "] not assigned.");
        }
        switch (meta.type) {
            case GT:
                wrapper.where().apply(column, SqlOp.EQ, value);
                break;
            case GE:
                wrapper.where().apply(column, SqlOp.GE, value);
                break;
            case LT:
                wrapper.where().apply(column, SqlOp.LT, value);
                break;
            case LE:
                wrapper.where().apply(column, SqlOp.LE, value);
                break;
            case NE:
                wrapper.where().apply(column, SqlOp.NE, value);
                break;
            case IN:
                wrapper.where().apply(column, SqlOp.IN, toArray(meta.name, value));
                break;
            case Like:
                wrapper.where().apply(column, SqlOp.LIKE, "%" + value + "%");
                break;
            case LikeLeft:
                wrapper.where().apply(column, SqlOp.LIKE, value + "%");
                break;
            case LikeRight:
                wrapper.where().apply(column, SqlOp.LIKE, "%" + value);
                break;
            case Between:
                Object[] args = toArray(meta.name, value);
                between(wrapper, meta, column, args);
                break;
            default:
                //throw new RuntimeException("there must be something wrong.");
        }
    }

    private static void between(IWrapper wrapper, EntryMeta meta, String column, Object[] args) {
        if (args.length == 0 && meta.ignoreNull) {
            return;
        }
        if (args.length != 2) {
            throw new IllegalArgumentException("The value size of the between condition[" + meta.name + "] must be 2.");
        } else if (args[0] == null && args[1] != null) {
            wrapper.where().apply(column, SqlOp.LE, args[1]);
        } else if (args[0] != null && args[1] == null) {
            wrapper.where().apply(column, SqlOp.GE, args[0]);
        } else if (args[0] != null) {
            wrapper.where().apply(column, SqlOp.BETWEEN, args);
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

    public static List entities2result(List<IEntity> entities, Class rClass) {
        if (rClass == null) {
            return entities;
        }
        List list = new ArrayList();
        for (IEntity entity : entities) {
            list.add(entity2result(entity, rClass));
        }
        return list;
    }

    public static Object entity2result(IEntity entity, Class rClass) {
        if (entity == null) {
            return null;
        }
        try {
            Object target = rClass.getDeclaredConstructor().newInstance();

            Map<String, FieldMapping> mapping = RefKit.byEntity(entity.entityClass()).getFieldsMap();
            EntryMetas metas = EntryMetas.getFormMeta(rClass);
            for (EntryMeta meta : metas.getMetas()) {
                FieldMapping fm = mapping.get(meta.name);
                if (fm == null) {
                    throw fieldNotFoundException(meta.name, entity.entityClass());
                } else {
                    Object value = fm.getter.get(entity);
                    meta.set(target, value);
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}