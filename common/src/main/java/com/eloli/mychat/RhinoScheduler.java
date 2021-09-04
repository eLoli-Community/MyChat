package com.eloli.mychat;

import org.apsarasmc.apsaras.plugin.PluginContainer;
import org.apsarasmc.apsaras.tasker.Task;
import org.apsarasmc.apsaras.tasker.Tasker;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Singleton
public class RhinoScheduler {
    private final PluginContainer plugin;
    private final Tasker handle;
    @Inject
    public RhinoScheduler(final PluginContainer plugin){
        this.plugin = plugin;
        handle = Tasker.factory().of(plugin,1,"nashorn-thread");
    }

    public Task<Void> runLater(Runnable command, int delay, TimeUnit timeUnit) {
        return handle.runLater(plugin, command, delay, timeUnit);
    }

    public <T> Task<T> run(Callable<T> command) {
        return handle.run(plugin, command);
    }

    public Task<Void> run(Runnable command) {
        return handle.run(plugin, command);
    }

    public <T> Task<T> runLater( Callable<T> command, int delay) {
        return handle.runLater(plugin, command, delay);
    }

    public Task<Void> runLater( Runnable command, int delay) {
        return handle.runLater(plugin, command, delay);
    }

    public <T> Task<T> runLater( Callable<T> callable, int i, TimeUnit timeUnit) {
        return handle.runLater(plugin,callable,i,timeUnit);
    }
}
