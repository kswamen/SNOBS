package com.back.snobs.util;

public class RedisUtils {
    public static String getChatRoomKey(Long chatRoomIdx) {
        return "CHATROOM" + chatRoomIdx;
    }
}
