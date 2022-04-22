package com.back.snobs.util;

import org.springframework.data.redis.listener.ChannelTopic;

public class RedisUtils {
    public static String getChatRoomKey(Long chatRoomIdx) {
        return "CHATROOM" + chatRoomIdx;
    }

    public static ChannelTopic getChannelTopic(Long chatRoomIdx) {
        return new ChannelTopic("CHANNEL" + chatRoomIdx);
    }
}
