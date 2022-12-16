package cn.org.atool.fluent.mybatis.base.crud;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.provider.SqlKit;
import cn.org.atool.fluent.mybatis.base.provider.SqlProvider;
import cn.org.atool.fluent.mybatis.segment.WhereBase;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.model.HintType;
import cn.org.atool.fluent.mybatis.segment.model.IWrapperData;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.common.kits.StrKey;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.EMPTY;

/**
 * IWrapper: 查询和更新都用到的接口
 *
 * @param <E>  对应的实体类
 * @param <W>  最终查询器或更新器
 * @param <NQ> 对应的嵌套查询器
 * @author darui.wu
 */
@SuppressWarnings({"unchecked"})
public interface IWrapper<
    E extends IEntity,
    W extends IWrapper<E, W, NQ>,
    NQ extends IBaseQuery<E, NQ>>
    extends Serializable {

    /**
     * 返回where
     *
     * @return WhereBase
     */
    WhereBase<?, W, NQ> where();

    /**
     * 返回查询器或更新器对应的xml数据
     * 系统方法, 请勿调用
     *
     * @return WrapperData
     */
    WrapperData data();

    /**
     * 在select或update指定位置插入hint语句
     *
     * @param type 指定位置
     * @param hint hint语句
     * @return W
     */
    default W hint(HintType type, String hint) {
        this.data().hint(type, hint);
        return (W) this;
    }

    /**
     * 在select或update开头插入hint语句
     *
     * @param hint hint语句
     * @return W
     */
    default W hint(String hint) {
        return this.hint(HintType.Before_All, hint);
    }

    /**
     * query/update表别名
     *
     * @return 表别名
     */
    default String getTableAlias() {
        return EMPTY;
    }

    /**
     * query/update表名
     *
     * @param notFoundError 未找到表名时, 是否抛出异常
     * @return ISqlSegment
     */
    IFragment table(boolean notFoundError);

    /**
     * 数据库映射定义
     *
     * @return Optional<IMapping>
     */
    Optional<IMapping> mapping();

    List<String> allFields();

    @SuppressWarnings("rawtypes")
    default StrKey<IWrapperData> sqlData() {
        SqlKit kit = SqlProvider.sqlKit((AMapping) this.mapping().orElseThrow(() -> new RuntimeException("")));
        IWrapperData data = this.data();

        return new StrKey<>("", data);
    }
}