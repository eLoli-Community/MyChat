package com.eloli.mychat.event;

import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apsarasmc.apsaras.event.Event;
import org.apsarasmc.apsaras.event.EventContext;

import java.util.function.Consumer;

public class ReloadEvent implements Event {
    private final EventContext context = EventContext.builder().build();
    private final Consumer<Result> callback;

    public ReloadEvent(Consumer<Result> callback) {
        this.callback = callback;
    }

    public void result(Result result){
        callback.accept(result);
    }

    @Override
    public EventContext context() {
        return context;
    }
    public enum Result {
        SUCCESS,
        FAILED
    }
}
