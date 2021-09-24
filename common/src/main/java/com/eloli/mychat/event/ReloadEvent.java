package com.eloli.mychat.event;

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
