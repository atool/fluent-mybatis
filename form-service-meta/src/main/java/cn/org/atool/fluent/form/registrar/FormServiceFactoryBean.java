package cn.org.atool.fluent.form.registrar;

import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * FormServiceFactoryBean: FormService bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public class FormServiceFactoryBean implements FactoryBean {
    public static final KeyMap<Class> TableEntityClass = new KeyMap();

    private final Class serviceClass;

    private final Object proxy;

    public FormServiceFactoryBean(Class serviceClass, Class aroundClass) {
        this.proxy = FormServiceProxy.create(serviceClass, aroundClass);
        this.serviceClass = serviceClass;
    }

    @Override
    public Class<?> getObjectType() {
        return this.serviceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object getObject() {
        return this.proxy;
    }

    /**
     * FactoryBean的 {@link InvocationHandler#invoke(Object, Method, Object[])} 实现
     */
    private Object invoke(Object target, Method method, Object[] args) throws Throwable {
        return method.invoke(this.proxy, args);
    }
}