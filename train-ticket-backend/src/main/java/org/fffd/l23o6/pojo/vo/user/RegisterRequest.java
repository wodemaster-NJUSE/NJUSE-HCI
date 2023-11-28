package org.fffd.l23o6.pojo.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "注册请求")
public class RegisterRequest {

    @Schema(description = "用户名", required = true)
    @NotNull
    @Size(min = 4, max = 16, message = "用户名长度必须在 4-16 之间")
    @Pattern(regexp = "^[a-z\\d-_]*$", message = "用户名只能包含小写字母,数字,下划线和连字符")
    private String username;

    @Schema(description = "密码", required = true)
    @NotNull
    @Size(min = 8, max = 56, message = "密码长度必须在 8-56 之间")
    @Pattern.List({
            @Pattern(regexp = "^[\\x21-\\x7e]*$", message = "密码只能包含字母,数字和符号"),
            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "密码未达到复杂性要求:密码必须包含大小写字母和数字")
    })
    private String password;

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
