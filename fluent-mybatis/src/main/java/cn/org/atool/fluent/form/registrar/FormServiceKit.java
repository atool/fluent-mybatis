package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.common.kits.KeyMap;
import cn.org.atool.fluent.form.annotation.FormMethod;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.meta.ClassKit;
import cn.org.atool.fluent.form.meta.MethodArgs;
import cn.org.atool.fluent.form.meta.MethodMeta;
import cn.org.atool.fluent.form.setter.FormHelper;
import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.model.StdPagedList;
import cn.org.atool.fluent.mybatis.model.TagPagedList;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 静态方法
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public interface FormServiceKit {

    /**
     * table name 和 Entity class映射定义
     */
    KeyMap<Class> TableEntityClass = new KeyMap();

    /**
     * 构造eClass实体实例
     *
     * @param meta 操作定义
     * @param args 入参
     * @return entity实例
     */
    static Object save(MethodMeta meta, Object... args) {
        IEntity entity = FormHelper.newEntity(meta, args);
        Object pk = RefKit.mapper(meta.entityClass).save(entity);
        if (meta.isReturnVoid()) {
            return null;
        } else if (meta.isReturnBool()) {
            return pk != null;
        } else if (meta.returnType.isAssignableFrom(meta.entityClass)) {
            return entity;
        } else {
            return FormHelper.entity2result(entity, meta.returnType);
        }
    }

    /**
     * 批量插入
     *
     * @param meta 入参元数据
     * @param list 入参列表
     * @return entity实例
     */
    static Object save(MethodMeta meta, Collection list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the save list can't be empty.");
        }
        List<IEntity> entities = new ArrayList<>();
        for (Object obj : list) {
            IEntity entity = FormHelper.newEntity(meta, new Object[]{obj});
            entities.add(entity);
        }
        int count = RefKit.mapper(meta.entityClass).save(entities);
        return returnUpdateResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta 方法元数据
     * @param list 入参是List
     * @return ignore
     */
    static Object delete(MethodMeta meta, Collection list, boolean isLogic) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the delete list can't be empty.");
        }
        List<Integer> counts = new ArrayList<>(list.size());
        for (Object obj : list) {
            IQuery query = FormHelper.newQuery(new MethodArgs(meta, new Object[]{obj}));
            int count;
            if (isLogic) {
                count = RefKit.mapper(meta.entityClass).logicDelete(query);
            } else {
                count = RefKit.mapper(meta.entityClass).delete(query);
            }
            counts.add(count);
        }
        return returnUpdateResult(meta, counts);
    }

    /**
     * 更新操作
     *
     * @param meta 操作定义
     * @param args 入参
     * @return ignore
     */
    static Object update(MethodMeta meta, Object... args) {
        IUpdate update = FormHelper.newUpdate(new MethodArgs(meta, args));
        int count = RefKit.mapper(meta.entityClass).updateBy(update);
        return returnUpdateResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta    操作定义
     * @param isLogic 是否逻辑删除
     * @param args    入参
     * @return ignore
     */
    static Object delete(MethodMeta meta, boolean isLogic, Object... args) {
        IQuery query = FormHelper.newQuery(new MethodArgs(meta, args));
        int count;
        if (isLogic) {
            count = RefKit.mapper(meta.entityClass).logicDelete(query);
        } else {
            count = RefKit.mapper(meta.entityClass).delete(query);
        }
        return returnUpdateResult(meta, count);
    }

    /**
     * 更新操作
     *
     * @param meta 方法元数据
     * @param list 入参是List
     * @return ignore
     */
    static Object update(MethodMeta meta, Collection list) {
        if (list.size() == 0) {
            throw new IllegalArgumentException("the update list can't be empty.");
        }
        IUpdate[] updates = new IUpdate[list.size()];
        int index = 0;
        for (Object obj : list) {
            IUpdate update = FormHelper.newUpdate(new MethodArgs(meta, new Object[]{obj}));
            updates[index++] = update;
        }
        int count = RefKit.mapper(meta.entityClass).updateBy(updates);
        return returnUpdateResult(meta, count);
    }

    /**
     * 构造查询条件实例
     *
     * @param meta 操作定义
     * @param args 入参
     * @return 查询实例
     */
    static Object query(MethodMeta meta, Object... args) {
        IQuery query = FormHelper.newQuery(new MethodArgs(meta, args));
        if (meta.isCount()) {
            int count = query.to().count();
            return meta.isReturnLong() ? (long) count : count;
        } else if (meta.isStdPage()) {
            /* 标准分页 */
            StdPagedList paged = query.to().stdPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), meta.returnParameterType);
            return paged.setData(data);
        } else if (meta.isTagPage()) {
            /* Tag分页 */
            TagPagedList paged = query.to().tagPagedEntity();
            List data = FormHelper.entities2result(paged.getData(), meta.returnParameterType);
            IEntity next = (IEntity) paged.getNext();
            return new TagPagedList(data, next == null ? null : next.findPk());
        } else if (meta.isList()) {
            /* 返回List */
            List<IEntity> list = query.to().listEntity();
            return FormHelper.entities2result(list, meta.returnParameterType);
        } else if (meta.isReturnBool()) {
            /* exists操作 */
            ((BaseQuery) query).select("1").limit(1);
            Optional ret = query.to().findOneMap();
            return ret.isPresent();
        } else {
            /* 查找单条数据 */
            query.limit(1);
            IEntity entity = (IEntity) query.to().findOne().orElse(null);
            return FormHelper.entity2result(entity, meta.returnType);
        }
    }

    static Object returnUpdateResult(MethodMeta meta, int count) {
        if (meta.isReturnVoid()) {
            return null;
        } else if (meta.isReturnBool()) {
            return count > 0;
        } else if (meta.isReturnInt()) {
            return count;
        } else if (meta.isReturnLong()) {
            return (long) count;
        } else {
            throw new IllegalStateException("The type of batch result can only be: void, int, long, or boolean.");
        }
    }

    static Object returnUpdateResult(MethodMeta meta, List<Integer> counts) {
        if (meta.isReturnList()) {
            return counts;
        } else {
            int total = 0;
            for (int c : counts) {
                total += c;
            }
            return returnUpdateResult(meta, total);
        }
    }

    /**
     * 根据{@link FormMethod}或{@link FormService}注解上声明的entityClass和entityTable
     * 值解析实际的EntityClass值
     *
     * @param className   Entity类
     * @param entityTable 表名称
     * @return 有效的Entity Class
     */
    static Class getEntityClass(String className, String entityTable) {
        if (If.notBlank(entityTable)) {
            return FormServiceKit.getEntityClass(entityTable);
        } else if (Objects.equals(Object.class.getName(), className)) {
            return Object.class;
        } else {
            Class entityClass = ClassKit.forName(className);
            if (IEntity.class.isAssignableFrom(entityClass)) {
                return entityClass;
            } else {
                throw new RuntimeException("The value of entity() of @FormService must be a subclass of IEntity.");
            }
        }
    }

    /**
     * 根据{@link FormMethod}或{@link FormService}注解上声明的entityClass和entityTable
     * 值解析实际的EntityClass值
     *
     * @param entityClass Entity类
     * @param entityTable 表名称
     * @return 有效的Entity Class
     */
    static Class getEntityClass(Class entityClass, String entityTable) {
        if (If.notBlank(entityTable)) {
            return FormServiceKit.getEntityClass(entityTable);
        } else if (Object.class.equals(entityClass)) {
            return Object.class;
        } else if (IEntity.class.isAssignableFrom(entityClass)) {
            return entityClass;
        } else {
            throw new RuntimeException("The value of entity() of @FormService must be a subclass of IEntity.");
        }
    }

    /**
     * 根据表名称获取实例类型
     *
     * @param table 表名称
     * @return 实例类型
     */
    static Class<? extends IEntity> getEntityClass(String table) {
        if (If.isBlank(table)) {
            return null;
        }
        if (TableEntityClass.containsKey(table)) {
            return TableEntityClass.get(table);
        }
        AMapping mapping = RefKit.byTable(table);
        if (mapping == null) {
            throw new RuntimeException("The table[" + table + "] not found.");
        } else {
            return mapping.entityClass();
        }
    }

    /**
     * 执行Form操作
     *
     * @param args 方法执行行为
     * @return 执行结果
     */
    static <T> T invoke(MethodMeta meta, Object[] args) {
        if (meta.isOneArgListOrArray() && !meta.isQuery()) {
            Collection list = asCollection(args[0]);
            if (meta.isSave()) {
                return (T) FormServiceKit.save(meta, list);
            } else if (meta.isDelete()) {
                return (T) FormServiceKit.delete(meta, list, false);
            } else if (meta.isLogicDelete()) {
                return (T) FormServiceKit.delete(meta, list, true);
            } else {
                return (T) FormServiceKit.update(meta, list);
            }
        } else {
            if (meta.isSave()) {
                return (T) FormServiceKit.save(meta, args);
            } else if (meta.isUpdate()) {
                return (T) FormServiceKit.update(meta, args);
            } else if (meta.isDelete()) {
                return (T) FormServiceKit.delete(meta, false, args);
            } else if (meta.isLogicDelete()) {
                return (T) FormServiceKit.delete(meta, true, args);
            } else {
                return (T) FormServiceKit.query(meta, args);
            }
        }
    }

    static Collection asCollection(Object arg) {
        if (arg instanceof Collection) {
            return (Collection) arg;
        } else {
            return Arrays.asList((Object[]) arg);
        }
    }

    Map<String, MethodMeta> METHOD_CACHED = new HashMap<>();

    static MethodMeta meta(Class klass, String methodName, Class... pClasses) {
        String key = Arrays.stream(pClasses).map(Class::getName)
            .collect(Collectors.joining(",", klass.getName() + "#" + methodName + "(", ")"));
        if (METHOD_CACHED.containsKey(key)) {
            return METHOD_CACHED.get(key);
        }
        try {
            FormService annotation = (FormService) klass.getAnnotation(FormService.class);
            Class entityClass = getEntityClass(annotation.entity(), annotation.table());
            Method method = klass.getMethod(methodName, pClasses);
            return MethodMeta.meta(entityClass, method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("find method error:" + e.getMessage(), e);
        }
    }
}