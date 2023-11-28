package org.fffd.l23o6.controller;

import io.github.lyc8503.spring.starter.incantation.pojo.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.pojo.vo.route.AddRouteRequest;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.impl.RouteServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/v1/")
@RequiredArgsConstructor
public class RouteController {
    RouteServiceImpl routeServiceImpl;
    @PostMapping("admin/route")
    public CommonResponse<?> addRoute(@Valid @RequestBody AddRouteRequest request) {
        // There should be something, and so do the following methods.
        // TODO: 2023/5/26

        return CommonResponse.success();
    }

    @GetMapping("admin/route")
    public CommonResponse<List<RouteVO>> getRoutes() {
        return CommonResponse.success();
    }

    @GetMapping("admin/route/{routeId}")
    public CommonResponse<RouteVO> getRoute(@PathVariable("routeId") Long routeId) {
        return CommonResponse.success();
    }

    @PutMapping("admin/route/{routeId}")
    public CommonResponse<?> editRoute(@PathVariable("routeId") Long routeId, @Valid @RequestBody AddRouteRequest request) {
        return CommonResponse.success();
    }
}