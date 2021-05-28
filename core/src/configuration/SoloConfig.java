package configuration;

import infra.app.Game;

public class SoloConfig extends MainConfig{
    @Override
    protected void configure() {
        super.configure();
        bind(Game.class).asEagerSingleton();
    }
}
