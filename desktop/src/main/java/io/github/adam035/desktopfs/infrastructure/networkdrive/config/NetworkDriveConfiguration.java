package io.github.adam035.desktopfs.infrastructure.networkdrive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class NetworkDriveConfiguration {

    @Value("${network-drive.base-url}")
    private String baseUrl;

    @Bean
    public RestClient networkDriveClient(JsonMapper mapper) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .configureMessageConverters(converters -> {
                    converters.addCustomConverter(new JacksonJsonHttpMessageConverter(mapper));
                    converters.addCustomConverter(new ByteArrayHttpMessageConverter());
                })
                .build();
    }

}
