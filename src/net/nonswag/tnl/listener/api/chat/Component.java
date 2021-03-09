package net.nonswag.tnl.listener.api.chat;

import javax.annotation.Nonnull;

public class Component {

    public enum HoverEvent {
        CUSTOM_TEXT("show_text"),
        ITEM_OR_BLOCK("show_item"),
        PLAYER_MOB_OR_ENTITY("show_entity"),
        ACHIEVEMENT("achievement"),
        STATISTIC("statistic");

        @Nonnull
        private final String event;

        HoverEvent(@Nonnull String event) {
            this.event = event;
        }

        @Nonnull
        public String getEvent() {
            return event;
        }

        @Override
        public String toString() {
            return "ClickEvent{" +
                    "event='" + event + '\'' +
                    '}';
        }
    }

    public enum KeyBind {
        WALK_FORWARD("key.forward"),
        WALK_BACKWARD("key.back"),
        STRAFE_LEFT("key.left"),
        STRAFE_RIGHT("key.right"),
        JUMP("key.jump"),
        SNEAK("key.sneak"),
        SPRINT("key.sprint"),
        OPEN_CLOSE_INVENTORY("key.inventory"),
        ATTACK_DESTROY("key.attack"),
        USE_ITEM_PLACE_BLOCK("key.use"),
        PICK_BLOCK("key.pickItem"),
        DROP_SELECTED_ITEM("key.drop"),
        SWAP_ITEM_IN_HANDS("key.swapHands"),
        HOTBAR_SLOT_OBE("key.hotbar.1"),
        HOTBAR_SLOT_TWO("key.hotbar.2"),
        HOTBAR_SLOT_THREE("key.hotbar.3"),
        HOTBAR_SLOT_FOUR("key.hotbar.4"),
        HOTBAR_SLOT_FIVE("key.hotbar.5"),
        HOTBAR_SLOT_SIX("key.hotbar.6"),
        HOTBAR_SLOT_SEVEN("key.hotbar.7"),
        HOTBAR_SLOT_EIGHT("key.hotbar.8"),
        HOTBAR_SLOT_NINE("key.hotbar.9"),
        SAVE_TOOLBAR("key.saveToolbarActivator"),
        LOAD_TOOLBAR("key.loadToolbarActivator"),
        LIST_PLAYERS("key.playerlist"),
        OPEN_CHAT("key.chat"),
        OPEN_COMMAND("key.command"),
        HIGHLIGHT_PLAYERS("key.spectatorOutlines"),
        TAKE_SCREENSHOT("key.screenshot"),
        TOGGLE_CINEMATIC_CAMERA("key.smoothCamera"),
        TOGGLE_FULLSCREEN("key.fullscreen"),
        TOGGLE_PERSPECTIVE("key.togglePerspective");

        @Nonnull
        private final String tag;

        KeyBind(@Nonnull String tag) {
            this.tag = tag;
        }

        @Nonnull
        public String getTag() {
            return tag;
        }

        @Override
        public String toString() {
            return "KeyBind{" +
                    "tag='" + tag + '\'' +
                    '}';
        }
    }
    public enum ClickEvent {
        RUN_COMMAND("run_command"),
        SUGGEST_COMMAND("suggest_command"),
        SUGGEST_TEXT("insertion"),
        OPEN_URL("open_url"),
        COPY_A_TEXT_TO_CLIPBOARD("copy_to_clipboard");

        @Nonnull
        private final String event;

        ClickEvent(@Nonnull String event) {
            this.event = event;
        }

        @Nonnull
        public String getEvent() {
            return event;
        }

        @Override
        public String toString() {
            return "ClickEvent{" +
                    "event='" + event + '\'' +
                    '}';
        }
    }
}
