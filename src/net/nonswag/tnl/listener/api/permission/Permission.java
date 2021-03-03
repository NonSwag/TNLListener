package net.nonswag.tnl.listener.api.permission;

import javax.annotation.Nonnull;

public class Permission {

    @Nonnull
    private final String permission;
    private final boolean allowed;

    public Permission(@Nonnull String permission, boolean allowed) {
        this.permission = permission;
        this.allowed = allowed;
    }

    @Nonnull
    public String getPermission() {
        return permission;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
