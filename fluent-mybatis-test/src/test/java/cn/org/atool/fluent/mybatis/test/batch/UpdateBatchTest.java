package cn.org.atool.fluent.mybatis.test.batch;

import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.customize.mapper.StudentBatchMapper;
import cn.org.atool.fluent.mybatis.generate.ATM;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generate.mapper.StudentMapper;
import cn.org.atool.fluent.mybatis.generate.wrapper.HomeAddressUpdate;
import cn.org.atool.fluent.mybatis.generate.wrapper.StudentUpdate;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

import java.util.Arrays;
import java.util.List;

public class UpdateBatchTest extends BaseTest {
    @Autowired
    private StudentMapper mapper;

    @Autowired
    private StudentBatchMapper batchMapper;

    @BeforeEach
    void setup() {
        ATM.dataMap.student.initTable(2)
            .id.values(23L, 24L)
            .userName.values("user")
            .cleanAndInsert();
        ATM.dataMap.homeAddress.initTable(2)
            .id.values(23, 24)
            .address.values("address")
            .cleanAndInsert();
    }

    @Test
    public void testBatchJavaEach() {
        List<IUpdate> updates = this.newListUpdater();
        for (IUpdate update : updates) {
            mapper.updateBy(update);
        }
        db.sqlList().wantFirstSql().eq("" +
            "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?", StringMode.SameAsSpace);
        db.sqlList().wantSql(1).eq("" +
            "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user name24")
        );
    }

    @Test
    public void updateStudentBatch() {
        List<StudentEntity> students = Arrays.asList(
            new StudentEntity().setId(23L).setUserName("user name23"),
            new StudentEntity().setId(24L).setUserName("user name24"));
        batchMapper.updateStudentBatch(students);
        /** 验证SQL参数 **/
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user name24")
        );
    }

    @DisplayName("批量更新同一张表")
    @Test
    public void testUpdateBatch_same() {
        IUpdate[] updates = this.newListUpdater().toArray(new IUpdate[0]);
        int count = mapper.updateBy(updates);
        /** 验证SQL语句 **/
        db.sqlList().wantFirstSql().eq("" +
                "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?; " +
                "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?"
            , StringMode.SameAsSpace);
        /** 验证SQL参数 **/
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user name24")
        );
        /** 返回的update值是最后一条sql **/
        want.number(count).isEqualTo(1);
    }

    @DisplayName("批量更新不同表")
    @Test
    public void testUpdateBatch_different() {
        StudentUpdate update1 = new StudentUpdate()
            .update.userName().is("user name23").end()
            .where.id().eq(23L).end();
        HomeAddressUpdate update2 = new HomeAddressUpdate()
            .update.address().is("address 24").end()
            .where.id().eq(24L).end();
        int count = mapper.updateBy(update1, update2);
        db.sqlList().wantFirstSql()
            .eq("" +
                "UPDATE student SET gmt_modified = now(), user_name = ? WHERE id = ?; " +
                "UPDATE home_address SET gmt_modified = now(), address = ? WHERE id = ?", StringMode.SameAsSpace);
        db.table(ATM.table.student).query().eqDataMap(ATM.dataMap.student.table(2)
            .id.values(23L, 24L)
            .userName.values("user name23", "user")
        );
        db.table(ATM.table.homeAddress).query().eqDataMap(ATM.dataMap.homeAddress.table(2)
            .id.values(23, 24)
            .address.values("address", "address 24")
        );
        /** 返回的update值是最后一条sql **/
        want.number(count).isEqualTo(1);
    }

    /**
     * 构造多个更新操作
     */
    private List<IUpdate> newListUpdater() {
        StudentUpdate update1 = new StudentUpdate()
            .update.userName().is("user name23").end()
            .where.id().eq(23L).end();
        StudentUpdate update2 = new StudentUpdate()
            .update.userName().is("user name24").end()
            .where.id().eq(24L).end();
        return Arrays.asList(update1, update2);
    }
}
