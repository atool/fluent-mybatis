package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.demo.generate.mapper.UserMapper;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

class WhereApplyTest_In extends BaseTest {

    @Autowired
    private UserMapper mapper;

    @Test
    void in() {
        mapper.listEntity(new UserQuery()
            .where
            .age().in(new int[]{23, 34})
            .age().in(new int[]{23})
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IN (?, ?) AND age = ?");
    }

    @Test
    void testIn() {
        mapper.listEntity(new UserQuery()
            .where
            .age().in(true, new int[]{23, 34})
            .age().in(true, new long[]{23, 34})
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IN (?, ?) AND age IN (?, ?)");
    }

    @Test
    void testIn_collection() {
        mapper.listEntity(new UserQuery()
            .where
            .age().in(true, Arrays.asList(12, 23))
            .age().in(true, Arrays.asList(12))
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IN (?, ?) AND age = ?");
    }

    @Test
    void testIn_IfNotEmpty() {
        mapper.listEntity(new UserQuery()
            .where
            .age().in_IfNotEmpty(Arrays.asList(12, 23))
            .age().in_IfNotEmpty(new long[]{23})
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age IN (?, ?) AND age = ?");
    }

    @Test
    void notIn() {
        mapper.listEntity(new UserQuery()
            .where
            .age().notIn(new int[]{23, 34})
            .age().notIn_IfNotEmpty(new long[]{23})
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age NOT IN (?, ?) AND age NOT IN (?)");
    }


    @Test
    void notIn_IfNotEmpty() {
        mapper.listEntity(new UserQuery()
            .where
            .age().notIn(Arrays.asList(1, 2))
            .age().notIn_IfNotEmpty(Arrays.asList(1))
            .end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE age NOT IN (?, ?) AND age NOT IN (?)");
    }
}