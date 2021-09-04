package com.eloli.mychat.util;

import org.apsarasmc.apsaras.entity.Player;
import org.apsarasmc.sponge.legacy.entity.SpongePlayer;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.placeholder.PlaceholderContext;
import org.spongepowered.api.text.placeholder.PlaceholderParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class SpongeLegacyPlaceHolderUtil implements PlaceHolderUtil.Handler{
    private final static Pattern ENTRY_PATTERN = Pattern.compile("%[a-zA-Z0-9_]+:[a-zA-Z0-9_]+%");
    @Inject
    private Logger logger;

    @Override
    public String handle(Player player, String message) {
        if(player instanceof SpongePlayer) {
            StringBuilder builder = new StringBuilder();
            int offset = 0;
            Matcher matcher = ENTRY_PATTERN.matcher(message);
            while (true) {
                if (matcher.find()) {
                    builder.append(message, offset, matcher.start());
                    builder.append(replace(
                            ((SpongePlayer) player).handle(),
                            message.substring(matcher.start(), matcher.end())
                    ));
                    offset = matcher.end();
                } else {
                    builder.append(message.substring(offset));
                    return builder.toString();
                }
            }
        }else {
            logger.warn("Player {} isn't an instance of SpigotPlayer, PlaceholderAPI won't be called.", player.name());
            return message;
        }
    }

    private String replace(org.spongepowered.api.entity.living.player.Player player, String context) {
        try {
            String name = context.substring(1,context.length() - 1);;
            return Sponge.getRegistry().getType(PlaceholderParser.class, name)
                    .get().parse(
                            PlaceholderContext.builder().setAssociatedObject(player).build()
                    ).toPlain();
        }catch (Exception e){
            return context;
        }
    }
}
