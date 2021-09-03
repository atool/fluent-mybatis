package cn.org.atool.fluent.mybatis.utility;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.test4j.hamcrest.IWant;
import org.test4j.tools.datagen.DataMap;

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
        TestBean bean = PoJoHelper.toPoJoIgnoreNotFound(TestBean.class, DataMap.create(1)
            .kv("user_name", "myName")
            .kv("post_code", "310000")
            .kv("alias", "alias")
            .map());
        want.object(bean).eqReflect(new TestBean()
            .setUserName("myName")
            .setPostCode("310000"));
    }

    @Data
    @Accessors(chain = true)
    static class TestBean {
        private String userName;

        private String postCode;
    }
}