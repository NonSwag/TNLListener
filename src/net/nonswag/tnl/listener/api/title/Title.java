package net.nonswag.tnl.listener.api.title;

import org.bukkit.ChatColor;

import java.util.Objects;

public class Title {

    private String title;
    private String subtitle;
    private int timeStay;
    private int timeIn;
    private int timeOut;

    public Title(Object title,
                 Object subtitle,
                 int timeStay,
                 int timeIn,
                 int timeOut) {
        this.title = title.toString();
        this.subtitle = subtitle.toString();
        this.timeStay = timeStay;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public Title(Object title,
                 Object subtitle) {
        this(title, subtitle, 70, 0, 10);
    }

    public Title() {
        this("§7-§8/§7-", "§7-§8/§7-");
    }

    public String getTitle() {
        return title;
    }

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

    public Title setTitle(String title) {
        this.title = title;
        return this;
    }

    public Title setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public Title setTimeStay(int timeStay) {
        this.timeStay = timeStay;
        return this;
    }

    public Title setTimeIn(int timeIn) {
        this.timeIn = timeIn;
        return this;
    }

    public Title setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public Animation animate(Design design) {
        return new Animation(this, design);
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

        private final Title title;
        private final Design design;

        public Animation(Title title,
                         Design design) {
            this.title = title;
            this.design = design;
        }

        public Title getTitle() {
            return title;
        }

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

    public enum Design {
        LIGHT(ChatColor.GREEN, ChatColor.GRAY, ChatColor.GOLD),
        DARK(ChatColor.RED, ChatColor.DARK_GRAY, ChatColor.DARK_RED),
        OCEAN(ChatColor.AQUA, ChatColor.GRAY, ChatColor.BLUE),
        ;

        private final ChatColor primaryColor;
        private final ChatColor secondaryColor;
        private final ChatColor extraColor;

        Design(ChatColor primaryColor,
               ChatColor secondaryColor,
               ChatColor extraColor) {
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.extraColor = extraColor;
        }

        public ChatColor getPrimaryColor() {
            return primaryColor;
        }

        public ChatColor getSecondaryColor() {
            return secondaryColor;
        }

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
    }
}
