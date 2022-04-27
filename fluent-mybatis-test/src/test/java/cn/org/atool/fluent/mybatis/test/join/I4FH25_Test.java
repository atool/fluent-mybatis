package cn.org.atool.fluent.mybatis.test.join;

import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class I4FH25_Test extends BaseTest {
    /**
     * SELECT ... FROM `表1` a_9
     * LEFT JOIN `表2` a_10
     * ON a_9.`xxx` = a_10.`yyy`
     * WHERE a_9.`status` = ?
     * AND (`name` LIKE ? OR `aaa` LIKE ? OR `bbb` LIKE ?)
     * <p>
     * https://gitee.com/fluent-mybatis/fluent-mybatis/issues/I4FH25
     */
    @DisplayName("生成连表查询语句的时候, 如果有嵌套sql, 嵌套的sql里的查询条件指定表别名")
    @Test
    void test() {
        //TODO
    }
}
