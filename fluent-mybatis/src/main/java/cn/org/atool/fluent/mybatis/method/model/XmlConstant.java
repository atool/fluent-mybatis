package cn.org.atool.fluent.mybatis.method.model;

/**
 * SqlBuilderConst: SqlBuilder中使用的表达式常量
 *
 * @author:darui.wu Created by darui.wu on 2020/6/23.
 */
public interface XmlConstant {
    /**
     * wrapper 类
     */
    String WRAPPER = "ew";
    /**
     * columnMap
     */
    String COLUMN_MAP = "cm";

    /**
     * collection
     */
    String COLLECTION = "coll";
    /**
     * 实体类
     */
    String ENTITY = "et";
    /**
     * 分库
     */
    String SPEC_COMMENT = "SPEC_COMMENT";
    /**
     * 分表
     */
    String SPEC_TABLE = "SPEC_TABLE";
    /**
     * 变量在xml文件中的占位符全路径表达式
     * 例子: #{ew.wrapperData.parameters.variable_1}
     */
    String WRAPPER_PARAM_FORMAT = "#{%s.parameters.%s}";

    String Spec_Comment_Not_Null = "SPEC_COMMENT != null and SPEC_COMMENT != ''";

    String Wrapper_Data = String.format("%s.wrapperData", WRAPPER);

    String Wrapper_Exists = String.format("%s != null", Wrapper_Data);

    String Wrapper_Page_Is_Null = String.format("%s.paged == null", Wrapper_Data);

    String Wrapper_Distinct_True = String.format("%s.distinct", Wrapper_Data);

    String Wrapper_Select_Not_Null = String.format("%s.sqlSelect != null", Wrapper_Data);

    String Wrapper_Select_Var = String.format("${%s.sqlSelect}", Wrapper_Data);

    String Wrapper_UpdateStr_Not_Null = String.format("%s.updateStr != null", Wrapper_Data);

    String Wrapper_UpdateStr_Var = String.format("${%s.updateStr}", Wrapper_Data);

    String Wrapper_Update_Contain_Key = String.format("%s.updates.containsKey('@column') == false", Wrapper_Data);

    String Wrapper_Where_Not_Null = String.format("%s.whereSql != null", Wrapper_Data, Wrapper_Data);

    String Wrapper_Where_Var = String.format("${%s.whereSql}", Wrapper_Data);

    String Wrapper_Where_NoLimit_NotNull = String.format("%s.whereNoLimit != null", Wrapper_Data, Wrapper_Data);

    String Wrapper_Where_NoLimit_Var = String.format("${%s.whereNoLimit}", Wrapper_Data);

    String Wrapper_Paged_Offset = String.format("#{%s.paged.offset}", Wrapper_Data);

    String Wrapper_Paged_Size = String.format("#{%s.paged.limit}", Wrapper_Data);

    String Wrapper_Paged_End_Offset = String.format("#{%s.paged.endOffset}", Wrapper_Data);
}