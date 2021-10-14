package cn.org.atool.fluent.mybatis.generate.mix;

import cn.org.atool.fluent.mybatis.generate.dm.StudentScoreDataMap;
import org.test4j.hamcrest.matcher.modes.EqMode;
import org.test4j.module.spec.IMix;
import org.test4j.module.spec.annotations.Step;

/**
 * 数据库[student_score]表数据准备和校验通用方法
 *
 * @author Powered By Test4J
 */
public class StudentScoreTableMix implements IMix {
    @Step("清空表[student_score]数据")
    public StudentScoreTableMix cleanStudentScoreTable() {
        db.table("student_score").clean();
        return this;
    }

    @Step("准备表[student_score]数据{1}")
    public StudentScoreTableMix readyStudentScoreTable(StudentScoreDataMap data) {
        db.table("student_score").insert(data);
        return this;
    }

    @Step("验证表[student_score]有全表数据{1}")
    public StudentScoreTableMix checkStudentScoreTable(StudentScoreDataMap data, EqMode... modes) {
        db.table("student_score").query().eqDataMap(data, modes);
        return this;
    }

    @Step("验证表[student_score]有符合条件{1}的数据{2}")
    public StudentScoreTableMix checkStudentScoreTable(String where, StudentScoreDataMap data,
                                                       EqMode... modes) {
        db.table("student_score").queryWhere(where).eqDataMap(data, modes);
        return this;
    }

    @Step("验证表[student_score]有符合条件{1}的数据{2}")
    public StudentScoreTableMix checkStudentScoreTable(StudentScoreDataMap where,
                                                       StudentScoreDataMap data, EqMode... modes) {
        db.table("student_score").queryWhere(where).eqDataMap(data, modes);
        return this;
    }

    @Step("验证表[student_score]有{1}条符合条件{2}的数据")
    public StudentScoreTableMix countStudentScoreTable(int count, StudentScoreDataMap where) {
        db.table("student_score").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[student_score]有{1}条符合条件{2}的数据")
    public StudentScoreTableMix countStudentScoreTable(int count, String where) {
        db.table("student_score").queryWhere(where).sizeEq(count);
        return this;
    }

    @Step("验证表[student_score]有{1}条数据")
    public StudentScoreTableMix countStudentScoreTable(int count) {
        db.table("student_score").query().sizeEq(count);
        return this;
    }
}
