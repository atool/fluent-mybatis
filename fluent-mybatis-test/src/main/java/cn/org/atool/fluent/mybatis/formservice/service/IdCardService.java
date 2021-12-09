package cn.org.atool.fluent.mybatis.formservice.service;

import cn.org.atool.fluent.form.annotation.Entry;
import cn.org.atool.fluent.form.annotation.EntryType;
import cn.org.atool.fluent.form.annotation.FormService;
import cn.org.atool.fluent.form.validator.IValidator;
import cn.org.atool.fluent.mybatis.generator.shared5.entity.IdcardEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
@FormService(entity = IdcardEntity.class)
public interface IdCardService {
    int updateByIdAndVersion(@Entry(value = "code", type = EntryType.Update) String code,
                             long id, long version);

    int validateArg(@Entry(value = "version") @Min(4) int version,
                    VForm form);

    int validateFormList(List<VForm> forms);

    @Data
    @Accessors(chain = true)
    class VForm implements IValidator {
        @NotBlank
        private String code;

        @Max(100)
        private int version;

        @Override
        public void validate() throws IllegalArgumentException {
            if (Objects.equals(code, "NotAllowed")) {
                throw new IllegalArgumentException("NotAllowed");
            }
        }
    }
}