package com.ecato.bugslabirint.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ecato.bugslabirint.OurGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Bug's labirint";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new OurGame(), config);
	}
}
