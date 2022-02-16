package com.back.snobs.redisTest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class RedisChat implements Serializable {
    String message;
    Long roomIdx;
}
