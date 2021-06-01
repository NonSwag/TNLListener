package net.nonswag.tnl.listener.api.gui.iterators;

import net.nonswag.tnl.listener.api.gui.Interaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ListIterator;

public class TypeIterator implements ListIterator<Interaction.Type> {

    @Nonnull
    private final Interaction interaction;
    @Nullable
    private Boolean lastDirection;
    private int nextIndex;

    public TypeIterator(@Nonnull Interaction interaction) {
        this(interaction, 0);
    }

    public TypeIterator(@Nonnull Interaction interaction, int index) {
        this.interaction = interaction;
        this.nextIndex = index;
    }

    @Nonnull
    public Interaction getInteraction() {
        return interaction;
    }

    @Nullable
    public Boolean getLastDirection() {
        return lastDirection;
    }

    private int getNextIndex() {
        return nextIndex;
    }

    private void setLastDirection(boolean lastDirection) {
        this.lastDirection = lastDirection;
    }

    private void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }

    @Override
    public boolean hasNext() {
        return this.nextIndex < getInteraction().getTypes().size();
    }

    @Override
    public Interaction.Type next() {
        setLastDirection(true);
        return getInteraction().getTypes().get(this.nextIndex++);
    }

    @Override
    public int nextIndex() {
        return getNextIndex();
    }

    @Override
    public boolean hasPrevious() {
        return getNextIndex() > 0;
    }

    @Override
    public Interaction.Type previous() {
        setLastDirection(false);
        return getInteraction().getTypes().get(this.nextIndex--);
    }

    public int previousIndex() {
        return getNextIndex() - 1;
    }

    @Override
    public void set(@Nonnull Interaction.Type type) {
        if (getLastDirection() == null) {
            throw new IllegalStateException("No current type!");
        } else {
            int i = getLastDirection() ? getNextIndex() - 1 : getNextIndex();
            getInteraction().getTypes().set(i, type);
        }
    }

    @Override
    public void add(Interaction.Type type) {
        throw new UnsupportedOperationException("Can't change the size of an type array!");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an type array!");
    }
}
