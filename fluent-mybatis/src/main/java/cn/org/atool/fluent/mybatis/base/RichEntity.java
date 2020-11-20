package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.base.entity.IRichEntity;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Entity Rich基类
 *
 * @author darui.wu
 */
public abstract class RichEntity implements IEntity, IRichEntity {

    /**
     * 数据缓存, 避免多次查询
     */
    @NotField
    protected transient Map<String, Optional<Object>> cached = new ConcurrentHashMap<>(4);

    /**
     * 加载关联信息
     *
     * @param cached     true: 进行缓存, false:直接查询
     * @param methodName method name of entity
     * @param args       方法参数
     * @param <T>
     */
    private <T> T invoke(boolean cached, String methodName, Object[] args) {
        if (cached) {
            if (this.cached.containsKey(methodName)) {
                return (T) this.cached.get(methodName).orElse(null);
            }
            synchronized (this) {
                if (this.cached.containsKey(methodName)) {
                    return (T) this.cached.get(methodName).orElse(null);
                }
                T result = IRefs.instance().invoke(this.getClass(), methodName, reArgs(args));
                this.cached.put(methodName, Optional.ofNullable(result));
                return result;
            }
        } else {
            return IRefs.instance().invoke(this.getClass(), methodName, this.reArgs(args));
        }
    }

    /**
     * 加载关联信息
     *
     * @param method method name
     * @param clazz  entity class name
     * @param <T>
     * @return
     * @deprecated used invoke(cache, method) directly
     */
    @Deprecated
    protected <T> T loadCache(String method, Class clazz) {
        return (T) this.invoke(true, method, new Object[0]);
    }

    /**
     * 加载关联信息
     *
     * @param method 方法名称
     * @param cached 是否缓存结果
     * @param <T>
     * @return
     */
    public <T> T invoke(String method, boolean cached) {
        return (T) this.invoke(cached, method, new Object[0]);
    }

    /**
     * 添加 entity实例 到参数列表作为第一个参数
     *
     * @param args
     * @return
     */
    private Object[] reArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return new Object[]{this};
        }
        Object[] reArgs = new Object[args.length + 1];
        reArgs[0] = this;
        for (int index = 0; index < args.length; index++) {
            reArgs[index + 1] = args[index];
        }
        return reArgs;
    }
}