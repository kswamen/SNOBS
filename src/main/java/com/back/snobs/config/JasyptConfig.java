package com.back.snobs.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

@Configuration
public class JasyptConfig {

    @Bean(name = "jaspytStringEncryptor")
    public StringEncryptor stringEncryptor() {
        // 마찬가지로 이 키도 따로 보존할 방법이 필요하다.
        // 환경변수에 아예 저장하거나,
        // AWS EC2 사용 시 환경변수처럼 쓸 수 있는 방법이 있다고 하니 알아보자.
        String key = "some_jasypt_key";
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(key);
        // 암호화 알고리즘
        config.setAlgorithm("PBEWithMD5AndDES");
        // 해싱 횟수
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
