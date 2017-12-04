package com.t3.ld40.world.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionComponent implements Component {
    public Vector3 position;
    public float rotation;

    public PositionComponent (float x, float y, float layer) {
        position = new Vector3 (x, y, layer);
    }
}
