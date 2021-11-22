package cn.org.atool.fluent.mybatis.formservice.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class HomeAddress {
    /**
     * 详细住址
     */
    private String address;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String district;
}