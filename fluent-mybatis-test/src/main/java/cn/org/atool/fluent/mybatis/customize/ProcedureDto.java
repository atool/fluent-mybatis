package cn.org.atool.fluent.mybatis.customize;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcedureDto {
    int minId;

    int total;
}