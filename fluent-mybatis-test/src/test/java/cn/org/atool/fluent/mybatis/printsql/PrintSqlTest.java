package cn.org.atool.fluent.mybatis.printsql;

import cn.org.atool.fluent.mybatis.generate.entity.MemberEntity;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.MemberQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class PrintSqlTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Test
    void insert() {
        List<String> sql = mapper.print(m -> m.insert(new StudentEntity()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES " +
            "(now(), now(), 0, ?, ?)");

        sql = mapper.print(1, m -> m.insert(new StudentEntity()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES " +
            "(now(), now(), 0, 'test_env', 234567)");

        sql = mapper.print(2, m -> m.insert(new StudentEntity()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES " +
            "(now(), now(), 0, #{ew.env}, #{ew.tenant})");
    }

    @Test
    void insertWithPk() {
        List<String> sql = mapper.print(m -> m.insertWithPk(new StudentEntity().setId(1L)));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES " +
            "(?, now(), now(), 0, ?, ?)");
    }

    @Test
    void insertBatch() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity(), new StudentEntity());
        List<String> sql = mapper.print(m -> m.insertBatch(list));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES " +
            "(now(), now(), 0, ?, ?), " +
            "(now(), now(), 0, ?, ?)");
    }

    @Test
    void insertBatchWithPk() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity().setId(1L), new StudentEntity().setId(2L));
        List<String> sql = mapper.print(m -> m.insertBatchWithPk(list));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`id`, `gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES " +
            "(?, now(), now(), 0, ?, ?), " +
            "(?, now(), now(), 0, ?, ?)");
    }

    @Test
    void listEntity() {
        List<String> sql = mapper.print(m -> m.listEntity(new StudentQuery()));
        want.list(sql).eqList("" +
            "SELECT `id`, `gmt_created`, `gmt_modified`, `is_deleted`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?");
    }

    @Test
    void insertSelect() {
        List<String> sql = mapper.print(m -> m.insertSelect(new String[]{"id", "address", "bonus_points"}, new StudentQuery()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`id`, `address`, `bonus_points`) " +
            "SELECT `id`, `address`, `bonus_points` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?");
    }

    @Test
    void updateBy() {
        StudentUpdate update = new StudentUpdate().set.email().is("test@163.com").end();
        List<String> sql = mapper.print(m -> m.updateBy(update, update));
        want.list(sql).eqList("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`email` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?;\n" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`email` = ? " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?");
    }

    @Test
    void listObjs() {
        List<String> sql = mapper.print(m -> m.listObjs(new StudentQuery()));
        want.list(sql).eqList("" +
            "SELECT `id`, `gmt_created`, `gmt_modified`, `is_deleted`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?");
    }

    @Test
    void listMaps() {
        List<String> sql = mapper.print(m -> m.listMaps(new StudentQuery().limit(10)));
        want.list(sql).eqList("" +
            "SELECT `id`, `gmt_created`, `gmt_modified`, `is_deleted`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "LIMIT ?, ?");
    }

    @Test
    void count() {
        List<String> sql = mapper.print(1, m -> m.count(new StudentQuery().limit(10)));
        want.list(sql).eqList("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = false " +
            "AND `env` = 'test_env' " +
            "LIMIT 0, 10");
    }

    @Test
    void stdPagedEntity() {
        List<String> sql = mapper.print(m -> m.stdPagedEntity(new StudentQuery()));
        want.list(sql).eqList(
            "SELECT COUNT(*) " +
                "FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? " +
                "AND `env` = ?",
            "SELECT `id`, `gmt_created`, `gmt_modified`, `is_deleted`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version` " +
                "FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? " +
                "AND `env` = ?");
    }

    @Test
    void entitySave() {
        List<String> sql = mapper.print(m -> new StudentEntity().save());
        want.list(sql).eqList("" +
            "INSERT INTO " +
            "fluent_mybatis.student (`gmt_created`, `gmt_modified`, `is_deleted`, `env`, `tenant`) " +
            "VALUES (now(), now(), 0, ?, ?)");
    }

    @Test
    void queryToLogicalDelete() {
        List list = mapper.print(m -> new MemberQuery()
            .where.age().in(new int[]{1, 2, 4}).end().to().logicDelete());
        want.list(list).eqList("" +
            "UPDATE `t_member` SET `gmt_modified` = now(), `is_deleted` = ? WHERE `age` IN (?, ?, ?)");
    }

    @Test
    void insertSelect2() {
        List list = mapper.print(m -> new MemberQuery()
            .select.exclude(MemberEntity::getId)
            .where.age().in(new int[]{1, 2, 4}).end().to().insertSelect());
        want.list(list).eqList("" +
            "INSERT INTO `t_member` " +
            "(`gmt_modified`, `is_deleted`, `age`, `gmt_created`, `is_girl`, `school`, `user_name`) " +
            "SELECT `gmt_modified`, `is_deleted`, `age`, `gmt_created`, `is_girl`, `school`, `user_name` " +
            "FROM `t_member` " +
            "WHERE `age` IN (?, ?, ?)");
    }
}