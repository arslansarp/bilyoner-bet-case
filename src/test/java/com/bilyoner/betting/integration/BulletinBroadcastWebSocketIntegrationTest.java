package com.bilyoner.betting.integration;

import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.service.BulletinBroadcastService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class BulletinBroadcastWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BulletinBroadcastService bulletinBroadcastService;

    private WebSocketStompClient stompClient;

    private final CountDownLatch latch = new CountDownLatch(1);
    private final BlockingQueue<List<Event>> blockingQueue = new LinkedBlockingQueue<>();

    @BeforeEach
    public void setup() {
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void testReceiveBulletinMessage() throws Exception {
        StompSession session = stompClient.connectAsync("ws://localhost:" + port + "/bulletin", new StompSessionHandlerAdapter() {
                })
                .get(1, TimeUnit.SECONDS);

        session.subscribe("/topic/bulletin", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return List.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.offer((List<Event>) payload);
                latch.countDown();
            }
        });

        List<Event> events = List.of(new Event(), new Event());
        bulletinBroadcastService.sendUpdatedBulletin(events);

        boolean messageReceived = latch.await(3, TimeUnit.SECONDS);
        assertThat(messageReceived).isTrue();

        List<Event> received = blockingQueue.poll(2, TimeUnit.SECONDS);
        assertThat(received).isNotNull();
        Assertions.assertNotNull(received);
        Assertions.assertEquals(events.size(), received.size());
    }
}

