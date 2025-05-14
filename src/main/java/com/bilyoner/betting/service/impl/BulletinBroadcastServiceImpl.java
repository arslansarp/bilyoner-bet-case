package com.bilyoner.betting.service.impl;

import com.bilyoner.betting.entity.Event;
import com.bilyoner.betting.service.BulletinBroadcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BulletinBroadcastServiceImpl implements BulletinBroadcastService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendUpdatedBulletin(List<Event> events) {
        messagingTemplate.convertAndSend("/topic/bulletin", events);
    }
}
