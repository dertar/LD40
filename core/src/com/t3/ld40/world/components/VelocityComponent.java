package com.t3.ld40.world.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 velocity = new Vector2();

    public boolean isActive = false;
    public boolean impulse = false;

    public VelocityComponent(float x, float y) {
        velocity.x = x;
        velocity.y = y;

    }
}