package com.t3.ld40.world.gameoobjects;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.t3.ld40.world.components.BodyComponent;
import com.t3.ld40.world.components.PositionComponent;
import com.t3.ld40.world.components.TextureComponent;
import com.t3.ld40.world.components.VelocityComponent;

abstract public class GameObject {
    public Entity entity;

    public BodyComponent cBody;
    public TextureComponent cTexture;
    public PositionComponent cPos;
    public VelocityComponent cVel;

    public boolean canBeClicked = false;

    public GameObject(Body body){
        cBody = new BodyComponent(body);
        cTexture = new TextureComponent();
        cPos = new PositionComponent(100f,110f,1f);
        cVel = new VelocityComponent(6f, 6f);
        entity = new Entity();
        entity.add(cBody).add(cTexture).add(cPos).add(cVel);
    }
}
