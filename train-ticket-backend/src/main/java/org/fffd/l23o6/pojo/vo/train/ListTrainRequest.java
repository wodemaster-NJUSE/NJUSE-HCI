package org.fffd.l23o6.pojo.vo.train;

import io.swagger.v3.oas.annotations.media.Schema;
// import jakarta.validation.constraints.Max;
// import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "列出车次请求")
public class ListTrainRequest {
    // @Schema(description = "页码", required = false)
    // @Min(value = 1, message = "页码范围错误")
    // private Integer page = 1;

    // @Schema(description = "每页条数", required = false)
    // @Min(value = 1, message = "每页条数范围错误")
    // @Max(value = 100, message = "每页条数范围错误")
    // private Integer pageSize = 20;

    @Schema(description = "起点站", required = true)
    @NotNull
    private Long startStationId;

    @Schema(description = "终点站", required = true)
    @NotNull
    private Long endStationId;

    @Schema(description = "车次日期", required = true)
    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "日期格式错误")
    @NotNull
    private String date;
}
