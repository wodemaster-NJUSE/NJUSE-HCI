package org.fffd.l23o6.pojo.vo.train;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketInfo {
    private String type;
    private Integer count;
    private Integer price;
}