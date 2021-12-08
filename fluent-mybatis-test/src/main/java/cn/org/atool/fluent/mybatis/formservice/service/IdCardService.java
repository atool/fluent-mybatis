package cn.org.atool.fluent.mybatis.formservice.service;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.mybatis.generator.shared5.entity.IdcardEntity;

@SuppressWarnings("UnusedReturnValue")
@FormService(entity = IdcardEntity.class)
public interface IdCardService {
    int updateByIdAndVersion(@Entry(value = "code", type = EntryType.Update) String code,
                             long id, long version);
}