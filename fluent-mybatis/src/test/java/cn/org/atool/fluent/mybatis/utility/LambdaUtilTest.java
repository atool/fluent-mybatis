package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.base.IEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.io.Serializable;

public class LambdaUtilTest extends Test4J {
    @Test
    public void test() {
        DemoEntity entity = new DemoEntity();
        String column = LambdaUtil.resolve(DemoEntity::getUserName);
        want.string(column).eq("userName");
        LambdaUtil.resolve(DemoEntity::getUserName);
        LambdaUtil.resolve(DemoEntity::getUserName);
        LambdaUtil.resolve(DemoEntity::getUserName);
        LambdaUtil.resolve(DemoEntity::getUserName);
        DemoEntity entity2 = new DemoEntity() {
        };
        column = LambdaUtil.resolve(DemoEntity::getAge);
        want.string(column).eq("age");
    }
}

@Data
@Accessors(chain = true)
class DemoEntity implements Serializable, IEntity {
    private int age;

    private String userName;

}