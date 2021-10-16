package cn.org.atool.fluent.mybatis.generator.shared2.dao.intf;

import cn.org.atool.fluent.mybatis.generator.shared2.entity.StudentEntity;
import cn.org.atool.fluent.mybatis.generator.shared2.wrapper.StudentQuery;
import cn.org.atool.fluent.mybatis.base.IBaseDao;

import java.util.List;
import java.util.Optional;

/**
 * StudentDao: 数据操作接口
 * <p>
 * 这只是一个减少手工创建的模板文件
 * 可以任意添加方法和实现, 更改作者和重定义类名
 * <p/>@author Powered By Fluent Mybatis
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

    Optional<StudentEntity> findOne(StudentQuery query);
}
