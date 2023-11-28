package org.fffd.l23o6.pojo.vo.route;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "添加路线请求")
public class AddRouteRequest {

    @Schema(description = "路线名", required = true)
    @NotNull
    @Size(min = 1, max = 40, message = "路线名长度必须在 1-40 之间")
    private String name;

    @Schema(description = "站点列表", required = true)
    @NotNull
    @Size(min = 2, message = "路线至少包含2站")
    private List<Long> stationIds;
}
