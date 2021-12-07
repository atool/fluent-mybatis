package cn.org.atool.fluent.mybatis.formservice.model;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

import static cn.org.atool.fluent.form.annotation.EntryType.OrderBy;

@Data
@Accessors(chain = true)
public class StudentQuery {
    @Entry
    private String userName;

    @Entry("userName")
    private String userName2;

    @Entry(type = EntryType.StartWith)
    private String address;

    @Size(min = 2, max = 2)
    @Entry(type = EntryType.Between)
    private Integer[] age;

    private Integer gender;

    private StatusEnum status;

    /**
     * 默认正序
     */
    @Entry(type = OrderBy, value = "userName")
    private boolean byUserName = true;
    /**
     * 默认倒序
     */
    @Entry(type = OrderBy, value = "age")
    private boolean byAge;
}