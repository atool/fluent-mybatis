package cn.org.atool.fluent.mybatis.test.origin;

import cn.org.atool.fluent.mybatis.origin.entity.PersonEntity;
import cn.org.atool.fluent.mybatis.origin.mapper.PersonMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.tools.datagen.DataMap;

public class OneToOneTest extends BaseTest {
    @Autowired
    private PersonMapper personMapper;

    @BeforeEach
    public void setup() {
        db.table("idcard").clean().insert(new DataMap(2)
            .arr("id", 21, 22)
            .arr("version", 0)
            .arr("code", "code1", "code2"));
        db.table("person").clean().insert(new DataMap(2)
            .arr("id", 1, 2)
            .arr("name", "name1", "name2")
            .arr("age", 2, 3)
            .arr("idcard_id", 21, 22)
        );
        db.table("nick_name").clean().insert(new DataMap(4)
            .arr("id", 11, 12, 13, 14)
            .arr("nick_name", "nick1", "nick2", "nick3", "nick4")
            .arr("person_id", 1, 1, 2, 2)
        );
    }

    @Test
    public void one2one() {
        PersonEntity p1 = personMapper.selectPersonById(1);
        System.out.println("=============================");

        System.out.println(p1.getAge());
        System.out.println("================age=============");
        System.out.println(p1.getCard());
        System.out.println("=================card============");
        System.out.println(p1.getNickNames());
        System.out.println("===================nick==========");
    }
}
