package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.model.form.Form;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * gson实现
 *
 * @author wudarui
 */
@SuppressWarnings({"unchecked"})
public class GsonKit {
    public static final Gson gson = new GsonBuilder()
        .disableHtmlEscaping()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create();

    public static Form form(String json) {
        return gson.fromJson(json, Form.class);
    }
}