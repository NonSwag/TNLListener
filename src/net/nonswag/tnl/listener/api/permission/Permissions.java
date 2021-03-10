package net.nonswag.tnl.listener.api.permission;

import javax.annotation.Nonnull;
import java.util.List;

public interface Permissions {

    void setPermission(@Nonnull String permission, boolean allowed);

    void addPermission(@Nonnull String... permissions);

    void removePermission(@Nonnull String... permissions);

    boolean hasPermission(@Nonnull String permission);

    @Nonnull
    List<String> getPermissions();
}
