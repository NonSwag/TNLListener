package net.nonswag.tnl.listener.api.title;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Title{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", timeStay=" + timeStay +
                ", timeIn=" + timeIn +
                ", timeOut=" + timeOut +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
        return timeStay == title1.timeStay && timeIn == title1.timeIn && timeOut == title1.timeOut && title.equals(title1.title) && subtitle.equals(title1.subtitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle, timeStay, timeIn, timeOut);
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

        @Override
        public String toString() {
            return "Animation{" +
                    "title=" + title +
                    ", design=" + design +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Animation animation = (Animation) o;
            return title.equals(animation.title) && design == animation.design;
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, design);
        }
    }

    public static class Design {
        @Nonnull
        public static final Design LIGHT = new Design(ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD);
        @Nonnull
        public static final Design DARK = new Design(ChatColor.RED, ChatColor.DARK_GRAY, ChatColor.DARK_RED);
        @Nonnull
        public static final Design OCEAN = new Design(ChatColor.AQUA, ChatColor.GRAY, ChatColor.BLUE);

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

        @Override
        public String toString() {
            return "Design{" +
                    "primaryColor=" + primaryColor +
                    ", secondaryColor=" + secondaryColor +
                    ", extraColor=" + extraColor +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Design design = (Design) o;
            return primaryColor == design.primaryColor && secondaryColor == design.secondaryColor && extraColor == design.extraColor;
        }

        @Override
        public int hashCode() {
            return Objects.hash(primaryColor, secondaryColor, extraColor);
        }
    }
}
