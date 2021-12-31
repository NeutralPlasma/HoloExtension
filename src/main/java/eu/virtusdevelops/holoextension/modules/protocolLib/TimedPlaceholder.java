package eu.virtusdevelops.holoextension.modules.protocolLib;

import eu.virtusdevelops.virtuscore.utils.TextUtils;
import me.filoghost.holographicdisplays.api.beta.placeholder.GlobalPlaceholder;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimedPlaceholder implements GlobalPlaceholder {


    private final int interval;

    public TimedPlaceholder(int interval){
        this.interval = interval;
    }

    @Override
    public @Nullable String getReplacement(@Nullable String s) {
        return "";
    }

    @Override
    public int getRefreshIntervalTicks() {
        return interval;
    }
}
