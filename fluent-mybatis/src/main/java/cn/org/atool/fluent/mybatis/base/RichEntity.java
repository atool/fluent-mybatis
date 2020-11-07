package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.annotation.NotField;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类处理
 *
 * @author darui.wu
 */
public abstract class RichEntity implements IEntity {
    /**
     * 数据缓存, 避免多次查询
     */
    @NotField
    protected transient Map<String, Object> cached = new HashMap<>(4);

    /**
     * 加载关联信息
     *
     * @param cached         true: 进行缓存, false:直接查询
     * @param methodOfEntity method name of entity
     * @param args           方法参数
     * @param <T>
     */
    protected <T> T loadCache(boolean cached, String methodOfEntity, Object[] args) {
        if (!cached) {
            this.cached.remove(methodOfEntity);
            return EntityRefQuery.query().load(methodOfEntity, this.reArgs(args));
        }
        if (this.cached.containsKey(methodOfEntity)) {
            return (T) this.cached.get(methodOfEntity);
        }
        synchronized (this) {
            if (this.cached.containsKey(methodOfEntity)) {
                return (T) this.cached.get(methodOfEntity);
            }
            T result = EntityRefQuery.query().load(methodOfEntity, reArgs(args));
            this.cached.put(methodOfEntity, result);
            return result;
        }
    }

    /**
     * 加载关联信息
     *
     * @param method      method name
     * @param entityClass entity class name
     * @param <T>
     * @return
     */
    protected <T> T loadCache(String method, Class<? extends RichEntity> entityClass) {
        return this.loadCache(true, method + "Of" + entityClass.getSimpleName(), new Object[0]);
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