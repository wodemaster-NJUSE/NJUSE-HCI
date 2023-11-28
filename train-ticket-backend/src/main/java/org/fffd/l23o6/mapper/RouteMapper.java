package org.fffd.l23o6.mapper;

import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RouteMapper {
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    RouteVO toRouteVO(RouteEntity routeEntity);

    List<RouteVO> toRouteVOs(List<RouteEntity> all);
}
