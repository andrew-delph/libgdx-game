package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import render.MapRender;

public class MapRenderMain {
    public static void main(String[] arg) {

        MapRender mapRender = new MapRender();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();


        config.height = mapRender.height *mapRender.size;
        config.width = mapRender.width *mapRender.size;


        new LwjglApplication(mapRender, config);
    }
}
