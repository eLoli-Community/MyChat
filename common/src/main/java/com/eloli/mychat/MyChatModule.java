package com.eloli.mychat;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.apsarasmc.apsaras.aop.ApsarasPlugin;
import org.apsarasmc.apsaras.aop.Dependency;
import org.apsarasmc.apsaras.plugin.PluginDepend;

@ApsarasPlugin(
        name = "my-chat",
        version = "1.0-SNAPSHOT",
        dependency = {
                @Dependency(
                        type = PluginDepend.Type.LIBRARY,
                        name = "org.mozilla:rhino:1.7.13"
                )
        }
)
public class MyChatModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(IChatCore.class).to(MyChatCore.class);
    }
}
