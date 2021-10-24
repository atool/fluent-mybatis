package cn.org.atool.fluent.form;

/**
 * Form请求入参
 */
public interface FormProcessor {
    /**
     * 获取请求参数中的IForm数据
     *
     * @param request
     * @param <F>
     * @param <R>
     * @return
     */
    <F extends IForm, R> F getParam(R request);

    <R, D> R setResult(D data);
}
