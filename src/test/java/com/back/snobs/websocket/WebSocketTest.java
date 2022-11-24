package com.back.snobs.websocket;

import com.back.snobs.domain.chatroom.chatmessage.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketTest {
    @LocalServerPort
    private Integer port;
    private final String ChatRoomIdx = "123";
    private final ChatMessage chatMessage = ChatMessage.builder()
            .message("asdf")
            .chatRoomIdx(123L)
            .userIdx(1L)
            .build();

    private WebSocketStompClient webSocketStompClient;

    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))
        ));
    }

//    @Test
    void verifyMessageIsReceived() throws Exception {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        session.send("/ws/pub/chat/enter", chatMessage);
        session.subscribe("/ws/sub/chat/room/" + ChatRoomIdx, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.add((String) payload);
            }
        });
        session.send("/ws/pub/chat/message", chatMessage);

        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> assertEquals("Mike", blockingQueue.poll()));
    }


//    @Test
    void verifyChatMessageIsSent() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        webSocketStompClient.setMessageConverter(new StringMessageConverter());
        StompSession session = webSocketStompClient
                .connect(getWsPath(), new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/ws/pub/chat/message/" + ChatRoomIdx, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                latch.countDown();
            }
        });

        await()
                .atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> assertEquals(0, latch.getCount()));
    }


    private String getWsPath() {
        return String.format("ws://localhost:%d/api/stomp/chat", port);
    }
}
