package net.nonswag.tnl.listener.v1_14_R1.api.title;

import com.sun.istack.internal.NotNull;
import org.bukkit.entity.Player;

public class Title {

    String title = "";
    String subtitle = "";
    @NotNull
    Player player;
    int timeStay = 70;
    int timeIn = 0;
    int timeOut = 10;

    public Title(@NotNull Player player) {

        this.player = player;

    }

    private int getTimeIn() {

        return timeIn;

    }

    public Title setTimeIn(int timeIn) {

        this.timeIn = timeIn;
        return this;

    }

    private int getTimeOut() {

        return timeOut;

    }

    public Title setTimeOut(int timeOut) {

        this.timeOut = timeOut;
        return this;

    }

    private int getTimeStay() {

        return timeStay;

    }

    public Title setTimeStay(int timeStay) {

        this.timeStay = timeStay;
        return this;

    }

    private Player getPlayer() {

        return player;

    }

    public Title setPlayer(@NotNull Player player) {

        this.player = player;
        return this;

    }

    private String getSubtitle() {

        return subtitle;

    }

    public Title setSubtitle(Object subtitle) {

        this.subtitle = subtitle + "";
        return this;

    }

    private String getTitle() {

        return title;

    }

    public Title setTitle(Object title) {

        this.title = title + "";
        return this;

    }

    public void send() {

        this.player.sendTitle(getTitle(), getSubtitle(), getTimeIn(), getTimeStay(), getTimeOut());

    }

}
