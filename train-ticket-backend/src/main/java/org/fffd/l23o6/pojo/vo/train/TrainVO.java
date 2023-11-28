package org.fffd.l23o6.pojo.vo.train;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainVO {
    private Long id;
    private String name;
    private String trainType;
    private Long startStationId;
    private Long endStationId;
    private Date departureTime;
    private Date arrivalTime;
    private List<TicketInfo> ticketInfo;
}
