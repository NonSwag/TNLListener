package net.nonswag.tnl.listener.api.title;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

public class Title {

    @Nonnull
    public static final Title EMPTY = new Title("", "", 0, 0, 0);

    @Nonnull
    private String title;
    @Nonnull
    private String subtitle;
    private int timeStay;
    private int timeIn;
    private int timeOut;

    public Title(@Nonnull Object title, @Nonnull Object subtitle, int timeStay, int timeIn, int timeOut) {
        this.title = title.toString();
        this.subtitle = subtitle.toString();
        this.timeStay = timeStay;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public Title(@Nonnull Object title, @Nonnull Object subtitle) {
        this(title, subtitle, 70, 0, 10);
    }

    public Title() {
        this("", "");
    }

    @Nonnull
    public String getTitle() {
        return title;
    }

    @Nonnull
    public String getSubtitle() {
        return subtitle;
    }

    public int getTimeStay() {
        return timeStay;
    }

    public int getTimeIn() {
        return timeIn;
    }

    public int getTimeOut() {
        return timeOut;
    }

    @Nonnull
    public Title setTitle(@Nonnull String title) {
        this.title = title;
        return this;
    }

    @Nonnull
    public Title setSubtitle(@Nonnull String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    @Nonnull
    public Title setTimeStay(int timeStay) {
        this.timeStay = timeStay;
        return this;
    }

    @Nonnull
    public Title setTimeIn(int timeIn) {
        this.timeIn = timeIn;
        return this;
    }

    @Nonnull
    public Title setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    @Nonnull
    public Animation animate(@Nonnull Design design) {
        return new Title.Animation(this, design);
    }

    public static class Animation {

        @Nonnull
        private final Title title;
        @Nonnull
        private final Design design;

        public Animation(@Nonnull Title title, @Nonnull Design design) {
            this.title = title;
            this.design = design;
        }

        @Nonnull
        public Title getTitle() {
            return title;
        }

        @Nonnull
        public Design getDesign() {
            return design;
        }
    }

    public static class Design {
        @Nonnull
        public static final Design LIGHT = new Design(ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD);
        @Nonnull
        public static final Design BLOODY = new Design(ChatColor.DARK_RED, ChatColor.DARK_GRAY, ChatColor.RED);
        @Nonnull
        public static final Design DARK = new Design(ChatColor.RED, ChatColor.DARK_GRAY, ChatColor.GRAY);
        @Nonnull
        public static final Design OCEAN = new Design(ChatColor.DARK_AQUA, ChatColor.GRAY, ChatColor.AQUA);

        @Nonnull
        private final ChatColor primaryColor;
        @Nonnull
        private final ChatColor secondaryColor;
        @Nonnull
        private final ChatColor extraColor;

        public Design(@Nonnull ChatColor primaryColor, @Nonnull ChatColor secondaryColor, @Nonnull ChatColor extraColor) {
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.extraColor = extraColor;
        }

        @Nonnull
        public ChatColor getPrimaryColor() {
            return primaryColor;
        }

        @Nonnull
        public ChatColor getSecondaryColor() {
            return secondaryColor;
        }

        @Nonnull
        public ChatColor getExtraColor() {
            return extraColor;
        }
    }
}
