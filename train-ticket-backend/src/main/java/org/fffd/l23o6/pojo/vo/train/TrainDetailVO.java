package org.fffd.l23o6.pojo.vo.train;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TrainDetailVO {
    private Long id;
    private String name;
    private String trainType;
    private List<Long> stationIds;
    private String date;
    private List<Date> departureTimes;
    private List<Date> arrivalTimes;
    private List<String> extraInfos;
}
