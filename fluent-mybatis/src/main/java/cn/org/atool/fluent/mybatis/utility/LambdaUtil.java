package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.entity.IEntity;
import cn.org.atool.fluent.mybatis.functions.GetterFunc;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * lambda表达式处理
 *
 * @author darui.wu
 */
public class LambdaUtil {

    /**
     * 返回getter lambda表达式
     *
     * @param getter
     * @return
     */
    public static <E extends IEntity> String resolve(GetterFunc<E> getter) {
        String method = lambdaName(getter);
        return validateMethod(method);
    }

    private static <E extends IEntity> String lambdaName(GetterFunc<E> lambda) {
        try {
            Method writeReplace = lambda.getClass().getDeclaredMethod(M_WRITE_REPLACE);
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
        if (method.startsWith("get")) {
            return method.substring(3, 4).toLowerCase() + method.substring(4);
        } else if (method.startsWith("is")) {
            return method.substring(2, 3).toLowerCase() + method.substring(3);
        } else {
            throw new RuntimeException("not a getter method, please use lambda as 'entity::getXyz'");
        }
    }

    private static String M_WRITE_REPLACE = "writeReplace";
}