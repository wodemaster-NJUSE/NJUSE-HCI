package org.fffd.l23o6.service;

import java.util.List;

import org.fffd.l23o6.pojo.vo.station.StationVO;

public interface StationService {
    public StationVO getStation(Long stationId);
    public List<StationVO> listStations();
    public void addStation(String name);
    public void editStation(Long stationId, String name);
}
