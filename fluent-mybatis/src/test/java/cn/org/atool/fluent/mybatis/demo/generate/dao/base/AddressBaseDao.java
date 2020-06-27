package cn.org.atool.fluent.mybatis.demo.generate.dao.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.AddressMapper;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressMapping;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import cn.org.atool.fluent.mybatis.demo.MyCustomerInterface;

/**
* AddressEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class AddressBaseDao extends BaseDaoImpl<AddressEntity>
        implements AddressMapping, MyCustomerInterface<AddressEntity> {

    @Autowired
    protected AddressMapper mapper;

    @Override
    public AddressMapper mapper() {
        return mapper;
    }

    @Override
    public AddressQuery query(){
        return new AddressQuery();
    }

    @Override
    public AddressUpdate updater(){
        return new AddressUpdate();
    }

    @Override
    public String findPkColumn() {
        return AddressMapping.id.column;
    }
}