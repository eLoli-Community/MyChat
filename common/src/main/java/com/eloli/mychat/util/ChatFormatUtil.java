package com.eloli.mychat.util;

import com.eloli.mychat.MyChatCore;
import com.eloli.mychat.RhinoScheduler;
import net.kyori.adventure.text.Component;
import org.apsarasmc.apsaras.event.message.ChatEvent;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ChatFormatUtil {
    @Inject
    private MyChatCore core;
    @Inject
    private RhinoScheduler scheduler;

    public ChatEvent.ChatFormatter box(Function function) {
        return (event, receiver) ->
                (Component)
                        ((NativeJavaObject) function.call(
                                core.context(),
                                core.scope(),
                                null,
                                new Object[]{event, receiver}))
                .unwrap();
    }
}
