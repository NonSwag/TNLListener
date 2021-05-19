package net.nonswag.tnl.listener.api.gui.iterators;

import net.nonswag.tnl.listener.api.gui.GUI;
import net.nonswag.tnl.listener.api.gui.GUIItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ListIterator;

public class GUIIterator implements ListIterator<GUIItem> {

    @Nonnull
    private final GUI gui;
    @Nullable
    private Boolean lastDirection;
    private int nextIndex;

    public GUIIterator(@Nonnull GUI gui) {
        this(gui, 0);
    }

    public GUIIterator(@Nonnull GUI gui, int index) {
        this.gui = gui;
        this.nextIndex = index;
    }

    @Nonnull
    private GUI getGui() {
        return gui;
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
        return this.nextIndex < getGui().getSize();
    }

    @Override
    public GUIItem next() {
        setLastDirection(true);
        return getGui().getItem(this.nextIndex++);
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
    public GUIItem previous() {
        setLastDirection(false);
        return getGui().getItem(this.nextIndex--);
    }

    public int previousIndex() {
        return getNextIndex() - 1;
    }

    @Override
    public void set(@Nonnull GUIItem item) {
        if (getLastDirection() == null) {
            throw new IllegalStateException("No current item!");
        } else {
            int i = getLastDirection() ? getNextIndex() - 1 : getNextIndex();
            getGui().setItem(i, item);
        }
    }

    @Override
    public void add(@Nonnull GUIItem item) {
        throw new UnsupportedOperationException("Can't change the size of an gui!");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't change the size of an gui!");
    }
}
