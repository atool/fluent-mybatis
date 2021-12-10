package cn.org.atool.fluent.form.validator;

/**
 * 参数校验, 除注解验证外的方法验证
 *
 * @author darui.wu
 */
public interface IValidator {
    /**
     * 校验方法
     *
     * @throws IllegalArgumentException 参数非法异常
     */
    default void validate() throws IllegalArgumentException {
    }
}