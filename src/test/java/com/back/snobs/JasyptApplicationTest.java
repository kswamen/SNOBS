package com.back.snobs;

import com.back.snobs.dto.chatroom.ChatRoom;
import com.back.snobs.dto.chatroom.ChatRoomRepository;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageDto;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRdb;
import com.back.snobs.dto.chatroom.chatmessage.ChatMessageRepositoryRdb;
import com.back.snobs.dto.snob.SnobRepository;
import com.back.snobs.service.ChatMessageService;
import com.back.snobs.service.ChatMessageServiceRdb;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
class JasyptApplicationTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void jasypt() {
        String jwtSignature = "04ca023b39512e46d0c2cf4b48d5aac61d34302994c87ed4eff225dcf3b0a218739f3897051a057f9b846a69ea2927a587044164b7bae5e1306219d50b588cb1";
        System.out.println(jasyptEncoding(jwtSignature));
    }

    public String jasyptEncoding(String value) {
        String key = "some_jasypt_key";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }
}
