package io.github.adam035.networkdrive.infrastructure.spring.web.mapper;

import io.github.adam035.networkdrive.application.dto.RegisterDeviceCommand;
import io.github.adam035.networkdrive.infrastructure.spring.web.dto.RegisterDeviceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterDeviceMapper {

    @Mapping(target = "deviceId", source = "deviceId")
    @Mapping(
            target = "publicKey",
            expression = "java(java.util.Base64.getUrlDecoder().decode(registerDeviceRequest.publicKeyBase64Url()))"
    )
    RegisterDeviceCommand mapToRegisterDeviceCommand(RegisterDeviceRequest registerDeviceRequest);

}
