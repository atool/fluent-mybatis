package cn.org.atool.fluent.mybatis.demo.generate.dao.intf;

import cn.org.atool.fluent.mybatis.base.IDao;
import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import org.springframework.stereotype.Repository;

/**
 * @ClassName AddressDao
 * @Description AddressEntity数据操作接口
 *
 * @author generate code
 */
@Repository
public interface AddressDao extends IDao<AddressEntity>  {
}