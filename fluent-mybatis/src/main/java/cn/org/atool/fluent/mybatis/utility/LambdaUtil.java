package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.functions.IGetter;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.PRE_GET;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.PRE_IS;

/**
 * lambda表达式处理
 *
 * @author darui.wu
 */
public class LambdaUtil {

    /**
     * 返回getter lambda表达式
     *
     * @param getter getter method lambda
     * @return name of getter method
     */
    public static <E> String resolveGetter(IGetter<E> getter) {
        String method = lambdaName(getter);
        return validateMethod(method);
    }

    public static <E> String resolve(IGetter<E> getter) {
        return lambdaName(getter);
    }

    @SuppressWarnings("all")
    private static String lambdaName(Object lambda) {
        try {
            String m_WRITE_REPLACE = "writeReplace";
            Method writeReplace = lambda.getClass().getDeclaredMethod(m_WRITE_REPLACE);
            boolean accessible = writeReplace.isAccessible();
            writeReplace.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(lambda);
            String method = serializedLambda.getImplMethodName();
            writeReplace.setAccessible(accessible);
            return method;
        } catch (Exception e) {
            throw new RuntimeException("can't parser getter name of " + lambda.getClass().getSimpleName(), e);
        }
    }

    private static String validateMethod(String method) {
        if (method.startsWith(PRE_GET)) {
            return method.substring(3, 4).toLowerCase() + method.substring(4);
        } else if (method.startsWith(PRE_IS)) {
            return method.substring(2, 3).toLowerCase() + method.substring(3);
        } else {
            throw new RuntimeException("not a getter method, please use lambda as 'entity::getXyz'");
        }
    }
}