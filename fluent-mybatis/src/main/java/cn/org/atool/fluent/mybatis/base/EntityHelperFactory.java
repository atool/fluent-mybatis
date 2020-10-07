package cn.org.atool.fluent.mybatis.base;

import java.util.ArrayList;
import java.util.List;

public class EntityHelperFactory {
    public static <T extends IEntity> IEntityHelper getInstance(Class<T> clazz) {
        try {
            List<ClassLoader> classLoaders = new ArrayList<ClassLoader>(3);
            classLoaders.add(clazz.getClassLoader());

            if (Thread.currentThread().getContextClassLoader() != null) {
                classLoaders.add(Thread.currentThread().getContextClassLoader());
            }
            classLoaders.add(IEntityHelper.class.getClassLoader());
            return getEntityHelper(clazz, classLoaders);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
            return (IEntityHelper<T>) classLoader.loadClass(clazz.getName() + "Helper").newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
