package org.fffd.l23o6.pojo.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "修改用户信息请求")
public class EditUserInfoRequest {

    @Schema(description = "姓名", required = true)
    @NotNull
    @Size(min = 2, max = 16, message = "姓名长度必须在 2-16 之间")
    @Pattern.List({
            @Pattern(regexp = "^[\\u4E00-\\u9FA5]{2,16}$", message = "姓名只能包含中文"),
    })
    private String name;

    @Schema(description = "证件号", required = true)
    @NotNull
    @Size(min = 18, max = 18, message = "证件号长度必须为18")
    @Pattern.List({
            @Pattern(regexp = "^\\d{17}[0-9X]$", message = "证件号格式错误"),
    })
    private String idn;

    @Schema(description = "手机号", required = true)
    @NotNull
    @Size(min = 11, max = 11, message = "手机号长度必须为11")
    @Pattern.List({
            @Pattern(regexp = "^\\d{11}$", message = "手机号格式错误"),
    })
    private String phone;

    @Schema(description = "证件类型", required = true)
    @NotNull
    @Pattern.List({
            @Pattern(regexp = "^身份证|护照|其他$", message = "证件类型错误"),
    })
    private String type;
}
