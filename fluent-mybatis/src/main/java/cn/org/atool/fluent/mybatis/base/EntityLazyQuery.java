package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.annotation.RefMethod;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity @RefMethod关联关系, 懒加载实现基类
 *
 * @author darui.wu
 */
public abstract class EntityLazyQuery {
    /**
     * 单例变量, 被Spring容器初始化时赋值
     */
    private static EntityLazyQuery query;

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return
     */
    public static EntityLazyQuery query() {
        if (query == null) {
            throw new RuntimeException("the LazyFinder must be defined as a spring bean.");
        }
        return query;
    }

    /**
     * 根据relation和entity获取entity的关联信息
     *
     * @param relation
     * @param entity
     * @param <T>
     * @return
     */
    public <T> T load(String relation, RichEntity entity) {
        if (!methods.containsKey(relation)) {
            throw new RuntimeException("not found method for relation:" + relation);
        }
        Method method = methods.get(relation);
        try {
            return (T) method.invoke(this, entity);
        } catch (Exception e) {
            String err = String.format("load relation[%s] of entity[%s] by method[%s] error:%s",
                relation, entity, method.getName(), e.getMessage());
            throw new RuntimeException(err, e);
        }
    }

    private Map<String, Method> methods = new HashMap<>(16);

    @PostConstruct
    public void init() {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterCount() != 1) {
                continue;
            }
            RefMethod ref = method.getAnnotation(RefMethod.class);
            if (ref == null) {
                continue;
            }
            Class parameterType = method.getParameterTypes()[0];
            if (parameterType.isAssignableFrom(RichEntity.class)) {
                throw new RuntimeException("The type of parameter must be " + RichEntity.class.getSimpleName());
            }
            this.methods.put(ref.value(), method);
        }
        query = this;
    }
}