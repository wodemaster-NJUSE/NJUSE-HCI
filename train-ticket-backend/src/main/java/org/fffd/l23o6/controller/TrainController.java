package org.fffd.l23o6.controller;

import java.util.List;
import io.github.lyc8503.spring.starter.incantation.pojo.CommonResponse;

import org.fffd.l23o6.pojo.vo.train.AddTrainRequest;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.ListTrainRequest;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.service.TrainService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/v1/")
@AllArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @GetMapping("train")
    public CommonResponse<List<TrainVO>> listTrains(@Valid ListTrainRequest request) {
        return CommonResponse.success(trainService.listTrains(request.getStartStationId(), request.getEndStationId(), request.getDate()));
    }

    @GetMapping("train/{trainId}")
    public CommonResponse<TrainDetailVO> getTrain(@PathVariable Long trainId) {
        return CommonResponse.success(trainService.getTrain(trainId));
    }

    @PostMapping("admin/train")
    public CommonResponse<?> addTrain(@Valid @RequestBody AddTrainRequest request){
        trainService.addTrain(request.getName(), request.getRouteId(), request.getTrainType(), request.getDate(), request.getArrivalTimes(), request.getDepartureTimes());
        return CommonResponse.success();
    }

    @GetMapping("admin/train")
    public CommonResponse<List<AdminTrainVO>> listTrainsAdmin() {
        return CommonResponse.success(trainService.listTrainsAdmin());
    }

    @GetMapping("admin/train/{trainId}")
    public CommonResponse<AdminTrainVO> getTrainAdmin(@PathVariable Long trainId) {
        return CommonResponse.success();
    }
    
    @PutMapping("admin/train/{trainId}")
    public CommonResponse<?> changeTrain(@PathVariable Long trainId, @Valid @RequestBody AddTrainRequest request) {
        trainService.changeTrain(trainId, request.getName(), request.getRouteId(), request.getTrainType(),
                request.getDate(), request.getArrivalTimes(), request.getDepartureTimes());
        return CommonResponse.success();
    }
    
    @DeleteMapping("admin/train/{trainId}")
    public CommonResponse<?> deleteTrain(@PathVariable Long trainId) {
        trainService.deleteTrain(trainId);
        return CommonResponse.success();
    }
}
