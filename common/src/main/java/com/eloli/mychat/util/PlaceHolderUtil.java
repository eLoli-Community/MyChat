package com.eloli.mychat.util;

import org.apsarasmc.apsaras.entity.Player;
import org.apsarasmc.apsaras.util.ResourceKey;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderUtil {
    private final static Pattern ENTRY_PATTERN = Pattern.compile("%[a-zA-Z0-9_]+:[a-zA-Z0-9_]+%");
    private PlaceHolderUtil(){
        //
    }

    public static String apply(Player player, String message){
        StringBuilder builder = new StringBuilder();
        int offset = 0;
        Matcher matcher = ENTRY_PATTERN.matcher(message);
        while (true) {
            if (matcher.find()) {
                builder.append(message, offset, matcher.start());
                builder.append(
                    player.placeholder(ResourceKey.factory().resolve(
                            message.substring(matcher.start()+1, matcher.end()-1)
                    ))
                );
                offset = matcher.end();
            } else {
                builder.append(message.substring(offset));
                return builder.toString();
            }
        }
    }
}
