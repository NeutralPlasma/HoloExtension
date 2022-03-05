package eu.virtusdevelops.holoextension.leaderboards.modules;

import eu.virtusdevelops.holoextension.storage.DataStorage;

public interface DefaultModule {

    void init();
    void tick();
    String getName();
}
