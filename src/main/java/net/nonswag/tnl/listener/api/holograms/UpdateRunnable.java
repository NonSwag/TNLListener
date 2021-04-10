package net.nonswag.tnl.listener.api.holograms;

import net.nonswag.tnl.listener.Holograms;
import net.nonswag.tnl.listener.api.logger.Logger;

import javax.annotation.Nonnull;

public class UpdateRunnable {

    @Nonnull
    private static final Thread thread;

    static {
        thread = new Thread(() -> {
            try {
                do {
                    for (Hologram hologram : Holograms.getInstance().cachedValues()) {
                        hologram.updateAll();
                    }
                    Thread.sleep(Holograms.getInstance().getUpdateTime());
                } while (true);
            } catch (Exception e) {
                if (!(e instanceof InterruptedException)) {
                    Logger.error.println(e);
                }
            }
        });
        thread.setDaemon(true);
    }

    public static void start() {
        if (!isRunning()) {
            getThread().start();
        }
    }

    public static void stop() {
        if (isRunning()) {
            getThread().interrupt();
        }
    }

    public static void restart() {
        if (isRunning()) {
            stop();
            start();
        }
    }

    public static boolean isRunning() {
        return getThread() != null && getThread().isAlive() && !getThread().isInterrupted();
    }

    public static Thread getThread() {
        return thread;
    }
}
