package net.nonswag.tnl.listener.api.title;

import java.util.Objects;

public class Title {

    private String title;
    private String subtitle;
    private int timeStay;
    private int timeIn;
    private int timeOut;

    public Title(Object title, Object subtitle, int timeStay, int timeIn, int timeOut) {
        this.title = title.toString();
        this.subtitle = subtitle.toString();
        this.timeStay = timeStay;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public Title(Object title, Object subtitle) {
        this.title = title.toString();
        this.subtitle = subtitle.toString();
        this.timeStay = 70;
        this.timeIn = 0;
        this.timeOut = 10;
    }

    public Title() {
        this.title = "§7-§8/§7-";
        this.subtitle = "§7-§8/§7-";
        this.timeStay = 70;
        this.timeIn = 0;
        this.timeOut = 10;
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
        return timeStay == title1.timeStay &&
                timeIn == title1.timeIn &&
                timeOut == title1.timeOut &&
                Objects.equals(title, title1.title) &&
                Objects.equals(subtitle, title1.subtitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle, timeStay, timeIn, timeOut);
    }
}
