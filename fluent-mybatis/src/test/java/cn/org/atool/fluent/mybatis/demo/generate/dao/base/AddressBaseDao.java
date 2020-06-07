package cn.org.atool.fluent.mybatis.demo.generate.dao.base;

import cn.org.atool.fluent.mybatis.condition.base.BaseDaoImpl;
import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapper.AddressMapper;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressEntityQuery;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressEntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import cn.org.atool.fluent.mybatis.demo.MyCustomerInterface;

/**
* AddressEntity数据库操作服务类
 *
 * @author generate code
*/
public abstract class AddressBaseDao extends BaseDaoImpl<AddressEntity, AddressEntityQuery, AddressEntityUpdate>
        implements AddressMP, MyCustomerInterface<AddressEntity, AddressEntityQuery, AddressEntityUpdate> {

    @Autowired
    protected AddressMapper mapper;

    @Override
    public AddressMapper mapper() {
        return mapper;
    }

    @Override
    public AddressEntityQuery query(){
        return new AddressEntityQuery();
    }

    @Override
    public AddressEntityUpdate update(){
        return new AddressEntityUpdate();
    }

    @Override
    public String findPkColumn() {
        return AddressMP.Column.id;
    }
}