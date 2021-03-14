package net.nonswag.tnl.listener.api.player;

import net.minecraft.server.v1_15_R1.ChatMessage;
import net.nonswag.tnl.listener.api.chat.Message;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BackFlip {

    @Nonnull
    private final TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player;

    public BackFlip(@Nonnull TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> player) {
        this.player = player;
    }

    @Nonnull
    public TNLPlayer<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> getPlayer() {
        return player;
    }

    public void sendJsonMessage(@Nonnull Message message) {
        sendJsonMessage(message.getChatMessage());
    }

    public void sendJsonMessage(@Nonnull ChatMessage message) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + getPlayer().getName() + " " + message.getText());
    }

    @Override
    public String toString() {
        return "BackFlip{" +
                "player=" + player +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackFlip backFlip = (BackFlip) o;
        return player.equals(backFlip.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }
}
