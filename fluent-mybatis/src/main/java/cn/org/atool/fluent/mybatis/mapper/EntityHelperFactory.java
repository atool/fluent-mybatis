package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.entity.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.IEntityHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_EntityHelper;

/**
 * 获取和构建Entity对应的帮助类实例
 *
 * @author wudarui
 */
public class EntityHelperFactory {
    private final static Map<Class<? extends IEntity>, IEntityHelper> INSTANCES = new HashMap<>();

    private final static Lock lock = new ReentrantLock(true);

    /**
     * 采用反射构造方式
     *
     * @param clazz
     * @return
     */
    public static IEntityHelper getInstance(Class<? extends IEntity> clazz) {
        if (INSTANCES.containsKey(clazz)) {
            return INSTANCES.get(clazz);
        }
        try {
            lock.lock();
            if (INSTANCES.containsKey(clazz)) {
                return INSTANCES.get(clazz);
            }
            List<ClassLoader> classLoaders = new ArrayList<ClassLoader>(3);
            classLoaders.add(clazz.getClassLoader());

            if (Thread.currentThread().getContextClassLoader() != null) {
                classLoaders.add(Thread.currentThread().getContextClassLoader());
            }
            classLoaders.add(IEntityHelper.class.getClassLoader());
            IEntityHelper helper = getEntityHelper(clazz, classLoaders);
            INSTANCES.put(clazz, helper);
            return helper;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private static IEntityHelper getEntityHelper(Class<? extends IEntity> clazz, List<ClassLoader> classLoaders) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            IEntityHelper helper = doGetEntityHelper(clazz, classLoader);
            if (helper != null) {
                return helper;
            }
        }
        throw new ClassNotFoundException("Cannot find entity helper for " + clazz.getName());
    }

    private static IEntityHelper doGetEntityHelper(Class<? extends IEntity> clazz, ClassLoader classLoader) {
        try {
            return (IEntityHelper) classLoader.loadClass(clazz.getName() + Suffix_EntityHelper).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
