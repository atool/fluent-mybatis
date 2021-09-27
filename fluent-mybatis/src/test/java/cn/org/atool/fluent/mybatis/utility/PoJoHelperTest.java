package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IRef;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.IWant;
import org.test4j.tools.datagen.DataMap;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
class PoJoHelperTest implements IWant {

    @Test
    void toPoJo() {
        TestBean bean = PoJoHelper.toPoJo(TestBean.class, DataMap.create(1)
            .kv("user_name", "myName")
            .kv("post_code", "310000")
            .map());
        want.object(bean).eqReflect(new TestBean()
            .setUserName("myName")
            .setPostCode("310000"));
    }

    @Test
    void toPoJo2() {
        IRef.register("java.util.List<java.lang.String>", obj -> Arrays.asList(String.valueOf(obj).split(";")));
        TestBean bean = PoJoHelper.toPoJoIgnoreNotFound(TestBean.class, DataMap.create(1)
            .kv("user_name", "myName")
            .kv("post_code", "310000")
            .kv("alias", "alias")
            .kv("list", "2323;232;232")
            .map());
        want.object(bean).eqReflect(new TestBean()
            .setUserName("myName")
            .setPostCode("310000")
            .setList(Arrays.asList("2323", "232", "232")));
    }

    @Data
    @Accessors(chain = true)
    static class TestBean {
        private String userName;

        private String postCode;

        private List<String> list;
    }
}