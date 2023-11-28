package org.fffd.l23o6.pojo.vo;

import java.util.List;

import lombok.Data;

@Data
public class PagedResult<T> {
    private List<T> items;
    private Integer page;
    private Integer total;
}
