package net.nonswag.tnl.listener.api.file;

import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.io.File;

public interface Config {

    @Nonnull
    File getFile();

    @Nonnull
    JsonElement getJsonElement();

    void save();
}
