package cn.org.atool.fluent.mybatis.generate.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.customize.model.ScoreStatistics;
import cn.org.atool.fluent.mybatis.generate.entity.StudentScoreEntity;

import java.util.List;

/**
 * StudentScoreDao: 数据操作接口
 *
 * @author Powered By Fluent Mybatis
 */
public interface StudentScoreDao extends IBaseDao<StudentScoreEntity> {
    /**
     * 统计从fromYear到endYear年间学科subjects的统计数据
     *
     * @param fromSchoolTerm 统计年份区间开始
     * @param endSchoolTerm  统计年份区间结尾
     * @param subjects       统计的学科列表
     * @return 统计数据
     */
    List<ScoreStatistics> statistics(int fromSchoolTerm, int endSchoolTerm, String[] subjects);

    /**
     * 统计从fromYear到endYear年间学科subjects的统计数据
     *
     * @param fromSchoolTerm 统计年份区间开始
     * @param endSchoolTerm  统计年份区间结尾
     * @param subjects       统计的学科列表
     * @return 统计数据
     */
    List<ScoreStatistics> statistics2(int fromSchoolTerm, int endSchoolTerm, String[] subjects);
}
