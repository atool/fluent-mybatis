package cn.org.atool.fluent.mybatis.origin.mapper;

import cn.org.atool.fluent.mybatis.origin.entity.PersonEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository("personMapper")
@Mapper
public interface PersonMapper {
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "age", column = "age"),
        @Result(property = "card", column = "idcard_id",
            one = @One(select = "cn.org.atool.fluent.mybatis.origin.mapper.IdCardMapper.selectCodeById")),
        @Result(property = "nickNames", column = "id",
            many = @Many(select = "cn.org.atool.fluent.mybatis.origin.mapper.NickNameMapper.selectNickNamesByPersonId"))
    })
    @Select("select * from person where id=#{id}")
    PersonEntity selectPersonById(Integer id);
}