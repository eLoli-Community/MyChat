package com.eloli.mychat.menu;

import com.eloli.mychat.event.OpenMenuEvent;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.apsarasmc.apsaras.event.EventHandler;

import javax.inject.Singleton;

@Singleton
public class OpenMenuHandler{
    @EventHandler
    public void onOpenMenu(OpenMenuEvent event){
        event.player().openBook(new OpenMenu(event).init().compile());
    }

    class OpenMenu{
        private final OpenMenuEvent event;
        private final Book.Builder builder;

        private Component component = Component.empty();
        private int line = 0;

        public OpenMenu(OpenMenuEvent event) {
            this.event = event;
            builder = Book.builder();
        }

        public OpenMenu init(){
            builder.title(Component.text("MY CHAT MENU BOOK"));
            builder.author(Component.text("All MyChat Developers"));
            addLine(Component.text(">>>  My Chat 1.0-SNAPSHOT  <<<"));
            addLine(Component.text(">>> As Apsaras test plugin <<<"));
            addLine(Component.text(">>>by All MyChat Developers<<<"));
            return this;
        }

        private void addLine(Component context) {
            component = component.append(context).append(Component.newline());
            line ++;
            if(line == 14){
                newPage();
            }
        }
        private void newPage(){
            line = 0;
            builder.addPage(component);
            component = Component.empty();
        }

        public Book compile(){
            if(line != 0){
                newPage();
            }
            return builder.build();
        }
    }
}
