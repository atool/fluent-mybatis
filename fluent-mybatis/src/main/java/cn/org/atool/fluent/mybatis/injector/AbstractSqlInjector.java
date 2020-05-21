package cn.org.atool.fluent.mybatis.injector;

import cn.org.atool.fluent.mybatis.metadata.TableHelper;
import cn.org.atool.fluent.mybatis.method.AbstractMethod;
import cn.org.atool.fluent.mybatis.metadata.TableInfo;
import cn.org.atool.fluent.mybatis.util.GlobalConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.MapperBuilderAssistant;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Set;

/**
 * AbstractSqlInjector
 *
 * @author darui.wu
 * @create 2020/5/18 3:37 下午
 */
@Slf4j
public abstract class AbstractSqlInjector implements ISqlInjector {

    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = extractModelClass(mapperClass);
        if (modelClass != null) {
            String className = mapperClass.toString();
            Set<String> mapperRegistryCache = GlobalConfigUtils.getMapperRegistryCache(builderAssistant.getConfiguration());
            if (!mapperRegistryCache.contains(className)) {
                List<AbstractMethod> methodList = this.getMethodList(mapperClass);
                if (methodList == null || methodList.isEmpty()) {
                    log.debug(mapperClass.toString() + ", No effective injection method was found.");
                } else {
                    TableInfo tableInfo = TableHelper.initTableInfo(builderAssistant, modelClass);
                    // 循环注入自定义方法
                    methodList.forEach(m -> m.inject(builderAssistant, mapperClass, modelClass, tableInfo));
                }
                mapperRegistryCache.add(className);
            }
        }
    }

    /**
     * <p>
     * 获取 注入的方法
     * </p>
     *
     * @param mapperClass 当前mapper
     * @return 注入的方法集合
     * @since 3.1.2 add  mapperClass
     */
    public abstract List<AbstractMethod> getMethodList(Class<?> mapperClass);

    /**
     * 提取泛型模型,多泛型的时候请将泛型T放在第一位
     *
     * @param mapperClass mapper 接口
     * @return mapper 泛型
     */
    protected Class<?> extractModelClass(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        for (Type type : types) {
            if (!(type instanceof ParameterizedType)) {
                continue;
            }
            // 对第一个泛型进行处理
            Type[] array = ((ParameterizedType) type).getActualTypeArguments();
            if (array == null || array.length == 0 || array[0] instanceof TypeVariable || array[0] instanceof WildcardType) {
                return null;
            } else {
                return (Class<?>) array[0];
            }
        }
        return null;
    }
}