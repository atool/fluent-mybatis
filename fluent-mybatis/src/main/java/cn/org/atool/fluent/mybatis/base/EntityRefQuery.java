package cn.org.atool.fluent.mybatis.base;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * EntityRefQuery: Entity @RefMethod关联关系, 关联加载基类
 *
 * @author darui.wu
 */
public abstract class EntityRefQuery {
    /**
     * 单例变量, 需要被Spring容器初始化时赋值
     */
    private static EntityRefQuery query;

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return
     */
    public static EntityRefQuery query() {
        if (query == null) {
            throw new RuntimeException("the LazyFinder must be defined as a spring bean.");
        }
        return query;
    }

    /**
     * 根据methodName和entity获取entity的关联信息
     *
     * @param methodName 方法名称
     * @param args       方法参数列表
     * @param <T>
     * @return
     */
    public <T> T load(String methodName, Object[] args) {
        if (!methods.containsKey(methodName)) {
            throw new RuntimeException("the method[" + methodName + "] not defined or wrong define.");
        }
        Method method = methods.get(methodName);
        try {
            return (T) method.invoke(this, args);
        } catch (Exception e) {
            String err = String.format("invoke method[%s] error:%s", methodName, e.getMessage());
            throw new RuntimeException(err, e);
        }
    }

    private Map<String, Method> methods = new HashMap<>(16);

    @PostConstruct
    public void init() {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() == 0) {
                continue;
            }
            Class parameterType = method.getParameterTypes()[0];
            if (RichEntity.class.isAssignableFrom(parameterType)) {
                this.methods.put(method.getName(), method);
            }
        }
        query = this;
    }
}