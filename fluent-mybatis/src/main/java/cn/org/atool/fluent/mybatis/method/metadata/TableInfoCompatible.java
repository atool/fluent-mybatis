package cn.org.atool.fluent.mybatis.method.metadata;


/**
 * TableInfoCompatible
 *
 * @author darui.wu
 * @create 2020/6/3 7:51 下午
 */
@Deprecated
public abstract class TableInfoCompatible {
    private static TableInfoCompatible instance;

    public static TableInfoCompatible instance() {
        if (instance == null) {
            try {
                Class klass = Class.forName("com.baomidou.mybatisplus.core.TableInfoHelper");
                instance = (TableInfoCompatible) klass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public abstract boolean initTableName(Class<?> clazz, TableMeta tableMeta);

    public abstract void initTableFields(Class<?> clazz, TableMeta tableMeta);
}