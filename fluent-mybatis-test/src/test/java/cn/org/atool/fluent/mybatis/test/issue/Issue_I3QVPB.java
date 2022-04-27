package cn.org.atool.fluent.mybatis.test.issue;


import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.JoinBuilder;
import cn.org.atool.fluent.mybatis.base.free.FreeQuery;
import cn.org.atool.fluent.mybatis.base.model.SqlOp;
import cn.org.atool.fluent.mybatis.generator.shared3.mapper.MemberMapper;
import cn.org.atool.fluent.mybatis.test.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.test4j.hamcrest.matcher.string.StringMode;

public class Issue_I3QVPB extends BaseTest {
    @Autowired
    MemberMapper mapper;

    /**
     * <pre>
     * SELECT * FROM
     *      (SELECT crs.* FROM
     *          (SELECT repository_id, COALESCE(total_sec_issue_count, 0) as total_sec_issue_count
     *           FROM code_repository_statistics
     *           WHERE organization_id = ? AND gmt_create = ? AND organization_id = ?) crs
     *      JOIN
     *      (SELECT identifier
     *       FROM code_repository
     *       WHERE organization_id = ? AND project_report_value = ?) cr
     *      ON crs.repository_id = cr.identifier) ccsr
     * ORDER BY total_sec_issue_count DESC LIMIT ? OFFSET ?
     * </pre>
     */
    @Test
    public void issue() {
        FreeQuery query11 = new FreeQuery("code_repository_statistics")
            .select("repository_id", "COALESCE(total_sec_issue_count, 0) as total_sec_issue_count")
            .where.apply("repository_id", SqlOp.EQ, "1")
            .and.apply("gmt_create", SqlOp.EQ, "2021-05-11 17:12:33.322")
            .and.apply("organization_id", SqlOp.EQ, "1")
            .end();
        FreeQuery query1 = new FreeQuery(query11, "crs")
            .select("csr.*");

        FreeQuery query21 = new FreeQuery("code_repository")
            .select("identifier")
            .where.apply("organization_id", SqlOp.EQ, "1")
            .and.apply("project_report_value", SqlOp.EQ, "2")
            .end();
        FreeQuery query2 = new FreeQuery(query21, "cr");
        IQuery join = JoinBuilder.from(query1)
            .join(query2)
            .onEq("repository_id", "identifier").endJoin()
            .build();
        try {
            mapper.listEntity(new FreeQuery(join, "ccsr")
                .select("*")
                .orderBy.desc("total_sec_issue_count").end()
                .limit(10));
        } catch (Exception ignored) {
        }
        db.sqlList().wantFirstSql().eq("" +
            "SELECT * FROM " +
            "(SELECT csr.* FROM " +
            "(SELECT `repository_id`, COALESCE(total_sec_issue_count, 0) as total_sec_issue_count " +
            "FROM `code_repository_statistics` " +
            "WHERE `repository_id` = ? " +
            "AND `gmt_create` = ? " +
            "AND `organization_id` = ?) crs " +
            "JOIN " +
            "(SELECT `identifier` FROM `code_repository` " +
            "WHERE `organization_id` = ? AND `project_report_value` = ?) cr " +
            "ON crs.`repository_id` = cr.`identifier`) ccsr " +
            "ORDER BY ccsr.`total_sec_issue_count` DESC " +
            "LIMIT ?, ?");
        db.sqlList().wantFirstPara().eq(new Object[]{"1", "2021-05-11 17:12:33.322", "1", "1", "2", 0, 10});
    }

    @Test
    public void issue_1() {
        FreeQuery query1 = new FreeQuery("code_repository_statistics")
            .select("repository_id", "COALESCE(total_sec_issue_count, 0) as total_sec_issue_count")
            .where.apply("repository_id", SqlOp.EQ, "1")
            .and.apply("gmt_create", SqlOp.EQ, "2021-05-11 17:12:33.322")
            .and.apply("organization_id", SqlOp.EQ, "1")
            .end();

        FreeQuery query2 = new FreeQuery("code_repository")
            .select("identifier")
            .where.apply("organization_id", SqlOp.EQ, "1")
            .and.apply("project_report_value", SqlOp.EQ, "2")
            .end();
        IQuery join = JoinBuilder.from(query1, "crs")
            .join(query2, "cr")
            .onEq("repository_id", "identifier").endJoin()
            .select("csr.*")
            .build();
        try {
            mapper.listEntity(new FreeQuery(join, "ccsr")
                .select("*")
                .orderBy.desc("total_sec_issue_count").end()
                .limit(10));
        } catch (Exception ignored) {
        }
        db.sqlList().wantFirstSql().eq("" +
            "SELECT * FROM " +
            "(SELECT csr.* FROM " +
            "(SELECT `repository_id`, COALESCE(total_sec_issue_count, 0) as total_sec_issue_count " +
            "FROM `code_repository_statistics` " +
            "WHERE `repository_id` = ? " +
            "AND `gmt_create` = ? " +
            "AND `organization_id` = ?) crs " +
            "JOIN " +
            "(SELECT `identifier` FROM `code_repository` " +
            "WHERE `organization_id` = ? AND `project_report_value` = ?) cr " +
            "ON crs.`repository_id` = cr.`identifier`) ccsr " +
            "ORDER BY ccsr.`total_sec_issue_count` DESC " +
            "LIMIT ?, ?", StringMode.SameAsSpace);
        db.sqlList().wantFirstPara().eqList("1", "2021-05-11 17:12:33.322", "1", "1", "2", 0, 10);
    }
}
