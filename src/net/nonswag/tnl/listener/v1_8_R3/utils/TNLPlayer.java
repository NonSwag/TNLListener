package net.nonswag.tnl.listener.v1_8_R3.utils;

import com.google.common.annotations.Beta;
import com.sun.istack.internal.NotNull;
import net.minecraft.server.v1_8_R3.*;
import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashMap;
import java.util.List;

@Beta
public class TNLPlayer {

    private final Player player;
    private static final HashMap<World, String> namedWorldHashMap = new HashMap<>();
    private final File dataFolder = NMSMain.getPlugin().getDataFolder();

    @Deprecated
    public TNLPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void sendPacket(Packet<?> packet) {
        PacketUtil.sendPacket(this.player, packet);
    }

    public void sendMessage(String string) {
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatMessage(string));
        sendPacket(packet);
    }

    public void disconnect() {
        disconnect(NMSMain.getPrefix() + "\n§cDisconnected");
    }

    public void disconnect(String kickMessage) {
        NMSMain.runTask(() -> {
            if (!((CraftPlayer) player).getHandle().playerConnection.isDisconnected()) {
                ((CraftPlayer) player).getHandle().playerConnection.disconnect(kickMessage);
            }
        });
    }

    public String getNamedWorld() {
        return getNamedWorldHashMap().get(player.getWorld()) + "";
    }

    public static HashMap<World, String> getNamedWorldHashMap() {
        return namedWorldHashMap;
    }

    public static void registerNamedWorld(World world, String namedWorld) {
        getNamedWorldHashMap().put(world, namedWorld);
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public void hideTabListName(@Nullable Player[] players) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        if (players == null) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                PacketUtil.sendPacket(all, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
            }
        } else {
            for (Player all : players) {
                PacketUtil.sendPacket(all, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
            }
        }
    }

    public void loadInventory(@NotNull String id) {
        File file = new File(dataFolder + "/" + player.getUniqueId() + ".yml");
        try {
            if (file.exists()) {
                YamlConfiguration inventory = YamlConfiguration.loadConfiguration(file);
                if (inventory.isSet(id)) {
                    List<?> contents = inventory.getList(id);
                    if (contents != null) {
                        for (int i = 0; i < contents.size(); i++) {
                            this.getInventory().setItem(i, ((ItemStack) contents.get(i)));
                        }
                    }
                }
            }
        } catch (Throwable t) {
            NMSMain.stacktrace(t);
        }
    }

    private void mkdirInventories() {
        if (!dataFolder.exists()) {
            if (dataFolder.mkdir()) {
                NMSMain.print("Successfully created folder '" + dataFolder.getName() + "'");
            } else {
                NMSMain.stacktrace("Failed to create folder '" + dataFolder.getName() + "'",
                        "Check if your software runs with the permission '777', 'root' or higher",
                        "Cloud and any kind of Remote software may cause issues if the server loads from a template");
            }
        }
    }
}
