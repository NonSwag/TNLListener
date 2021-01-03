package net.nonswag.tnl.listener.v1_16_R1.api.playerAPI;

import net.minecraft.server.v1_16_R1.ChatMessage;
import net.nonswag.tnl.listener.v1_16_R1.api.chatAPI.Message;
import org.bukkit.Bukkit;

import java.util.Objects;

public class BackFlip {

    private final TNLPlayer player;

    public BackFlip(TNLPlayer player) {
        this.player = player;
    }

    public TNLPlayer getPlayer() {
        return player;
    }

    public void sendJsonMessage(Message message) {
        sendJsonMessage(message.getChatMessage());
    }

    public void sendJsonMessage(ChatMessage message) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + getPlayer().getName() + " " + message.getText());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackFlip backFlip = (BackFlip) o;
        return Objects.equals(player, backFlip.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "BackFlip{" +
                "player=" + player +
                '}';
    }
}
