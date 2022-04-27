package cn.org.atool.fluent.mybatis.test.method.listpojos;

import cn.org.atool.fluent.mybatis.customize.model.ScoreStatistics;
import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.dao.intf.StudentScoreDao;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.tools.datagen.DataMap;

import java.util.List;

public class ListPoJosTest extends BaseTest {
    @Autowired
    private StudentScoreDao dao;

    @Test
    public void statistics() {
        ATM.dataMap.studentScore.cleanTable();
        ATM.dataMap.studentScore
            .table(10).init()
            .schoolTerm.values(2001)
            .score.functionAutoIncrease(index -> (index % 11) * 10)
            .subject.values("数学", "英语", "语文")
            .env.values("test_env")
            .gender.values(1)
            .cleanAndInsert();
        List<ScoreStatistics> list = dao.statistics(2000, 2019, new String[]{"语文", "数学", "英语"});
        want.list(list).eqDataMap(DataMap.create(3)
                .kv("schoolTerm", 2001)
                .kv("subject", "数学", "英语", "语文")
                .kv("count", 1, 1, 8)
                .kv("minScore", 10, 20, 30)
                .kv("maxScore", 10, 20, 100)
                .kv("avgScore", "10.0000", "20.0000", "65.0000"),
            EqMode.EQ_STRING
        );
    }

    @Test
    public void statistics2() {
        ATM.dataMap.studentScore.cleanTable();
        ATM.dataMap.studentScore
            .table(10).init()
            .schoolTerm.values(2001)
            .score.functionAutoIncrease(index -> (index % 11) * 10)
            .subject.values("数学", "英语", "语文")
            .env.values("test_env")
            .gender.values(1)
            .cleanAndInsert();
        List<ScoreStatistics> list = dao.statistics2(2000, 2019, new String[]{"语文", "数学", "英语"});
        want.list(list).eqDataMap(DataMap.create(3)
                .kv("schoolTerm", 2001)
                .kv("subject", "数学", "英语", "语文")
                .kv("count", 1, 1, 8)
                .kv("minScore", 10, 20, 30)
                .kv("maxScore", 10, 20, 100)
                .kv("avgScore", "10.0000", "20.0000", "65.0000"),
            EqMode.EQ_STRING
        );
    }
}
