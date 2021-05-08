package net.nonswag.tnl.listener.api.permission;

import org.bukkit.permissions.ServerOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface Permissions extends ServerOperator {

    void setPermission(@Nonnull String permission, boolean allowed);

    void addPermission(@Nonnull String... permissions);

    void removePermission(@Nonnull String... permissions);

    boolean hasPermission(@Nonnull String permission);

    @Nonnull
    List<String> getPermissions();

    void updatePermissions();

    @Nullable
    String getValue(@Nonnull String value, int index);
}
