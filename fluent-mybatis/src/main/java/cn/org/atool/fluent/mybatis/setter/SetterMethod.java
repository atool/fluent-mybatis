package cn.org.atool.fluent.mybatis.setter;

import java.lang.reflect.Method;

public class SetterMethod {
    private Method method;

    private Class parameterType;

    public SetterMethod(Method method, Class parameterType) {
        this.method = method;
        this.parameterType = parameterType;
    }
}
