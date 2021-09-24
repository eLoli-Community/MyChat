package com.eloli.mychat;

import com.eloli.mychat.event.OpenMenuEvent;
import com.eloli.mychat.event.ReloadEvent;
import com.eloli.mychat.menu.OpenMenuHandler;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apsarasmc.apsaras.Apsaras;
import org.apsarasmc.apsaras.aop.AutoComponent;
import org.apsarasmc.apsaras.event.EventHandler;
import org.apsarasmc.apsaras.event.EventManager;
import org.apsarasmc.apsaras.event.Order;
import org.apsarasmc.apsaras.event.lifecycle.ServerLifeEvent;
import org.apsarasmc.apsaras.event.message.ChatEvent;
import org.apsarasmc.apsaras.plugin.PluginContainer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.text.Style;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AutoComponent
@Singleton
public class MyChatCore implements IChatCore{
    @Inject
    private PluginContainer pluginContainer;
    @Inject
    private Logger logger;
    @Inject
    private EventManager eventManager;

    private Context cx = null;
    private Scriptable scope;

    private Map<String, Script> compiledScripts;

    @EventHandler
    public void onLoad(final ServerLifeEvent.Enable e) throws InterruptedException {
        load();
        eventManager.registerListeners(
                pluginContainer,
                Apsaras.injector().getInstance(OpenMenuHandler.class)
        );
    }

    public Context context(){
        return this.cx;
    }

    public Scriptable scope() {
        return scope;
    }

    private ReloadEvent.Result load(){
        compiledScripts = new HashMap<>();
        if(cx != null){
            Context.exit();
        }
        cx = Context.enter();
        scope = cx.initStandardObjects();
        try(InputStream inputStream = MyChatCore.class.getClassLoader().getResourceAsStream("mychat/init.js")) {
            Objects.requireNonNull(inputStream,"init.js resource stream");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            cx.evaluateReader(scope, inputStreamReader, "{mychat}/init.js",1, null);
            inputStreamReader.close();
            inputStream.close();
            require("index");
            return ReloadEvent.Result.SUCCESS;
        } catch (Exception exception) {
            logger.warn("Failed to load inner init script.", exception);
            return ReloadEvent.Result.FAILED;
        }
    }

    public Object require(String name) throws IOException {
        name = name + ".js";
        Script compiledScript = compiledScripts.get(name);
        if (compiledScript == null) {
            Path p = pluginContainer.configPath();
            for (String s : name.split("/")) {
                p = p.resolve(s);
            }
            File requireFile = p.toFile();
            InputStream inputStream = new FileInputStream(requireFile);
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            compiledScript = cx.compileReader(reader, name, 1, null);
            reader.close();
            inputStream.close();
            compiledScripts.put(name, compiledScript);
        }
        return compiledScript.exec(cx, scope);
    }

    @EventHandler(order = Order.POST)
    public void onReload(final ReloadEvent event) {
        event.result(load());
    }

    @EventHandler
    public void onChatEvent(final ChatEvent event) {
        if(event.cancelled()){
            return;
        }
        if(event.originalMessage().equals(".")){
            eventManager.post(new OpenMenuEvent(event.sender()));
            event.cancel();
            return;
        }
        try {
            ((Function) scope.get("onChat",scope)).call(cx,scope,null, new Object[]{event});
        } catch (Exception e) {
            logger.warn("Failed to pass ChatEvent.",e);
        }
    }
}
