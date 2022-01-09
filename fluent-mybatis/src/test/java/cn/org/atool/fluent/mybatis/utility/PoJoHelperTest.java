package cn.org.atool.fluent.mybatis.utility;

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
        TestBean bean = (TestBean) PoJoHelper.toPoJo(TestBean.class, DataMap.create(1)
            .kv("user_name", "myName")
            .kv("post_code", "310000")
            .kv("parentName", "parent")
            .map());
        want.object(bean).eqReflect(new TestBean()
            .setUserName("myName")
            .setPostCode("310000")
            .setParentName("parent")
        );
    }

    @Test
    void toPoJo2() {
        RefKit.register("java.util.List<java.lang.String>", obj -> Arrays.asList(String.valueOf(obj).split(";")));
        TestBean bean = (TestBean) PoJoHelper.toPoJoIgnoreNotFound(TestBean.class, DataMap.create(1)
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
    static class TestBean extends ParentBean {
        private String userName;

        private String postCode;

        private List<String> list;
    }

    @Data
    @Accessors(chain = true)
    static class ParentBean {
        private String parentName;
    }
}