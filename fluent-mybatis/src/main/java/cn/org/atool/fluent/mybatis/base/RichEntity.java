package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.base.entity.IRichEntity;
import cn.org.atool.fluent.mybatis.utility.RefKit;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

/**
 * Entity基类, 充血模式
 *
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
public abstract class RichEntity extends BaseEntity implements IRichEntity {
    /**
     * 当RichEntity作为查询参数时, params用于设置额外的参数, 比如范围（开始,结束）等
     */
    @Getter
    @NotField
    protected transient Map<String, Object> inlayParams = new ConcurrentHashMap<>(4);

    /**
     * 设置params参数
     *
     * @param key   参数key
     * @param value 参数值
     * @return self
     */
    public RichEntity inlayParams(String key, Object value) {
        this.inlayParams.put(key, value);
        return this;
    }

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
     * @param <T>        实例类型
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
                T result = RefKit.invoke(this.entityClass(), methodName, reArgs(args));
                if (result != null || !this.cached.containsKey(methodName)) {
                    /* 非多对多关联关系在RelateFunction2中设置 */
                    this.cached.put(methodName, Optional.ofNullable(result));
                }
                return (T) this.cached.get(methodName).orElse(null);
            }
        } else {
            return RefKit.invoke(this.entityClass(), methodName, this.reArgs(args));
        }
    }

    void cached(String methodName, Object cached) {
        this.cached.put(methodName, Optional.ofNullable(cached));
    }

    /**
     * 加载关联信息
     *
     * @param method 方法名称
     * @param cached 是否缓存结果
     * @param <T>    实例类型
     * @return ignore
     */
    public <T> T invoke(String method, boolean cached) {
        return this.invoke(cached, method, new Object[0]);
    }

    /**
     * 添加 entity实例 到参数列表作为第一个参数
     *
     * @param args 参数
     * @return ignore
     */
    private Object[] reArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return new Object[]{this};
        }
        Object[] reArgs = new Object[args.length + 1];
        reArgs[0] = this;
        System.arraycopy(args, 0, reArgs, 1, args.length);
        return reArgs;
    }
}