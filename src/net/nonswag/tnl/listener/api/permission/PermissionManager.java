package net.nonswag.tnl.listener.api.permission;

import net.nonswag.tnl.listener.NMSMain;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PermissionManager implements Permissions, ServerOperator {

    @Nonnull
    private final TNLPlayer player;
    @Nonnull
    private final PermissionAttachment attachment;

    public PermissionManager(@Nonnull TNLPlayer player) {
        this.player = player;
        this.attachment = player.getBukkitPlayer().addAttachment(NMSMain.getInstance());
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
    public void addPermission(@Nonnull Permission... permissions) {
        for (Permission permission : permissions) {
            setPermission(permission.getPermission(), permission.isAllowed());
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
    public boolean isOp() {
        return getPlayer().getBukkitPlayer().isOp();
    }

    @Override
    public void setOp(boolean op) {
        getPlayer().getBukkitPlayer().setOp(op);
    }
}