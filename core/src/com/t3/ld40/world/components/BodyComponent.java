package com.t3.ld40.world.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

public class BodyComponent implements Component{
    public Body body;

    public BodyComponent(Body b)
    {
        body = b;
    }
}
