package cn.org.atool.fluent.mybatis.form.registrar;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

public class FormApiBean implements FactoryBean {
    private Class apiInterface;

    public FormApiBean() {
        // intentionally empty
    }

    public FormApiBean(Class apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Object getObject() {
        InvocationHandler handler = new FormApiInvocationHandler(this.apiInterface);
        return Proxy.newProxyInstance(this.apiInterface.getClassLoader(), new Class[]{this.apiInterface}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return this.apiInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
