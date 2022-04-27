package cn.org.atool.fluent.mybatis.test.printsql;

import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.generator.shared3.entity.MemberEntity;
import cn.org.atool.fluent.mybatis.generator.shared3.wrapper.MemberQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.test4j.junit5.Test4J;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PrintSqlTest extends Test4J {

    @BeforeAll
    static void setup() {
        Ref.Query.student.setTableSupplier((t, v) -> "fluent_mybatis." + t);
    }

    @Test
    void insert() {
        List<String> sql = StudentMapper.print(0, m -> m.insert(new StudentEntity()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, now(), now(), 0)");

        sql = StudentMapper.print(1, m -> m.insert(new StudentEntity()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "('test_env', 234567, now(), now(), 0)");

        sql = StudentMapper.print(2, m -> m.insert(new StudentEntity()));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(#{ew.env}, #{ew.tenant}, now(), now(), 0)");
    }

    @Test
    void insertWithPk() {
        List<String> sql = StudentMapper.print(0, m -> m.insertWithPk(new StudentEntity().setId(1L)));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`id`, `env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, ?, now(), now(), 0)");
    }

    @Test
    void insertBatch() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity(), new StudentEntity());
        List<String> sql = StudentMapper.print(0, m -> m.insertBatch(list));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, now(), now(), 0), " +
            "(?, ?, now(), now(), 0)");
    }

    @Test
    void insertBatchWithPk() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity().setId(1L), new StudentEntity().setId(2L));
        List<String> sql = StudentMapper.print(0, m -> m.insertBatchWithPk(list));
        want.list(sql).eqList("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`id`, `env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(?, ?, ?, now(), now(), 0), " +
            "(?, ?, ?, now(), now(), 0)");
    }

    @Test
    void listEntity() {
        List<String> sql = StudentMapper.print(0, m -> m.listEntity(new StudentQuery()));
        want.list(sql).eqList("" +
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?");
    }

    @Test
    void insertSelect() {
        List<String> sql = StudentMapper.print(0, m -> m.insertSelect(new String[]{"id", "address", "bonus_points"}, new StudentQuery()));
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
        List<String> sql = StudentMapper.print(0, m -> m.updateBy(update, update));
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
        List<String> sql = StudentMapper.print(0, m -> m.listObjs(new StudentQuery()));
        want.list(sql).eqList("" +
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ?");
    }

    @Test
    void listMaps() {
        List<String> sql = StudentMapper.print(0, m -> m.listMaps(new StudentQuery().limit(10)));
        want.list(sql).eqList("" +
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = ? " +
            "AND `env` = ? " +
            "LIMIT ?, ?");
    }

    @Test
    void count() {
        List<String> sql = StudentMapper.print(1, m -> m.count(new StudentQuery().limit(10)));
        want.list(sql).eqList("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = false " +
            "AND `env` = 'test_env' " +
            "LIMIT 0, 10");
    }

    @Test
    void stdPagedEntity() {
        List<String> sql = StudentMapper.print(0, m -> m.stdPagedEntity(new StudentQuery()));
        want.list(sql).eqList(
            "SELECT COUNT(*) " +
                "FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? " +
                "AND `env` = ?",
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
                "FROM fluent_mybatis.student " +
                "WHERE `is_deleted` = ? " +
                "AND `env` = ?");
    }

    @Test
    void entitySave() {
        List<String> sql = StudentMapper.print(0, m -> new StudentEntity().save());
        want.list(sql).eqList("" +
            "INSERT INTO " +
            "fluent_mybatis.student (`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES (?, ?, now(), now(), 0)");
    }

    @Test
    void queryToLogicalDelete() {
        List list = StudentMapper.print(0, m -> new MemberQuery()
            .where.age().in(new int[]{1, 2, 4}).end().to().logicDelete());
        want.list(list).eqList("" +
            "UPDATE `t_member` SET `gmt_modified` = now(), `is_deleted` = ? WHERE `age` IN (?, ?, ?)");
    }

    @Test
    void insertSelect2() {
        List list = StudentMapper.print(0, m -> new MemberQuery()
            .select.exclude(MemberEntity::getId)
            .where.age().in(new int[]{1, 2, 4}).end().to().insertSelect());
        want.list(list).eqList("" +
            "INSERT INTO `t_member` " +
            "(`age`, `is_girl`, `school`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "SELECT `age`, `is_girl`, `school`, `user_name`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM `t_member` " +
            "WHERE `age` IN (?, ?, ?)");
    }
}
