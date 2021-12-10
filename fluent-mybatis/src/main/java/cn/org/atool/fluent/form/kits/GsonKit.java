package cn.org.atool.fluent.form.kits;

import cn.org.atool.fluent.form.Form;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.DateStrFormat;

/**
 * gson实现
 *
 * @author wudarui
 */
public class GsonKit {
    public static final Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .setDateFormat(DateStrFormat)
        .create();

    public static Form form(String json) {
        return gson.fromJson(json, Form.class);
    }
}