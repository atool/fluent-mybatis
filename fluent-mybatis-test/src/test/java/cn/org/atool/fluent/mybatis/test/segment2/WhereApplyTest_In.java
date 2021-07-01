package cn.org.atool.fluent.mybatis.test.segment2;

import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.If;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

class WhereApplyTest_In extends BaseTest {

    @Autowired
    private StudentMapper mapper;

    @Test
    void in() {
        mapper.listEntity(new StudentQuery()
            .where.age().in(new int[]{23, 34})
            .and.age().in(new int[]{23})
            .or.age().in(new int[]{1}).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IN (?, ?) AND `age` = ? OR `age` = ?");
    }

    @Test
    void testIn() {
        mapper.listEntity(new StudentQuery()
            .where.age().in(new int[]{23, 34}, If::everTrue)
            .and.age().in(new long[]{23, 34}, If::everTrue).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IN (?, ?) AND `age` IN (?, ?)");
    }

    @Test
    void testIn_collection() {
        mapper.listEntity(new StudentQuery()
            .where.age().in(Arrays.asList(12, 23), If::everTrue)
            .and.age().in(Arrays.asList(12), If::everTrue).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IN (?, ?) AND `age` = ?");
    }

    @Test
    void testIn_IfNotEmpty() {
        mapper.listEntity(new StudentQuery()
            .where.age().in(Arrays.asList(12, 23), If::notEmpty)
            .and.age().in(new long[]{23}, If::notEmpty).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` IN (?, ?) AND `age` = ?");
    }

    @Test
    void notIn() {
        mapper.listEntity(new StudentQuery()
            .where.age().notIn(new int[]{23, 34})
            .and.age().notIn(new long[]{23}, If::notEmpty).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` NOT IN (?, ?) AND `age` NOT IN (?)");
    }


    @Test
    void notIn_IfNotEmpty() {
        mapper.listEntity(new StudentQuery()
            .where.age().notIn(Arrays.asList(1, 2))
            .and.age().notIn(Arrays.asList(1), If::notEmpty).end()
        );
        db.sqlList().wantFirstSql()
            .end("WHERE `age` NOT IN (?, ?) AND `age` NOT IN (?)");
    }
}
