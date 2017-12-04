package com.t3.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    AssetManager manager = new AssetManager();
    public TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("assets.pack"));

}
