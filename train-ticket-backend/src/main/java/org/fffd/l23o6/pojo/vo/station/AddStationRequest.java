package org.fffd.l23o6.pojo.vo.station;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "添加车站请求")
public class AddStationRequest {

    @Schema(description = "车站名", required = true)
    @NotNull
    @Size(min = 1, max = 10, message = "车站名长度必须在 1-10 之间")
    private String name;
    
}
