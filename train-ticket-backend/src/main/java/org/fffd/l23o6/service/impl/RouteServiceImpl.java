package org.fffd.l23o6.service.impl;

import java.util.List;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.mapper.RouteMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.RouteService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final RouteDao routeDao;
    @Override
    public void addRoute(String name, List<Long> stationIds) {
        // TODO: 2023/5/26
        routeDao.save(RouteEntity.builder().name(name).stationIds(stationIds).build());
    }

    @Override
    public List<RouteVO> listRoutes() {
        // TODO: 2023/5/26
        return RouteMapper.INSTANCE.toRouteVOs(routeDao.findAll());
    }

    @Override
    public RouteVO getRoute(Long id) {
        // TODO: 2023/5/26
        return RouteMapper.INSTANCE.toRouteVO(routeDao.findById(id).get());
    }

    @Override
    public void editRoute(Long id, String name, List<Long> stationIds) {
        // TODO: 2023/5/26
        RouteEntity routeEntity = routeDao.findById(id).get();
        routeDao.save(routeEntity.setName(name).setStationIds(stationIds));
    }
}

