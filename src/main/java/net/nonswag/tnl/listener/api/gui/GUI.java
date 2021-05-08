package net.nonswag.tnl.listener.api.gui;

import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.item.TNLItemType;
import net.nonswag.tnl.listener.api.object.Pair;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.common.value.qual.IntRange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class GUI implements Iterable<GUIItem> {

    @Nonnull
    private final HashMap<Integer, GUIItem> contentHashMap = new HashMap<>();
    @Nonnull
    private final List<TNLPlayer> viewers = new ArrayList<>();
    @Nonnull
    private final String title;
    private final int size;
    private int maxStackSize;

    public GUI(@IntRange(from = 0, to = 6) int rows, @Nonnull String title) {
        this(rows, 64, title);
    }

    public GUI(@IntRange(from = 0, to = 6) int rows, int maxStackSize, @Nonnull String title) {
        this.size = rows * 9;
        this.title = title;
        this.maxStackSize = maxStackSize;
    }

    public GUI(@IntRange(from = 0, to = 6) int rows, int maxStackSize) {
        this(rows, maxStackSize, "§8» §4§lUnnamed§c§lGUI");
    }

    public GUI(@IntRange(from = 0, to = 6) int rows) {
        this(rows, "§8» §4§lUnnamed§c§lGUI");
    }

    public GUI(@Nonnull Inventory inventory) {
        this(inventory.getSize() / 9, inventory.getMaxStackSize());
    }

    @Nonnull
    private HashMap<Integer, GUIItem> getContentHashMap() {
        return contentHashMap;
    }

    @Nonnull
    public List<TNLPlayer> getViewers() {
        return viewers;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    @Nullable
    public GUIItem getItem(int slot) {
        return getContentHashMap().getOrDefault(slot, null);
    }

    public void setItem(int slot, @Nullable GUIItem item) throws IndexOutOfBoundsException {
        if (item != null && !item.getBuilder().isAir()) {
            if (slot >= 0 && slot < getSize()) {
                getContentHashMap().put(slot, item);
            } else {
                throw new IndexOutOfBoundsException("Slot '" + slot + "' is outside the gui");
            }
        } else {
            getContentHashMap().remove(slot);
        }
    }

    public void setItem(int slot, @Nullable TNLItem item) throws IndexOutOfBoundsException {
        setItem(slot, item == null ? null : item.toGUIItem());
    }

    public void setItem(int slot, @Nullable ItemStack itemStack) throws IndexOutOfBoundsException {
        setItem(slot, itemStack == null ? null : TNLItem.create(itemStack));
    }

    public void setItem(int slot, @Nullable Material material) throws IndexOutOfBoundsException {
        setItem(slot, material == null ? null : new ItemStack(material));
    }

    public void addItems(@Nonnull GUIItem... items) {
        items:
        for (@Nullable GUIItem item : items) {
            for (int slot = 0; slot < getSize(); slot++) {
                if (TNLItemType.isAir(getItem(slot))) {
                    setItem(slot, items[slot]);
                    continue items;
                }
            }
            break;
        }
    }

    public void removeItems(@Nonnull GUIItem... items) {
        for (@Nullable GUIItem item : items) {
            if (item != null) {
                remove(item);
            }
        }
    }

    @Nonnull
    public GUIItem[] getContents() {
        GUIItem[] items = new GUIItem[getSize()];
        for (int i = 0; i < getSize(); i++) {
            items[i] = getItem(i);
        }
        return items;
    }

    public void setContents(@Nonnull GUIItem[] items) {
        clear();
        for (int i = 0; i < getSize() && i < items.length; i++) {
            setItem(i, items[i]);
        }
    }

    public boolean contains(@Nonnull GUIItem item) {
        return contains(item.getBuilder());
    }

    public boolean contains(@Nonnull TNLItem item) {
        return contains(item.getItemStack());
    }

    public boolean contains(@Nonnull Material material) {
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(material) && TNLItemType.isAir(content) || (content != null && material.equals(content.getBuilder().getType()))) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(@Nonnull ItemStack itemStack) {
        return contains(itemStack, false);
    }

    public boolean contains(@Nonnull ItemStack itemStack, boolean ignoreAmount) {
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(itemStack) && TNLItemType.isAir(content)) {
                return true;
            } else {
                if (ignoreAmount) {
                    if (content != null) {
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(content.getBuilder().getAmount());
                        if (clone.equals(content.getBuilder().build())) {
                            return true;
                        }
                    }
                } else {
                    if (content != null && itemStack.equals(content.getBuilder().build())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean containsAtLeast(@Nonnull GUIItem item, int amount) {
        return containsAtLeast(item.getBuilder(), amount);
    }

    public boolean containsAtLeast(@Nonnull TNLItem item, int amount) {
        return containsAtLeast(item.getItemStack(), amount);
    }

    public boolean containsAtLeast(@Nonnull Material material, int amount) {
        int finalAmount = 0;
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(material) && TNLItemType.isAir(content)) {
                return true;
            } else {
                if (content != null) {
                    if (material.equals(content.getBuilder().getType())) {
                        if (content.getBuilder().getAmount() >= amount) {
                            return true;
                        } else {
                            finalAmount += content.getBuilder().getAmount();
                        }
                    }
                }
            }
        }
        return finalAmount >= amount;
    }

    public boolean containsAtLeast(@Nonnull ItemStack itemStack, int amount) {
        int finalAmount = 0;
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(itemStack) && TNLItemType.isAir(content)) {
                return true;
            } else {
                if (content != null) {
                    ItemStack clone = itemStack.clone();
                    clone.setAmount(content.getBuilder().getAmount());
                    if (clone.equals(content.getBuilder().build())) {
                        if (clone.getAmount() >= amount) {
                            return true;
                        } else {
                            finalAmount += clone.getAmount();
                        }
                    }
                }
            }
        }
        return finalAmount >= amount;
    }

    @Nonnull
    public HashMap<Integer, GUIItem> all(@Nonnull GUIItem item) {
        return all(item.getBuilder());
    }

    @Nonnull
    public HashMap<Integer, GUIItem> all(@Nonnull TNLItem item) {
        return all(item.getItemStack());
    }

    @Nonnull
    public HashMap<Integer, GUIItem> all(@Nonnull Material material) {
        HashMap<Integer, GUIItem> items = new HashMap<>();
        GUIItem[] contents = getContents();
        for (int i = 0; i < getSize(); i++) {
            if (TNLItemType.isAir(contents[i]) && TNLItemType.isAir(material) || (contents[i] != null && contents[i].getBuilder().getType().equals(material))) {
                items.put(i, contents[i]);
            }
        }
        return items;
    }

    @Nonnull
    public HashMap<Integer, GUIItem> all(@Nonnull ItemStack itemStack) {
        return all(itemStack, false);
    }

    @Nonnull
    public HashMap<Integer, GUIItem> all(@Nonnull ItemStack itemStack, boolean ignoreAmount) {
        HashMap<Integer, GUIItem> items = new HashMap<>();
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < getSize(); slot++) {
            if (TNLItemType.isAir(contents[slot]) && TNLItemType.isAir(itemStack.getType())) {
                items.put(slot, contents[slot]);
            } else {
                if (itemStack.getType().equals(contents[slot].getBuilder().getType())) {
                    if (ignoreAmount) {
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(contents[slot].getBuilder().getAmount());
                        if (clone.equals(contents[slot].getBuilder().build())) {
                            items.put(slot, contents[slot]);
                        }
                    } else {
                        if (itemStack.equals(contents[slot].getBuilder().build())) {
                            items.put(slot, contents[slot]);
                        }
                    }
                }
            }
        }
        return items;
    }

    public int first(@Nonnull GUIItem item) {
        return first(item.getBuilder());
    }

    public int first(@Nonnull TNLItem item) {
        return first(item.getItemStack());
    }

    public int first(@Nonnull Material material) {
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < getSize(); slot++) {
            if ((TNLItemType.isAir(contents[slot]) && TNLItemType.isAir(material)) || material.equals(contents[slot].getBuilder().getType())) {
                return slot;
            }
        }
        return -1;
    }

    public int first(@Nonnull ItemStack itemStack) {
        return first(itemStack, false);
    }

    public int first(@Nonnull ItemStack itemStack, boolean ignoreAmount) {
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < getSize(); slot++) {
            if (TNLItemType.isAir(contents[slot]) && TNLItemType.isAir(itemStack.getType())) {
                return slot;
            } else {
                if (itemStack.getType().equals(contents[slot].getBuilder().getType())) {
                    if (ignoreAmount) {
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(contents[slot].getBuilder().getAmount());
                        if (clone.equals(contents[slot].getBuilder().build())) {
                            return slot;
                        }
                    } else {
                        if (itemStack.equals(contents[slot].getBuilder().build())) {
                            return slot;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public int firstEmpty() {
        GUIItem[] contents = getContents();
        for (int i = 0; i < contents.length; ++i) {
            if (contents[i] == null || contents[i].getBuilder().isAir()) {
                return i;
            }
        }
        return -1;
    }

    @Nonnull
    public Pair<Boolean, Integer> remove(@Nonnull GUIItem item) {
        return remove(item.getBuilder());
    }

    @Nonnull
    public Pair<Boolean, Integer> remove(@Nonnull TNLItem item) {
        return remove(item.getItemStack());
    }

    @Nonnull
    public Pair<Boolean, Integer> remove(@Nonnull ItemStack item) {
        boolean removed = false;
        int amount = 0;
        GUIItem[] contents = getContents();
        for(int i = 0; i < contents.length; i++) {
            if (contents[i] != null && contents[i].getBuilder().build().equals(item)) {
                remove(i);
                removed = true;
                amount++;
            }
        }
        return new Pair<>(removed, amount);
    }

    @Nonnull
    public Pair<Boolean, Integer> remove(@Nonnull Material material) {
        boolean removed = false;
        int amount = 0;
        GUIItem[] contents = getContents();
        for(int slot = 0; slot < contents.length; slot++) {
            if (contents[slot] != null && contents[slot].getBuilder().getType().equals(material)) {
                remove(slot);
                removed = true;
                amount++;
            }
        }
        return new Pair<>(removed, amount);
    }

    public void remove(int i) {
        getContentHashMap().remove(i);
    }

    public void clear() {
        getContentHashMap().clear();
    }

    @Nonnull
    public ListIterator<GUIItem> iterator() {
        return new GUIIterator(this);
    }

    @Nonnull
    public ListIterator<GUIItem> iterator(int i) {
        return new GUIIterator(this, i);
    }

    private int firstPartial(@Nonnull GUIItem item) {
        return firstPartial(item.getBuilder());
    }

    private int firstPartial(@Nonnull TNLItem item) {
        return firstPartial(item.getItemStack());
    }

    private int firstPartial(@Nonnull ItemStack itemStack) {
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < contents.length; ++slot) {
            GUIItem item = contents[slot];
            if (item != null && item.getBuilder().getAmount() < item.getBuilder().getMaxStackSize() && item.getBuilder().build().equals(itemStack)) {
                return slot;
            }
        }
        return -1;
    }

    public int firstPartial(@Nonnull Material material) {
        GUIItem[] contents = getContents();
        for(int slot = 0; slot < contents.length; slot++) {
            GUIItem item = contents[slot];
            if (item != null && item.getBuilder().getType() == material && item.getBuilder().getAmount() < item.getBuilder().getMaxStackSize()) {
                return slot;
            }
        }
        return -1;
    }
}
