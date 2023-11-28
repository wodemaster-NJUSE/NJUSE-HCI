package org.fffd.l23o6.pojo.vo.train;

import java.util.Date;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.fffd.l23o6.pojo.enum_.TrainType;

@Data
@Schema(description = "添加车次请求")
public class AddTrainRequest {

    @Schema(description = "车次名", required = true)
    @NotNull
    @Size(min = 1, max = 10, message = "车次名长度必须在 1-5 之间")
    private String name;

    @Schema(description = "路线id", required = true)
    @NotNull
    private Long routeId;

    @Schema(description = "车类型", required = true)
//    @Pattern(regexp = "^(高铁|普通列车)$", message = "车类型目前只能为高铁或普通列车")
    @NotNull
    private TrainType trainType;

    @Schema(description = "车次日期,YYYY-MM-DD", required = true)
    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式错误")
    private String date;

    @Schema(description = "到点", required = true)
    @NotNull
    private List<Date> arrivalTimes;
    
    @Schema(description = "开点", required = true)
    @NotNull
    private List<Date> departureTimes;

}
