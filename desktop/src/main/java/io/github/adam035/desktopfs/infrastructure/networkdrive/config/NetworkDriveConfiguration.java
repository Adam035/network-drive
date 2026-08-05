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
                .defaultHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwic3ViIjoiYjkyNjljOTctNTIyMC00MDIwLThhN2YtMDgxNGI2ZDZkMDhlIiwiaWF0IjoxNzg0MzM2Mzc4LCJleHAiOjE3ODQzMzY2Nzh9.OouhvXd92QbjisMJlwW6TG6bHuq5uXVYJvC-Mg021nI")
                .configureMessageConverters(converters -> {
                    converters.addCustomConverter(new JacksonJsonHttpMessageConverter(mapper));
                    converters.addCustomConverter(new ByteArrayHttpMessageConverter());
                })
                .build();
    }

}
