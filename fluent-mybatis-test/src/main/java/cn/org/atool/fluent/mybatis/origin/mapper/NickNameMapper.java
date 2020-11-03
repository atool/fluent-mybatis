package cn.org.atool.fluent.mybatis.origin.mapper;

import cn.org.atool.fluent.mybatis.origin.entity.NickNameEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NickNameMapper {
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "nickName", column = "nick_name"),
        @Result(property = "personId", column = "person_id")
    })
    @Select("select * from nick_name where person_id=#{id}")
    List<NickNameEntity> selectNickNamesByPersonId(Long personId);
}