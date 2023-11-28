package org.fffd.l23o6.service;

import org.fffd.l23o6.pojo.vo.route.RouteVO;

import java.util.List;

public interface RouteService {
    void addRoute(String name, List<Long> stationIds);
    List<RouteVO> listRoutes();
    RouteVO getRoute(Long id);
    void editRoute(Long id, String name, List<Long> stationIds);

}
