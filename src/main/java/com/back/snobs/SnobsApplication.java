package com.back.snobs;

import com.back.snobs.config.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

// 배포 시 삭제(exclude 부분)
@SpringBootApplication(
        exclude = {
                org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
                org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
                org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
        }
)
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties(AuthProperties.class)
public class SnobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnobsApplication.class, args);
    }

}
