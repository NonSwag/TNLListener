package net.nonswag.tnl.listener.api.gui;

import net.nonswag.tnl.listener.api.item.TNLItem;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class GUIItem {

    @Nonnull
    private final TNLItem builder;
    @Nonnull
    private Interaction interaction = Interaction.EMPTY;

    public GUIItem(@Nonnull ItemStack itemStack) {
        this(TNLItem.create(itemStack));
    }

    public GUIItem(@Nonnull TNLItem builder) {
        this.builder = builder;
    }

    @Nonnull
    public Interaction getInteraction() {
        return interaction;
    }

    @Nonnull
    public GUIItem setInteraction(@Nonnull Interaction interaction) {
        this.interaction = interaction;
        return this;
    }

    @Nonnull
    public TNLItem getBuilder() {
        return builder;
    }
}
