package cn.org.atool.fluent.mybatis.test.method;

import cn.org.atool.fluent.mybatis.customize.ProcedureDto;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared4.mapper.BlobValueMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SuperMapperTest extends BaseTest {
    @Autowired
    private BlobValueMapper mapper;

    @Test
    void test() {
        ATM.dataMap.blobValue.table(1)
            .id.values(1)
            .cleanAndInsert();
        String str = mapper.customized();
        want.string(str).eq("test");
    }

    @Test
    void procedure() {
        ATM.dataMap.blobValue.table(3)
            .id.values(2, 3, 4)
            .cleanAndInsert();
        ProcedureDto dto = new ProcedureDto().setMinId(3);
        mapper.procedure(dto);
        want.number(dto.getTotal()).eq(2);
    }

    @Test
    void callProcedure() {
        // 准备数据
        ATM.dataMap.blobValue.table(3)
            .id.values(2, 3, 4)
            .cleanAndInsert();

        ProcedureDto dto = new ProcedureDto().setMinId(3);
        mapper.callProcedure("countRecord(#{p.minId, mode=IN, jdbcType=INTEGER}, #{p.total, mode=OUT, jdbcType=INTEGER})", dto);
        // 验证数据
        want.number(dto.getTotal()).eq(2);
        // 验证执行的sql语句
        db.sqlList().wantFirstSql().eq("{CALL countRecord(?, ?)}");
        db.sqlList().wantFirstPara().eq(new Object[]{3});
    }
}
