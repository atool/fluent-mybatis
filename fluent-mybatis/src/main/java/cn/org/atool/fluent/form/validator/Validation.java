package cn.org.atool.fluent.form.validator;

import cn.org.atool.fluent.mybatis.If;
import org.springframework.validation.beanvalidation.MethodValidationInterceptor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static cn.org.atool.fluent.common.kits.ParameterizedTypes.notFormObject;
import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * 校验工具
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes"})
public class Validation {
    private static final ValidatorFactory factory = javax.validation.Validation.buildDefaultValidatorFactory();
    private static final Validator objectValidator = factory.getValidator();
    private static final ExecutableValidator methodValidator = factory.getValidator().forExecutables();

    /**
     * 对Form Object进行校验
     *
     * @param objects Form对象
     */
    public static void validate(String prefix, Object... objects) throws IllegalArgumentException {
        if (objects == null) {
            throw new IllegalArgumentException("validate object can't be null.");
        } else if (objects.length == 0 || isFirstObjectNotForm(objects[0])) {
            return;
        }
        int index = -1;
        for (Object object : objects) {
            index++;
            if (object instanceof Collection) {
                validate((isBlank(prefix) ? EMPTY : prefix) + "[" + index + "]", ((Collection) object).toArray());
            } else if (object instanceof Object[]) {
                validate((isBlank(prefix) ? EMPTY : prefix) + "[" + index + "]", (Object[]) object);
            } else {
                Set<ConstraintViolation<Object>> violations = objectValidator.validate(object);
                doValidateResult(prefix, violations);
                validateArgsByIValidate(object);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean isFirstObjectNotForm(Object object) {
        Object first = object;
        if (object instanceof Collection) {
            first = ((Collection) object).stream().filter(If::notNull).findFirst().orElse(null);
        } else if (object instanceof Object[]) {
            first = Arrays.stream((Object[]) object).filter(If::notNull).findFirst().orElse(null);
        }
        return first == null || notFormObject(first.getClass());
    }

    /**
     * 验证方法参数合法性
     *
     * @param target 方法所属类型实例对象
     * @param method 方法
     * @param args   方法实际入参
     * @throws IllegalArgumentException 参数异常
     * @see MethodValidationInterceptor
     */
    public static <T> void validate(T target, Method method, Object[] args) throws IllegalArgumentException {
        if (method == null || target == null) {
            throw new IllegalArgumentException("validate object can't be null.");
        }

        Set<ConstraintViolation<T>> violations = methodValidator.validateParameters(target, method, args);
        doValidateResult(null, violations);
        validateArgsByIValidate(args);
        validateFormArg(method, args);
    }

    /**
     * 对没有显式标注 @Validate 注解的Form类单独调用校验方法
     *
     * @param method 方法
     * @param args   参数列表
     */
    private static void validateFormArg(Method method, Object[] args) {
        int index = -1;
        for (Parameter p : method.getParameters()) {
            index++;
            Annotation[] as = p.getDeclaredAnnotations();
            boolean hasValidation = false;
            for (Annotation a : as) {
                if (a.getClass().getName().startsWith("javax.validation.")) {
                    hasValidation = true;
                    break;
                }
            }
            String prefix = method.getName() + ".arg" + index;
            if (Collection.class.isAssignableFrom(p.getType())) {
                validate(prefix, ((Collection) args[index]).toArray());
            } else if (Object[].class.isAssignableFrom(p.getType())) {
                validate(prefix, (Object[]) args[index]);
            } else if (!hasValidation && !notFormObject(p.getType())) {
                validate(prefix, args[index]);
            }
        }
    }

    /**
     * 调用方法 {@link IValidator#validate()} 对参数进行校验
     *
     * @param args 参数列表
     */
    private static void validateArgsByIValidate(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                continue;
            }
            if (arg instanceof IValidator) {
                ((IValidator) arg).validate();
            } else if (arg instanceof Collection) {
                validateArgsByIValidate(((Collection) arg).toArray());
            } else if (arg instanceof Object[]) {
                validateArgsByIValidate((Object[]) arg);
            }
        }
    }

    /**
     * 对 validation 结果进行处理
     *
     * @param violations validation 结果
     */
    private static <T> void doValidateResult(String prefix, Set<ConstraintViolation<T>> violations) {
        List<String> messageList = new ArrayList<>();
        for (ConstraintViolation<T> v : violations) {
            messageList.add((isBlank(prefix) ? EMPTY : prefix + ".") + v.getPropertyPath() + ": " + v.getMessage());
        }
        String message = String.join("\n", messageList);
        if (!message.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}