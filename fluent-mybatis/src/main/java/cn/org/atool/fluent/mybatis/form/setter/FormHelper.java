package cn.org.atool.fluent.mybatis.form.setter;

import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.IPaged;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.base.model.op.SqlOps;
import cn.org.atool.fluent.mybatis.form.Form;
import cn.org.atool.fluent.mybatis.form.meta.FormFieldMeta;
import cn.org.atool.fluent.mybatis.form.meta.FormMetaList;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.base.model.SqlOpStr.*;
import static cn.org.atool.fluent.mybatis.mapper.FluentConst.F_Entity_Class;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.assertNotNull;

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
        assertNotNull(F_Entity_Class, entityClass);
        if (form.getId() != null && form.getCurrPage() != null) {
            throw new RuntimeException("nextId and currPage can only have one value");
        }
        IQuery query = RefKit.byEntity(entityClass).query();
        where(entityClass, form, (IWrapper) query);
        oderBy(entityClass, form, query);
        pageBy(entityClass, form, query);
        return query;
    }

    public static IUpdate toUpdate(Class entityClass, Form form) {
        assertNotNull(F_Entity_Class, entityClass);
        IUpdate updater = RefKit.byEntity(entityClass).updater();

        updateBy(entityClass, form, updater);
        where(entityClass, form, (IWrapper) updater);
        return updater;
    }

    private static void updateBy(Class entityClass, Form form, IUpdate updater) {
        for (Map.Entry<String, Object> entry : form.getUpdate().entrySet()) {
            String column = RefKit.columnOfField(entityClass, entry.getKey());
            if (isBlank(column)) {
                throw new RuntimeException("the field[" + entry.getKey() + "] of Entity[" + entityClass.getSimpleName() + "] not found.");
            }
            updater.updateSet(column, entry.getValue());
        }
    }

    /**
     * 条件设置
     */
    private static void where(Class entityClass, Form form, IWrapper query) {
        WhereBase where = query.where();
        for (FormItem item : form.getWhere()) {
            if (item.getValue() == null) {
                continue;
            }
            String column = RefKit.columnOfField(entityClass, item.getField());
            if (isBlank(column)) {
                throw new RuntimeException("the field[" + item.getField() + "] of Entity[" + entityClass.getSimpleName() + "] not found.");
            }
            switch (item.getOp()) {
                case OP_LIKE_LEFT:
                    where.and.apply(column, SqlOp.LIKE, item.getValue()[0] + "%");
                    break;
                case OP_LIKE:
                    where.and.apply(column, SqlOp.LIKE, "%" + item.getValue()[0] + "%");
                    break;
                case OP_LIKE_RIGHT:
                    where.and.apply(column, SqlOp.LIKE, "%" + item.getValue()[0]);
                    break;
                case OP_NOT_LIKE:
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
     * @param eClass Entity类型
     * @param form   IForm实例
     * @return Entity实例
     */
    public static <E extends IEntity> E newEntity(Class<E> eClass, Object form) {
        assertNotNull("FormObject", form);
        FormMetaList metas = FormMetaList.getFormMeta(form.getClass());
        AMapping mapping = RefKit.byEntity(eClass);
        IEntity entity = mapping.newEntity();
        for (FormFieldMeta meta : metas) {
            Object value = meta.get(form);
            if (meta.isIgnoreNull() && value == null) {
                continue;
            }
            FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.getName());
            fm.setter.set(entity, value);
        }
        return (E) entity;
    }

    public static <E extends IEntity> IQuery<E> newQuery(Class<E> eClass, Object form) {
        assertNotNull("FormObject", form);
        FormMetaList metas = FormMetaList.getFormMeta(form.getClass());
        AMapping mapping = RefKit.byEntity(eClass);
        IQuery<E> query = mapping.query();
        for (FormFieldMeta meta : metas) {
            Object value = meta.get(form);
            if (meta.isIgnoreNull() && value == null) {
                continue;
            }
            FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.getName());
            where((IWrapper) query, fm.column, meta, value);
        }
        if (form instanceof IPaged) {
            setPaged(mapping, query, (IPaged) form);
        }
        return query;
    }

    private static void setPaged(AMapping mapping, IQuery query, IPaged paged) {
        int pageSize = paged.getPageSize();
        if (pageSize < 1) {
            throw new RuntimeException("PageSize must be greater than 0.");
        }
        Integer currPage = paged.getCurrPage();
        String startTag = paged.getStartPk();
        if (currPage == null && startTag == null || currPage != null && startTag != null) {
            throw new RuntimeException("Paging operation, getCurrPage and getStartPk must be assigned one and only one");
        } else if (currPage == null) {
            String pk = mapping.primaryId(true);
            query.where().apply(pk, SqlOp.GE, paged.getStartPk());
            query.limit(pageSize);
        } else {
            query.limit(currPage * pageSize, pageSize);
        }
    }

    public static <E extends IEntity> IUpdate<E> newUpdate(Class<E> eClass, Object form) {
        assertNotNull("FormObject", form);
        FormMetaList metas = FormMetaList.getFormMeta(form.getClass());
        AMapping mapping = RefKit.byEntity(eClass);
        IUpdate<E> updater = mapping.updater();
        for (FormFieldMeta meta : metas) {
            Object value = meta.get(form);
            if (meta.isIgnoreNull() && value == null) {
                continue;
            }
            FieldMapping fm = (FieldMapping) mapping.getFieldsMap().get(meta.getName());
            if (meta.getType() == EntryType.UPDATE) {
                updater.updateSet(fm.column, value);
            } else {
                where((IWrapper) updater, fm.column, meta, value);
            }
        }
        return updater;
    }

    private static void where(IWrapper wrapper, String column, FormFieldMeta meta, Object value) {
        switch (meta.getType()) {
            case EQ:
                if (value == null) {
                    wrapper.where().apply(column, SqlOp.IS_NULL);
                } else {
                    wrapper.where().apply(column, SqlOp.EQ, value);
                }
                break;
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
                wrapper.where().apply(column, SqlOp.IN, toArray(meta.getGetter().getName(), value));
                break;
            case LIKE:
                wrapper.where().apply(column, SqlOp.LIKE, "%" + value + "%");
                break;
            case LIKE_LEFT:
                wrapper.where().apply(column, SqlOp.LIKE, value + "%");
                break;
            case LIKE_RIGHT:
                wrapper.where().apply(column, SqlOp.LIKE, "%" + value);
                break;
            case BETWEEN:
                wrapper.where().apply(column, SqlOp.BETWEEN, toArray(meta.getGetter().getName(), value));
                break;
            default:
                //throw new RuntimeException("there must be something wrong.");
        }
    }

    private static Object[] toArray(String methodName, Object object) {
        assertNotNull("result of method[" + methodName + "]", object);
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
            throw new RuntimeException("The result of method[" + methodName + "] should be an array or a collection.");
        }
        return list.toArray();
    }
}