package cn.org.atool.fluent.mybatis.customize;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.mapper.IMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

/**
 * 自定义的通用扩展接口
 *
 * @param <E>
 */
public interface ICustomizedMapper<E extends IEntity> extends IMapper<E> {
    @Select("select 'test' from blob_value where id=1")
    String customized();

    @Options(statementType = StatementType.CALLABLE)
    @Select("CALL countRecord(#{minId, mode=IN, jdbcType=INTEGER}, #{total, mode=OUT, jdbcType=INTEGER})")
    void procedure(ProcedureDto dto);
}