package com.eloli.mychat;

import com.eloli.mychat.event.ReloadEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apsarasmc.apsaras.aop.AutoComponent;
import org.apsarasmc.apsaras.command.*;
import org.apsarasmc.apsaras.event.EventHandler;
import org.apsarasmc.apsaras.event.EventManager;
import org.apsarasmc.apsaras.event.lifecycle.LoadPluginEvent;
import org.apsarasmc.apsaras.plugin.PluginContainer;

import javax.inject.Inject;

@AutoComponent
public class ReloadCommand {
    private final String RELOAD_PERMISSION = "mychat.reload";

    @Inject
    private PluginContainer plugin;

    @Inject
    private EventManager eventManager;

    @EventHandler
    public void registerCommand(LoadPluginEvent.RegisterCommand event) {
        event.register(plugin, "mychat",
                Command.builder()
                        .subcommand("reload",
                                Command.builder()
                                        .checker(Checkers.permission(RELOAD_PERMISSION))
                                        .executor(context -> {
                                            context.sendMessage(Component.text("Reloading, wait please."));
                                            eventManager.post(new ReloadEvent(result -> {
                                                if (result == ReloadEvent.Result.SUCCESS) {
                                                    context.sendMessage(Component.text("Reload succeed."));
                                                }else {
                                                    context.sendMessage(Component.text("Reload failed.", NamedTextColor.RED));
                                                }
                                            }));
                                            return CommandResult.success();
                                        })
                                        .build()
                        )
                        .build()
        );
    }
}
