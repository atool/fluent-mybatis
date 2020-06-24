package cn.org.atool.fluent.mybatis.setter;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ClassSetterMethod extends HashMap<String, Method> {
    private final Class klass;

    public ClassSetterMethod(Class beanClass) {
        this.klass = beanClass;
        this.init();
    }

    private void init() {
        Method[] methods = this.klass.getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() != 1) {
                continue;
            }
            Class type = method.getParameterTypes()[0];
            String name = method.getName();
            if (name.startsWith("is") && type == boolean.class) {
                name.substring(2);
            } else if (name.startsWith("set")) {
                name.substring(3);
            }
        }
    }
}
