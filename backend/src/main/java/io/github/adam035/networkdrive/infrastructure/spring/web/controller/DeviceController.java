package io.github.adam035.networkdrive.infrastructure.spring.web.controller;

import io.github.adam035.networkdrive.application.usecase.RegisterDeviceUseCase;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.RegisterDeviceRequest;
import io.github.adam035.networkdrive.infrastructure.spring.web.mapper.RegisterDeviceMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/device")
public class DeviceController {

    private final RegisterDeviceMapper registerDeviceMapper;

    private final RegisterDeviceUseCase registerDeviceUseCase;

    @PostMapping("/register")
    public void register(@RequestBody RegisterDeviceRequest registerDeviceRequest) {
        registerDeviceUseCase.registerDevice(registerDeviceMapper.mapToRegisterDeviceCommand(registerDeviceRequest));
    }

}
