package net.nonswag.tnl.listener.api.permission;

import net.nonswag.tnl.listener.Loader;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import net.nonswag.tnl.listener.api.reflection.Reflection;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager implements Permissions {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final PermissionAttachment attachment;

    public PermissionManager(@Nonnull TNLPlayer player) {
        this.player = player;
        this.attachment = player.getBukkitPlayer().addAttachment(Loader.getInstance());
    }

    @Nonnull
    public TNLPlayer getPlayer() {
        return player;
    }

    @Nonnull
    public PermissionAttachment getAttachment() {
        return attachment;
    }

    @Override
    public void setPermission(@Nonnull String permission, boolean allowed) {
        if (allowed) {
            attachment.setPermission(permission, true);
        } else {
            attachment.unsetPermission(permission);
        }
    }

    @Override
    public void addPermission(@Nonnull String... permissions) {
        for (String permission : permissions) {
            setPermission(permission, true);
        }
    }

    @Override
    public void removePermission(@Nonnull String... permissions) {
        for (String s : permissions) {
            setPermission(s, false);
        }
    }

    @Override
    public boolean hasPermission(@Nonnull String permission) {
        return getPlayer().getBukkitPlayer().hasPermission(permission);
    }

    @Nonnull
    @Override
    public List<String> getPermissions() {
        List<String> permissions = new ArrayList<>();
        for (PermissionAttachmentInfo info : getPlayer().getBukkitPlayer().getEffectivePermissions()) {
            permissions.add(info.getPermission());
        }
        return permissions;
    }

    @Override
    public void updatePermissions() {
        Class<?> clazz = null;
        if (getPlayer() instanceof net.nonswag.tnl.listener.api.player.v1_15.R1.NMSPlayer) {
            try {
                clazz = Reflection.getClass("net.minecraft.server.v1_15_R1.PacketPlayOutEntityStatus");
            } catch (NoClassDefFoundError e) {
                e.printStackTrace();
            }
        } else if (getPlayer() instanceof net.nonswag.tnl.listener.api.player.v1_7.R1.NMSPlayer) {
            try {
                clazz = Reflection.getClass("net.minecraft.server.v1_7_R1.PacketPlayOutEntityStatus");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else if (getPlayer() instanceof net.nonswag.tnl.listener.api.player.v1_7.R4.NMSPlayer) {
            try {
                clazz = Reflection.getClass("net.minecraft.server.v1_7_R4.PacketPlayOutEntityStatus");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (clazz != null) {
            try {
                Object packet = clazz.newInstance();
                Reflection.setField(packet, "a", getPlayer().getEntityId());
                Reflection.setField(packet, "b", (byte) 28);
                getPlayer().sendPacket(packet);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isOp() {
        return getPlayer().getBukkitPlayer().isOp();
    }

    @Override
    public void setOp(boolean op) {
        getPlayer().getBukkitPlayer().setOp(op);
    }
}
