package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;

/**
 * PrimaryInfo: 主键信息
 *
 * @author darui.wu 2020/5/27 6:45 下午
 */
@Getter
public class TablePrimaryMeta extends FieldMeta {
    /**
     * 主键ID是否自增
     */
    @Setter
    private boolean autoIncrease;
    /**
     * 表主键ID Sequence
     */
    private final String seqName;

    public TablePrimaryMeta(Field field, TableId tableId) {
        super(tableId.value(), field);
        this.setJdbcType(tableId.jdbcType());
        this.typeHandler = UnknownTypeHandler.class == tableId.typeHandler() ? null : (Class<? extends TypeHandler<?>>) tableId.typeHandler();

        this.el = el();
        this.autoIncrease = tableId.auto();
        this.seqName = tableId.seqName();
    }
}