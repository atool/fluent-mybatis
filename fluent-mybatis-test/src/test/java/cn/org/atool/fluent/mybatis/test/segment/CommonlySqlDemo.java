package cn.org.atool.fluent.mybatis.test.segment;

import cn.org.atool.fluent.mybatis.generator.ATM;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentScoreQuery;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.test4j.Logger;
import org.test4j.tools.datagen.DataMap;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("all")
public class CommonlySqlDemo extends BaseTest {
    @DisplayName("计算昨天的日期")
    @Test
    void test_yesterday() {
        ATM.dataMap.student.initTable(1).cleanAndInsert();
        // oracle: TO_CHAR(DATEADD(TO_DATE('${bizdate}', 'yyyymmdd'), - 1,'dd'),'yyyymmdd')
        // mysql
        List list = StudentQuery.emptyQuery()
            .select.applyAs("date_format(DATE_ADD(str_to_date('2020-01-29','%Y-%m-%d'), INTERVAL -1 DAY),'%Y-%m-%d')", "yesterday").end()
            .limit(1).to().listObjs();
        want.object(list.get(0)).eq("2020-01-28");
    }

    @DisplayName("行转列")
    @Test
    void test_row2col() {
        ATM.dataMap.studentScore.initTable(2)
            .studentId.values(1)
            .subject.values("语文", "数学")
            .score.values(89, 90)
            .cleanAndInsert();
        List list = StudentScoreQuery.emptyQuery()
            .select.apply(row2col("subject", "score", "语文", "数学")).end()
            .groupBy.studentId().end()
            .to().listMaps();
        Logger.info(list);
        want.object(list).eqDataMap(DataMap.create(1).kv("语文", 89L).kv("数学", 90L));
    }

    private String row2col(String row, String col, String... cases) {
        return Stream.of(cases)
            .map(c -> String.format("MAX(CASE WHEN %s='%s' THEN %s END) AS %s", row, c, col, c))
            .collect(Collectors.joining(",\n"));
    }

    @DisplayName("统计重复条数")
    @Test
    void test_deprecate() {
        StudentScoreQuery.emptyQuery().select.studentId().count("_count").end()
            .groupBy.studentId().end()
            .having.apply("_count").gt(1).end()
            .to().listMaps();
    }
}
