package io.github.adam035.desktopfs.infrastructure.winfsp.config;

import com.github.jnrwinfspteam.jnrwinfsp.api.MountOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WinFspConfiguration {

    @Bean
    public MountOptions winFspMountOptions() {
        return new MountOptions()
                .setDebug(true)
                .setCase(MountOptions.CaseOption.CASE_SENSITIVE)
                .setSectorSize(512)
                .setSectorsPerAllocationUnit(1)
                .setForceBuiltinAdminOwnerAndGroup(true);
    }

}
