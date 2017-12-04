package com.t3.ld40.world.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    public TextureRegion textureRegion;

    public TextureComponent(final TextureRegion textureRegion)
    {
        this.textureRegion = textureRegion;
    }

    public TextureComponent()
    {
    }
}
