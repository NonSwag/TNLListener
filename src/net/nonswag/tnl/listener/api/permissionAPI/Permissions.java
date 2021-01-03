package net.nonswag.tnl.listener.api.permissionAPI;

import net.nonswag.tnl.listener.NMSMain;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;

public final class Permissions {

    public static void add(Player player, String... permission) {
        PermissionAttachment attachment = player.addAttachment(NMSMain.getPlugin());
        for (String s : permission) {
            attachment.setPermission(s, true);
        }
    }

    public static void remove(Player player, String... permission) {
        PermissionAttachment attachment = player.addAttachment(NMSMain.getPlugin());
        for (String s : permission) {
            attachment.setPermission(s, false);
            attachment.unsetPermission(s);
        }
    }

    public static List<String> getPermissions(Player player) {
        List<String> permissions = new ArrayList<>();
        for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
            permissions.add(info.getPermission());
        }
        return permissions;
    }
}
