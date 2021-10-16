package cn.org.atool.fluent.mybatis.generator.shared2.dao.intf;

import cn.org.atool.fluent.mybatis.customize.model.ScoreStatistics;
import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentScoreEntity;
import cn.org.atool.fluent.mybatis.base.IBaseDao;

import java.util.List;

/**
 * StudentScoreDao: 数据操作接口
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
 */
public interface StudentScoreDao extends IBaseDao<StudentScoreEntity> {
    List<ScoreStatistics> statistics(int fromSchoolTerm, int endSchoolTerm, String[] subjects);

    List<ScoreStatistics> statistics2(int fromSchoolTerm, int endSchoolTerm, String[] subjects);
}
