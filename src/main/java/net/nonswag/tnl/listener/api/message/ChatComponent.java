package net.nonswag.tnl.listener.api.message;

import net.nonswag.tnl.listener.api.logger.Logger;
import net.nonswag.tnl.listener.api.player.TNLPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChatComponent {

    @Nonnull
    private static final List<ChatComponent> messages = new ArrayList<>();

    @Nonnull
    private final LanguageKey languageKey;
    @Nonnull
    private String text = "";

    public ChatComponent(@Nonnull LanguageKey languageKey, @Nonnull String text) {
        this.languageKey = languageKey;
        setText(text);
        if (!getMessages().contains(this)) {
            getMessages().add(this);
        }
    }

    @Nonnull
    public LanguageKey getLanguageKey() {
        return languageKey;
    }

    @Nonnull
    public String getText() {
        return text;
    }

    public void setText(@Nonnull String text) {
        this.text = text;
    }

    @Nonnull
    public String getText(@Nullable TNLPlayer player, @Nonnull Placeholder... placeholders) {
        return ChatComponent.getText(this.text, player, placeholders);
    }

    @Nonnull
    public String getText(@Nonnull Placeholder... placeholders) {
        return this.getText((TNLPlayer) null, placeholders);
    }

    @Nonnull
    public static String getText(@Nonnull String text, @Nullable TNLPlayer player, @Nonnull Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            text = text.replace("%" + placeholder.getPlaceholder() + "%", placeholder.getObject().toString());
        }
        for (String value : Placeholder.Registry.values()) {
            Placeholder placeholder = Placeholder.Registry.valueOf(value);
            if (placeholder != null) {
                text = text.replace("%" + placeholder.getPlaceholder() + "%", placeholder.getObject().toString());
            } else Logger.error.println("Cannot find placeholder <'" + value + "'> but it is registered");
        }
        if (player != null) for (Placeholder.Formulary formulary : Placeholder.Registry.formularies()) {
            Placeholder check = formulary.check(player);
            text = text.replace("%" + check.getPlaceholder() + "%", check.getObject().toString());
        }
        return text;
    }

    @Nonnull
    public static String getText(@Nonnull String text, @Nonnull Placeholder... placeholders) {
        return getText(text, null, placeholders);
    }

    @Nonnull
    public static List<ChatComponent> getMessages() {
        return messages;
    }
}
