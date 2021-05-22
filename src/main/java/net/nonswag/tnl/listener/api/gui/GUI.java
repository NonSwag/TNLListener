package net.nonswag.tnl.listener.api.gui;

import net.nonswag.tnl.listener.api.annotation.Info;
import net.nonswag.tnl.listener.api.gui.iterators.GUIIterator;
import net.nonswag.tnl.listener.api.item.TNLItem;
import net.nonswag.tnl.listener.api.item.TNLItemType;
import net.nonswag.tnl.listener.api.math.Range;
import net.nonswag.tnl.listener.api.object.Pair;
import net.nonswag.tnl.listener.api.packet.TNLSetSlot;
import net.nonswag.tnl.listener.api.player.TNLPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class GUI implements Iterable<GUIItem>, Cloneable {

    @Nonnull
    private final HashMap<Integer, GUIItem> contentHashMap = new HashMap<>();
    @Nonnull
    private final List<TNLPlayer> viewers = new ArrayList<>();
    @Nonnull
    private final String title;
    @Nonnull
    private ClickEvent clickListener = (player, slot, type) -> true;
    private final int size;
    private boolean instantUpdates = false;
    private boolean playSounds = true;

    public GUI(@Range(from = 1, to = 6) int rows, @Nonnull String title) {
        this(rows, 64, title);
    }

    public GUI(@Range(from = 1, to = 6) int rows, int maxStackSize, @Nonnull String title) {
        this.size = Math.min(Math.max(rows, 1), 6) * 9;
        this.title = title;
    }

    public GUI(@Range(from = 1, to = 6) int rows, int maxStackSize) {
        this(rows, maxStackSize, "§8» §4§lUnnamed§c§lGUI");
    }

    public GUI(@Range(from = 1, to = 6) int rows) {
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
    public ClickEvent getClickListener() {
        return clickListener;
    }

    public void setClickListener(@Nonnull ClickEvent clickListener) {
        this.clickListener = clickListener;
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public boolean isInstantUpdates() {
        return instantUpdates;
    }

    public void setInstantUpdates(boolean instantUpdates) {
        this.instantUpdates = instantUpdates;
    }

    public boolean isPlaySounds() {
        return playSounds;
    }

    public void setPlaySounds(boolean playSounds) {
        this.playSounds = playSounds;
    }

    @Nullable
    public GUIItem getItem(int slot) {
        return getContentHashMap().get(slot);
    }

    @Nonnull
    public GUI setItem(int slot, @Nullable GUIItem item) throws IllegalArgumentException {
        if (item != null) {
            if (slot >= 0 && slot < getSize()) getContentHashMap().put(slot, item);
            else throw new IllegalArgumentException("Slot '" + slot + "' is outside the gui");
        } else remove(slot);
        if (isInstantUpdates()) {
            for (TNLPlayer viewer : getViewers()) {
                if (item != null) viewer.sendPackets(TNLSetSlot.create(slot, item));
                else viewer.sendPackets(TNLSetSlot.create(slot, new ItemStack(Material.AIR)));
            }
        }
        return this;
    }

    @Nonnull
    public GUI setItem(int slot, @Nullable TNLItem item) throws IllegalArgumentException {
        return setItem(slot, item == null ? null : item.toGUIItem());
    }

    @Nonnull
    public GUI setItem(int slot, @Nullable ItemStack itemStack) throws IllegalArgumentException {
        return setItem(slot, itemStack == null ? null : TNLItem.create(itemStack));
    }

    @Nonnull
    public GUI setItem(int slot, @Nullable Material material) throws IllegalArgumentException {
        return setItem(slot, material == null ? null : new ItemStack(material));
    }

    @Nonnull
    public GUI addItem(@Nonnull GUIItem item) {
        return addItems(item);
    }

    @Nonnull
    public GUI addItems(@Nonnull GUIItem... items) {
        items:
        for (@Nullable GUIItem item : items) {
            for (int slot = 0; slot < getSize(); slot++) {
                if (TNLItemType.isAir(getItem(slot))) {
                    setItem(slot, item);
                    continue items;
                }
            }
            break;
        }
        return this;
    }

    @Nonnull
    public GUI removeItems(@Nonnull GUIItem... items) {
        for (@Nullable GUIItem item : items) if (item != null) remove(item);
        return this;
    }

    @Nonnull
    public GUI removeItem(@Nonnull GUIItem item) {
        return removeItems(item);
    }

    @Nonnull
    public GUIItem[] getContents() {
        GUIItem[] items = new GUIItem[getSize()];
        for (int i = 0; i < getSize(); i++) items[i] = getItem(i);
        return items;
    }

    @Nonnull
    public List<ItemStack> items() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < getSize(); i++) {
            GUIItem item = getItem(i);
            if (item != null && item.getBuilder().hasValue()) items.add(item.getBuilder().nonnull().build());
            else items.add(new ItemStack(Material.AIR));
        }
        return items;
    }

    @Nonnull
    public GUI setContents(@Nonnull GUIItem[] items) {
        clear();
        for (int i = 0; i < items.length; i++) {
            setItem(i, items[i]);
        }
        return this;
    }

    public boolean contains(@Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return contains(item.getBuilder().nonnull());
        return contains(Material.AIR);
    }

    public boolean contains(@Nonnull TNLItem item) {
        return contains(item.getItemStack());
    }

    public boolean contains(@Nonnull Material material) {
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(material) && TNLItemType.isAir(content)
                    || (content != null && content.getBuilder().hasValue()
                    && material.equals(content.getBuilder().nonnull().getType()))) {
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
            if (TNLItemType.isAir(itemStack) && TNLItemType.isAir(content)) return true;
            else {
                if (ignoreAmount) {
                    if (content != null && content.getBuilder().hasValue()) {
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(content.getBuilder().nonnull().getAmount());
                        if (clone.equals(content.getBuilder().nonnull().build())) return true;
                    }
                } else if (content != null && content.getBuilder().hasValue()
                        && itemStack.equals(content.getBuilder().nonnull().build())) return true;
            }
        }
        return false;
    }

    public boolean containsAtLeast(@Nonnull GUIItem item, int amount) {
        if (item.getBuilder().hasValue()) return containsAtLeast(item.getBuilder().nonnull(), amount);
        else return containsAtLeast(new ItemStack(Material.AIR), amount);
    }

    public boolean containsAtLeast(@Nonnull TNLItem item, int amount) {
        return containsAtLeast(item.getItemStack(), amount);
    }

    public boolean containsAtLeast(@Nonnull Material material, int amount) {
        int finalAmount = 0;
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(material) && TNLItemType.isAir(content)) return true;
            else {
                if (content != null && content.getBuilder().hasValue()) {
                    if (material.equals(content.getBuilder().nonnull().getType())) {
                        if (content.getBuilder().nonnull().getAmount() >= amount) return true;
                        else finalAmount += content.getBuilder().nonnull().getAmount();
                    }
                }
            }
        }
        return finalAmount >= amount;
    }

    public boolean containsAtLeast(@Nonnull ItemStack itemStack, int amount) {
        int finalAmount = 0;
        for (@Nullable GUIItem content : getContents()) {
            if (TNLItemType.isAir(itemStack) && TNLItemType.isAir(content)) return true;
            else {
                if (content != null && content.getBuilder().hasValue()) {
                    ItemStack clone = itemStack.clone();
                    clone.setAmount(content.getBuilder().nonnull().getAmount());
                    if (clone.equals(content.getBuilder().nonnull().build())) {
                        if (clone.getAmount() >= amount) return true;
                        else finalAmount += clone.getAmount();
                    }
                }
            }
        }
        return finalAmount >= amount;
    }

    @Nonnull
    public HashMap<Integer, GUIItem> all(@Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return all(item.getBuilder().nonnull());
        else return all(Material.AIR);
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
            if (TNLItemType.isAir(contents[i]) && TNLItemType.isAir(material)
                    || (contents[i] != null && contents[i].getBuilder().hasValue()
                    && contents[i].getBuilder().nonnull().getType().equals(material))) {
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
            GUIItem content = contents[slot];
            if (TNLItemType.isAir(content) && TNLItemType.isAir(itemStack.getType())) {
                items.put(slot, content);
            } else {
                if (itemStack.getType().equals(content.getBuilder().hasValue() ? content.getBuilder().nonnull().getType() : null)) {
                    if (ignoreAmount) {
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(content.getBuilder().nonnull().getAmount());
                        if (clone.equals(content.getBuilder().nonnull().build())) {
                            items.put(slot, content);
                        }
                    } else {
                        if (itemStack.equals(content.getBuilder().nonnull().build())) {
                            items.put(slot, content);
                        }
                    }
                }
            }
        }
        return items;
    }

    public int first(@Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return first(item.getBuilder().nonnull());
        return first(Material.AIR);
    }

    public int first(@Nonnull TNLItem item) {
        return first(item.getItemStack());
    }

    public int first(@Nonnull Material material) {
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < getSize(); slot++) {
            GUIItem content = contents[slot];
            if (TNLItemType.isAir(content) && TNLItemType.isAir(material)) {
                return slot;
            }
            if (content.getBuilder().hasValue() && material.equals(content.getBuilder().nonnull().getType())) {
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
            GUIItem content = contents[slot];
            if (TNLItemType.isAir(content) && TNLItemType.isAir(itemStack.getType())) {
                return slot;
            } else {
                if (content.getBuilder().hasValue() && itemStack.getType().equals(content.getBuilder().nonnull().getType())) {
                    if (ignoreAmount) {
                        ItemStack clone = itemStack.clone();
                        clone.setAmount(content.getBuilder().nonnull().getAmount());
                        if (clone.equals(content.getBuilder().nonnull().build())) {
                            return slot;
                        }
                    } else {
                        if (itemStack.equals(content.getBuilder().nonnull().build())) {
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
            GUIItem content = contents[i];
            if (content == null || !content.getBuilder().hasValue() || content.getBuilder().nonnull().isAir()) {
                return i;
            }
        }
        return -1;
    }

    @Nonnull
    public Pair<Boolean, Integer> remove(@Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return remove(item.getBuilder().nonnull().getItemStack());
        return remove(Material.AIR);
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
        for (int i = 0; i < contents.length; i++) {
            GUIItem content = contents[i];
            if ((TNLItemType.isAir(item) && TNLItemType.isAir(content))
                    || (content != null && content.getBuilder().hasValue()
                    && content.getBuilder().nonnull().build().equals(item))) {
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
        for (int slot = 0; slot < contents.length; slot++) {
            GUIItem content = contents[slot];
            if ((TNLItemType.isAir(material) && TNLItemType.isAir(content))
                    && (content != null && content.getBuilder().hasValue()
                    && content.getBuilder().nonnull().getType().equals(material))) {
                remove(slot);
                removed = true;
                amount++;
            }
        }
        return new Pair<>(removed, amount);
    }

    @Nonnull
    public GUI remove(int i) {
        getContentHashMap().remove(i);
        return this;
    }

    @Nonnull
    public GUI clear() {
        getContentHashMap().clear();
        return this;
    }

    @Nonnull
    @Override
    public Iterator<GUIItem> iterator() {
        return new GUIIterator(this);
    }

    @Nonnull
    public ListIterator<GUIItem> iterator(int i) {
        return new GUIIterator(this, i);
    }

    private int firstPartial(@Nonnull GUIItem item) {
        if (item.getBuilder().hasValue()) return firstPartial(item.getBuilder().nonnull());
        return firstPartial(Material.AIR);
    }

    private int firstPartial(@Nonnull TNLItem item) {
        return firstPartial(item.getItemStack());
    }

    private int firstPartial(@Nonnull ItemStack itemStack) {
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < contents.length; slot++) {
            GUIItem item = contents[slot];
            if (TNLItemType.isAir(itemStack) && TNLItemType.isAir(item)) return slot;
            if (item != null && item.getBuilder().hasValue() && item.getBuilder().nonnull().getAmount()
                    < item.getBuilder().nonnull().getMaxStackSize()
                    && item.getBuilder().nonnull().build().equals(itemStack)) {
                return slot;
            }
        }
        return -1;
    }

    public int firstPartial(@Nonnull Material material) {
        GUIItem[] contents = getContents();
        for (int slot = 0; slot < contents.length; slot++) {
            GUIItem item = contents[slot];
            if (TNLItemType.isAir(material) && TNLItemType.isAir(item)) return slot;
            if (item != null && item.getBuilder().hasValue() && item.getBuilder().nonnull().getType() == material
                    && item.getBuilder().nonnull().getAmount() < item.getBuilder().nonnull().getMaxStackSize()) {
                return slot;
            }
        }
        return -1;
    }

    public interface ClickEvent {
        @Info(value = "if return value is true the event is cancelled (true by default)")
        boolean onClick(@Nonnull TNLPlayer player, int slot, @Nonnull Interaction.Type type);
    }
}
