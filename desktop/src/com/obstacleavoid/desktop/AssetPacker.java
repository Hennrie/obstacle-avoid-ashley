package com.obstacleavoid.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {

    public static final boolean DRAW_DEBUG_OUTLINE = false;

    public static final String RAW_ASSETS_PATH = "desktop/assets-raw";
    public static final String ASSETS_PATH = "android/assets";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.debug = DRAW_DEBUG_OUTLINE;


        TexturePacker.process(settings,
                RAW_ASSETS_PATH + "/gameplay",
                ASSETS_PATH + "/gameplay",
                "gameplay");

        TexturePacker.process(settings,
                RAW_ASSETS_PATH + "/skin",
                ASSETS_PATH + "/ui",
                "uiskin");

    }


}
