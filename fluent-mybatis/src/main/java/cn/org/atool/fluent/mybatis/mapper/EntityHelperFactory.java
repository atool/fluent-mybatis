package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IEntityHelper;

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

    public static <T extends IEntity> IEntityHelper<T> getInstance(Class<T> clazz) {
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

    private static <T extends IEntity> IEntityHelper getEntityHelper(Class<T> clazz, List<ClassLoader> classLoaders) throws ClassNotFoundException {
        for (ClassLoader classLoader : classLoaders) {
            IEntityHelper<T> helper = doGetEntityHelper(clazz, classLoader);
            if (helper != null) {
                return helper;
            }
        }
        throw new ClassNotFoundException("Cannot find entity helper for " + clazz.getName());
    }

    private static <T extends IEntity> IEntityHelper doGetEntityHelper(Class<T> clazz, ClassLoader classLoader) {
        try {
            return (IEntityHelper<T>) classLoader.loadClass(clazz.getName() + Suffix_EntityHelper).newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
