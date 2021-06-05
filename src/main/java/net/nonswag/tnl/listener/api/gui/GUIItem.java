package net.nonswag.tnl.listener.api.gui;

import net.nonswag.tnl.listener.api.gui.iterators.InteractionIterator;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.object.Objects;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class GUIItem implements Iterable<Interaction>, Cloneable {

    @Nonnull
    private final Objects<TNLItem> builder = new Objects<>();
    @Nonnull
    private final List<Interaction> interactions = new ArrayList<>();

    public GUIItem(@Nonnull ItemStack itemStack) {
        this(TNLItem.create(itemStack));
    }

    public GUIItem(@Nonnull TNLItem builder) {
        getBuilder().setValue(builder);
    }

    @Nonnull
    public List<Interaction> interactions() {
        return new ArrayList<>(interactions);
    }

    @Nonnull
    private List<Interaction> getInteractions() {
        return interactions;
    }

    @Nonnull
    public GUIItem addInteractions(@Nonnull Interaction... interactions) {
        for (Interaction interaction : interactions) {
            if (!getInteractions().contains(interaction)) {
                getInteractions().add(interaction);
            }
        }
        return this;
    }

    @Nonnull
    public GUIItem removeInteractions(@Nonnull Interaction... interactions) {
        getInteractions().removeAll(Arrays.asList(interactions));
        return this;
    }

    @Nonnull
    public List<Interaction> getInteractions(@Nonnull Interaction.Type type) {
        List<Interaction> interactions = new ArrayList<>();
        for (Interaction interaction : this) {
            for (@Nullable Interaction.Type interactionType : interaction) {
                if (type.comparable(interactionType != null ? interactionType : Interaction.Type.LEFT)) {
                    interactions.add(interaction);
                }
            }
        }
        return interactions;
    }

    @Nonnull
    public Objects<TNLItem> getBuilder() {
        return builder;
    }

    @Nonnull
    @Override
    public Iterator<Interaction> iterator() {
        return new InteractionIterator(this);
    }

    @Nonnull
    public ListIterator<Interaction> iterator(int i) {
        return new InteractionIterator(this, i);
    }
}
