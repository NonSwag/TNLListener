package net.nonswag.tnl.listener.api.actionbarAPI;

public class ActionBar {

    private String text;

    public ActionBar(Object text) {
        this.text = text.toString();
    }

    public ActionBar() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
