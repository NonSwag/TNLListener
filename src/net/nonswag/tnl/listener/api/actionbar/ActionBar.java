package net.nonswag.tnl.listener.api.actionbar;

import javax.annotation.Nonnull;

public class ActionBar {

    @Nonnull
    private String text = "";

    public ActionBar(@Nonnull Object text) {
        this.text = text.toString();
    }

    public ActionBar() {
    }

    @Nonnull
    public String getText() {
        return text;
    }

    public void setText(@Nonnull String text) {
        this.text = text;
    }
}
