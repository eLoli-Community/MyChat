package com.eloli.mychat.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.apsarasmc.apsaras.Apsaras;
import org.apsarasmc.apsaras.aop.AutoComponent;
import org.apsarasmc.apsaras.entity.Player;
import org.apsarasmc.apsaras.event.EventHandler;
import org.apsarasmc.apsaras.event.lifecycle.ServerLifeEvent;
import org.apsarasmc.apsaras.plugin.PluginContainer;
import org.apsarasmc.spigot.entity.SpigotPlayer;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@AutoComponent
public class PlaceHolderUtil {
    public interface Handler {
        String handle(Player player, String message);
    }
    private Handler handler;

    @Inject
    private PluginContainer plugin;
    @Inject
    private Logger logger;

    public String apply(final Player player, final String message) {
        if(handler == null){
            logger.warn("PlaceHolderUtil applied before init.");
            return message;
        }
        return handler.handle(player, message);
    }

    @EventHandler
    public void onLoad(final ServerLifeEvent.Enable event){
        Apsaras.server().syncTasker().run(plugin, ()->{
            try {
                Class.forName("me.clip.placeholderapi.PlaceholderAPI");
                handler = (player, message) -> {
                    if(player instanceof SpigotPlayer) {
                        return PlaceholderAPI.setPlaceholders(
                                ((SpigotPlayer)player).handle(),
                                message
                        );
                    }else {
                        logger.warn("Player {} isn't an instance of SpigotPlayer, PlaceholderAPI won't be called.", player.name());
                        return message;
                    }
                };
                return;
            } catch (ClassNotFoundException e) {
                //
            }

            try {
                Class.forName("org.spongepowered.api.placeholder.PlaceholderParser");
                handler = Apsaras.injector().getInstance(SpongePlaceHolderUtil.class);
                return;
            }catch (ClassNotFoundException e) {
                //
            }

            try {
                Class.forName("org.spongepowered.api.text.placeholder.PlaceholderParser");
                handler = Apsaras.injector().getInstance(SpongeLegacyPlaceHolderUtil.class);
                return;
            }catch (ClassNotFoundException e) {
                //
            }

            logger.warn("No supported PlaceholderAPI found.");
            handler = (player, message) -> message;
        });
    }
}
