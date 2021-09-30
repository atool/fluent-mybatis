package cn.org.atool.fluent.mybatis.refs;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.entity.AMapping;
import cn.org.atool.fluent.mybatis.base.mapper.IRichMapper;
import cn.org.atool.fluent.mybatis.base.model.KeyMap;
import cn.org.atool.fluent.mybatis.functions.RelateFunction;
import cn.org.atool.fluent.mybatis.spring.IMapperFactory;
import cn.org.atool.fluent.mybatis.utility.LambdaUtil;

import java.util.Set;

import static cn.org.atool.fluent.mybatis.mapper.FluentConst.Suffix_Ref;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.DOT;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.Ref_Package;

/**
 * IRef: Entity @RefMethod关联关系, 关联加载基类
 *
 * @author darui.wu
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
public abstract class ARef extends IRef {

    /**
     * 单例变量, 需要被Spring容器初始化时赋值
     */
    private static ARef INSTANCE;

    /**
     * 关联方法实现引用
     */
    protected KeyMap<RelateFunction<IEntity>> relations = new KeyMap<>();

    /**
     * 返回查询关联单例
     * 必须将子类配置到Spring容器中进行bean初始化
     *
     * @return IRefs
     */
    static ARef instance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        synchronized (ARef.class) {
            if (INSTANCE != null) {
                return INSTANCE;
            }
            try {
                Class klass = Class.forName(Ref_Package + DOT + Suffix_Ref);
                INSTANCE = (ARef) klass.getDeclaredConstructor().newInstance();
                return INSTANCE;
            } catch (Exception e) {
                throw new RuntimeException("new Refs error:" + e.getMessage(), e);
            }
        }
    }

    /**
     * 初始化 entity mapper 和 关联方法
     */
    protected abstract void initialize(IMapperFactory factory);

    /**
     * 返回所有的Mapper类
     *
     * @return ClassMap
     */
    protected abstract KeyMap<AMapping> mapperMapping();

    protected abstract AMapping byEntity(String clazz);

    protected abstract AMapping byMapper(String clazz);

    /**
     * 所有Entity Class
     *
     * @return ignore
     */
    protected abstract Set<String> allEntityClass();

    protected abstract IRichMapper mapper(String eClass);

    /**
     * 设置实体类的关联自定义实现
     *
     * @param method 方法引用
     * @param <E>    实体类型
     */
    protected <E extends IEntity> void put(RelateFunction<E> method) {
        String name = LambdaUtil.resolve(method);
        relations.put(name, (RelateFunction) method);
    }
}