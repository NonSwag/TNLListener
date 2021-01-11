package net.nonswag.tnl.listener.v1_14_R1.api.bossbar;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BossBar {

    private static final HashMap<Player, org.bukkit.boss.BossBar> bossHashMap = new HashMap<>();

    public static HashMap<Player, org.bukkit.boss.BossBar> getBossHashMap() {
        return bossHashMap;
    }

    public static void send(Player player, String title, int seconds, BarColor color, BarStyle style, BarFlag flag, double process) {
        NamespacedKey key = new NamespacedKey(NMSMain.getPlugin(), player.getName() + "-BossBar");
        if (getBossHashMap() != null && getBossHashMap().get(player) != null && Bukkit.getBossBar(key) != null) {
            getBossHashMap().get(player).setVisible(false);
            Bukkit.removeBossBar(key);
        }
        org.bukkit.boss.BossBar bossBar = Bukkit.createBossBar(key, title, color, style, flag);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        bossBar.setProgress(process);
        getBossHashMap().put(player, bossBar);
        NMSMain.delayedTask(() -> {
            if (getBossHashMap() != null && getBossHashMap().get(player) != null
                    && Bukkit.getBossBar(key) != null && getBossHashMap().get(player).equals(bossBar)) {
                getBossHashMap().get(player).setVisible(false);
                Bukkit.removeBossBar(key);
            }
        }, seconds * 20);
    }
}
