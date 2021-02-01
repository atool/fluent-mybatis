package cn.org.atool.fluent.mybatis.generate.dao.intf;

import cn.org.atool.fluent.mybatis.base.IBaseDao;
import cn.org.atool.fluent.mybatis.generate.entity.StudentEntity;

import java.util.List;

/**
 * StudentDao: 数据操作接口
 *
 * @author Powered By Fluent Mybatis
 */
public interface StudentDao extends IBaseDao<StudentEntity> {
    /**
     * 测试 dao类不会被重写
     *
     * @return
     */
    int noOverWrite();

    /**
     * 根据姓名模糊查询学生列表
     *
     * @param name
     * @return
     */
    List<StudentEntity> findStudentsByName(String name);

    void updateAddressAndAgeById(StudentEntity... entities);
}
