package cn.org.atool.fluent.mybatis.origin.mapper;

import cn.org.atool.fluent.mybatis.origin.entity.IdCardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository("idCardMapper")
@Mapper
public interface IdCardMapper {
    @Select("select * from idcard where id=#{id}")
    @Results({
        @Result(column = "id", property = "id"),
        @Result(column = "code", property = "code")
    })
    IdCardEntity selectCodeById(Integer id);
}