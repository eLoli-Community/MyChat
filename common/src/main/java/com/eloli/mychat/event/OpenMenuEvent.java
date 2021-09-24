package com.eloli.mychat.event;

import org.apsarasmc.apsaras.entity.Player;
import org.apsarasmc.apsaras.event.Event;
import org.apsarasmc.apsaras.event.EventContext;

public class OpenMenuEvent implements Event {
    private final EventContext context = EventContext.builder().build();
    private final Player player;

    public OpenMenuEvent(Player player) {
        this.player = player;
    }

    public Player player() {
        return player;
    }

    @Override
    public EventContext context() {
        return context;
    }
}
