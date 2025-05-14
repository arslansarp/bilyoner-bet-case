package com.bilyoner.betting.service;

import com.bilyoner.betting.entity.Event;

import java.util.List;

public interface BulletinBroadcastService {

    void sendUpdatedBulletin(List<Event> events);
}
