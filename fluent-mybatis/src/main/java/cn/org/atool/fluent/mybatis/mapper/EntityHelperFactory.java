package cn.org.atool.fluent.mybatis.mapper;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.IEntityHelper;

import java.util.*;
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
            Set<ClassLoader> classLoaders = getClassLoaders(clazz);
            IEntityHelper helper = loadEntityHelper(clazz, classLoaders);
            INSTANCES.put(clazz, helper);
            return helper;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    protected static Set<ClassLoader> getClassLoaders(Class<? extends IEntity> clazz) {
        Set<ClassLoader> classLoaders = new HashSet<>(3);
        classLoaders.add(clazz.getClassLoader());

        if (Thread.currentThread().getContextClassLoader() != null) {
            classLoaders.add(Thread.currentThread().getContextClassLoader());
        }
        classLoaders.add(IEntityHelper.class.getClassLoader());
        return classLoaders;
    }

    /**
     * 加载entityClass对应的Helper类
     *
     * @param entityClass
     * @param classLoaders
     * @return
     */
    private static IEntityHelper loadEntityHelper(Class<? extends IEntity> entityClass, Set<ClassLoader> classLoaders) throws ClassNotFoundException {
        String helperName = entityClass.getName() + Suffix_EntityHelper;
        for (ClassLoader classLoader : classLoaders) {
            IEntityHelper helper = null;
            try {
                helper = (IEntityHelper) classLoader.loadClass(helperName).getConstructor().newInstance();
            } catch (Exception e) {
                helper = null;
            }
            if (helper != null) {
                return helper;
            }
        }
        throw new ClassNotFoundException("Cannot find entity helper class: " + helperName);
    }
}