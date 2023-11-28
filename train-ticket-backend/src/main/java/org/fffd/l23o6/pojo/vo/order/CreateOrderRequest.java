package org.fffd.l23o6.pojo.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "创建订单请求")
public class CreateOrderRequest{

    @Data
    @Schema(description = "乘车人信息")
    public class Passenger{
        @Schema(description = "姓名", required = true)
        @NotNull
        @Size(min = 2, max = 16, message = "姓名长度必须在 2-16 之间")
        @Pattern(regexp = "^[\\u4E00-\\u9FA5]{2,16}$", message = "姓名只能包含中文")
        private String name;

        @Schema(description = "证件号", required = true)
        @NotNull
        @Size(min = 18, max = 18, message = "证件号长度必须为18")
        @Pattern(regexp = "^\\d{17}[0-9X]$", message = "证件号格式错误")
        private String idn;

        @Schema(description = "手机号", required = true)
        @NotNull
        @Size(min = 11, max = 11, message = "手机号长度必须为11")
        @Pattern(regexp = "^\\d{11}$", message = "手机号格式错误")
        private String phone;

        @Schema(description = "证件类型", required = true)
        @NotNull
        @Pattern(regexp = "^身份证|护照|其他$", message = "证件类型错误")
        private String type;
    }

    @Schema(description = "车次id", required = true)
    @NotNull
    private Long trainId;

    @Schema(description = "出发站id", required = true)
    @NotNull
    private Long startStationId;

    @Schema(description = "目的站id", required = true)
    @NotNull
    private Long endStationId;

    @Schema(description = "座位类型", required = true)
    @NotNull
    @Pattern(regexp = "^(([软硬]卧)|([软硬无]座)|([一二]等座)|(商务座))$", message = "座位类型错误")
    private String seatType;
}
