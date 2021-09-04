var Apsaras = org.apsarasmc.apsaras.Apsaras;
var ChatFormatUtil = Apsaras.injector().getInstance(
    com.eloli.mychat.util.ChatFormatUtil
);
var PlaceHolderUtil = Apsaras.injector().getInstance(
    com.eloli.mychat.util.PlaceHolderUtil
);
var ComponentUtil = com.eloli.mychat.util.ComponentUtil;
var _MyChatCore = Apsaras.injector().getInstance(
    com.eloli.mychat.MyChatCore
);
function require(name) {
    return _MyChatCore.require(name);
}
var Sound = net.kyori.adventure.sound.Sound;
var ResourceKey = org.apsarasmc.apsaras.util.ResourceKey;
var Component = net.kyori.adventure.text.Component;
var Player = org.apsarasmc.apsaras.entity.Player;
var TextColor = net.kyori.adventure.text.format.TextColor;
var NamedTextColor = net.kyori.adventure.text.format.NamedTextColor;
var ClickEvent = net.kyori.adventure.text.event.ClickEvent;
var HoverEvent = net.kyori.adventure.text.event.HoverEvent;