package cn.org.atool.fluent.mybatis.test.printsql;

import cn.org.atool.fluent.common.kits.StrKey;
import cn.org.atool.fluent.mybatis.base.crud.Inserter;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.Ref;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import cn.org.atool.fluent.mybatis.utility.RefKit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"rawtypes"})
public class PrintSqlTest2 extends BaseTest {

    @BeforeAll
    static void setup() {
        Ref.Query.student.setTableSupplier((t, v) -> "fluent_mybatis." + t);
    }

    @Test
    void insertBatch() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity(), new StudentEntity());
        StrKey sql = Inserter.instance().insert(list).sql(IRichMapper::insert);
        want.string(sql.key()).eq("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(#{list[0].env}, #{list[0].tenant}, now(), now(), 0), " +
            "(#{list[1].env}, #{list[1].tenant}, now(), now(), 0)");
        RefKit.mapper(StudentEntity.class).execute(sql.key(), sql.val());
    }

    @Test
    void insert_batch() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity(), new StudentEntity());
        StrKey sql = Inserter.instance().insert(list).sql(IRichMapper::insert);
        want.string(sql.key()).eq("" +
            "INSERT INTO fluent_mybatis.student (`env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(#{list[0].env}, #{list[0].tenant}, now(), now(), 0), " +
            "(#{list[1].env}, #{list[1].tenant}, now(), now(), 0)");
        want.list((List) sql.val()).sizeEq(2);
    }

    @Test
    void insertBatchWithPk() {
        List<StudentEntity> list = Arrays.asList(new StudentEntity().setId(1L), new StudentEntity().setId(2L));
        StrKey sql = Inserter.instance().insert(list).sql(IRichMapper::insert);

        want.string(sql.key()).eq("" +
            "INSERT INTO fluent_mybatis.student " +
            "(`id`, `env`, `tenant`, `gmt_created`, `gmt_modified`, `is_deleted`) " +
            "VALUES " +
            "(#{list[0].id}, #{list[0].env}, #{list[0].tenant}, now(), now(), 0), " +
            "(#{list[1].id}, #{list[1].env}, #{list[1].tenant}, now(), now(), 0)");
        RefKit.mapper(StudentEntity.class).execute(sql.key(), sql.val());
    }

    @Test
    void listEntity() {
        ATM.dataMap.student.cleanTable();
        new StudentEntity().setAge(12).setUserName("test").setEnv("test_env").save();
        StrKey sql = new StudentQuery().sql(IRichMapper::listEntity);
        want.string(sql.key()).eq("" +
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = #{ew.data.parameters.variable_1_1} " +
            "AND `env` = #{ew.data.parameters.variable_1_2}");
        List<StudentEntity> value = RefKit.mapper(StudentEntity.class).query(IRichMapper::listEntity, sql.key(), sql.val());
        want.list(value).sizeEq(1);
    }

    @Test
    void listEntity2() {
        StrKey data = StudentQuery.query().sql(IRichMapper::listEntity);
        want.string(data.key()).eq("" +
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = #{ew.data.parameters.variable_1_1} " +
            "AND `env` = #{ew.data.parameters.variable_1_2}");
        RefKit.mapper(StudentEntity.class).execute(data.key(), data.val());
    }

    @Test
    void updateBy() {
        StudentUpdate update = new StudentUpdate().set.email().is("test@163.com").end();
        StrKey sql = update.sql(IRichMapper::updateBy);
        RefKit.mapper(StudentEntity.class).execute(sql.key(), sql.val());
        want.string(sql.key()).eq("" +
            "UPDATE fluent_mybatis.student " +
            "SET `gmt_modified` = now(), " +
            "`email` = #{ew[0].data.parameters.variable_1_3} " +
            "WHERE `is_deleted` = #{ew[0].data.parameters.variable_1_1} " +
            "AND `env` = #{ew[0].data.parameters.variable_1_2}"
        );
    }

    @Test
    void listObjs() {
        StrKey sql = new StudentQuery().sql(IRichMapper::listObjs);
        want.string(sql.key()).eq("" +
            "SELECT `id`, `address`, `age`, `birthday`, `bonus_points`, `desk_mate_id`, `email`, `env`, `gender`, `grade`, `home_address_id`, `home_county_id`, `phone`, `status`, `tenant`, `user_name`, `version`, `gmt_created`, `gmt_modified`, `is_deleted` " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = #{ew.data.parameters.variable_1_1} " +
            "AND `env` = #{ew.data.parameters.variable_1_2}");
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
        StrKey sql = new StudentQuery().limit(10).sql(IRichMapper::count);
        want.string(sql.key()).eq("" +
            "SELECT COUNT(*) " +
            "FROM fluent_mybatis.student " +
            "WHERE `is_deleted` = #{ew.data.parameters.variable_1_1} " +
            "AND `env` = #{ew.data.parameters.variable_1_2} " +
            "LIMIT #{ew.data.parameters.variable_1_3}, #{ew.data.parameters.variable_1_4}");
    }
}
