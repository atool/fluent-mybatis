package cn.org.atool.fluent.mybatis.form.registrar;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class FormApiInvocationHandler implements InvocationHandler {
    private Class apiInterface;

    public FormApiInvocationHandler(Class apiInterface) {
        this.apiInterface = apiInterface;
    }

    @Override
    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        return null;
    }
}
