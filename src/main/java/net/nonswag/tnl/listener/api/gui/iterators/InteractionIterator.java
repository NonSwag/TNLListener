package net.nonswag.tnl.listener.api.gui.iterators;

import net.nonswag.tnl.listener.api.gui.GUIItem;
import net.nonswag.tnl.listener.api.gui.Interaction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ListIterator;

public class InteractionIterator implements ListIterator<Interaction> {

    @Nonnull
    private final GUIItem guiItem;
    @Nullable
    private Boolean lastDirection;
    private int nextIndex;

    public InteractionIterator(@Nonnull GUIItem guiItem) {
        this(guiItem, 0);
    }

    public InteractionIterator(@Nonnull GUIItem guiItem, int index) {
        this.guiItem = guiItem;
        this.nextIndex = index;
    }

    @Nonnull
    public GUIItem getGuiItem() {
        return guiItem;
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
        return this.nextIndex < getGuiItem().interactions().size();
    }

    @Override
    public Interaction next() {
        setLastDirection(true);
        return getGuiItem().interactions().get(this.nextIndex++);
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
    public Interaction previous() {
        setLastDirection(false);
        return getGuiItem().interactions().get(this.nextIndex--);
    }

    public int previousIndex() {
        return getNextIndex() - 1;
    }

    @Override
    public void set(@Nonnull Interaction interaction) {
        if (getLastDirection() == null) {
            throw new IllegalStateException("No current interaction!");
        } else {
            int i = getLastDirection() ? getNextIndex() - 1 : getNextIndex();
            getGuiItem().interactions().set(i, interaction);
        }
    }

    @Override
    public void add(@Nonnull Interaction interaction) {
        throw new UnsupportedOperationException("Can't change the size of an interaction!");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an interaction!");
    }
}
