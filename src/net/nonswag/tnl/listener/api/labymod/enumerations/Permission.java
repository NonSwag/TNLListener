package net.nonswag.tnl.listener.api.labymod.enumerations;

import javax.annotation.Nonnull;

public enum Permission {
    IMPROVED_LAVA("Improved Lava", false),
    CROSSHAIR_SYNC("Crosshair sync", false),
    REFILL_FIX("Refill fix", false),
    GUI_ALL("LabyMod GUI", true),
    GUI_POTION_EFFECTS("Potion Effects", true),
    GUI_ARMOR_HUD("Armor HUD", true),
    GUI_ITEM_HUD("Item HUD", true),
    BLOCKBUILD("Blockbuild", true),
    TAGS("Tags", true),
    CHAT("Chat features", true),
    ANIMATIONS("Animations", true),
    SATURATION_BAR("Saturation bar", true),
    ;

    @Nonnull private final String displayName;
    private final boolean defaultEnabled;

    Permission(@Nonnull String displayName, boolean defaultEnabled) {
        this.displayName = displayName;
        this.defaultEnabled = defaultEnabled;
    }

    @Nonnull
    public String getDisplayName() {
        return displayName;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }
}
