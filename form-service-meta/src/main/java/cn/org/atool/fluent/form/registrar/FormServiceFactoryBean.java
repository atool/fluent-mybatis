package cn.org.atool.fluent.form.registrar;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * FormServiceFactoryBean: FormService bean封装工厂
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "rawtypes"})
public class FormServiceFactoryBean implements FactoryBean {

    private final Class serviceClass;

    private final Class aroundClass;

    public FormServiceFactoryBean(Class serviceClass, Class aroundClass) {
        this.serviceClass = serviceClass;
        this.aroundClass = aroundClass;
    }

    @Override
    public Class<?> getObjectType() {
        return this.serviceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private volatile Object proxy;

    @Override
    public Object getObject() {
        if (this.proxy == null) {
            /* 为避免在MapperFactory.init()方法前执行proxy实例化,
            proxy实例化放到getObject()中执行, 不能放构造函数中执行*/
            synchronized (this) {
                if (this.proxy == null) {
                    this.proxy = FormServiceProxy.create(serviceClass, aroundClass);
                }
            }
        }
        return this.proxy;
    }

    /**
     * FactoryBean的 {@link InvocationHandler#invoke(Object, Method, Object[])} 实现
     */
    private Object invoke(Object target, Method method, Object[] args) throws Throwable {
        return method.invoke(this.getObject(), args);
    }
}