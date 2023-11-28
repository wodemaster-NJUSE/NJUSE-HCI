package org.fffd.l23o6.pojo.vo.route;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RouteVO {

    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private List<Integer> stationIds;

}
