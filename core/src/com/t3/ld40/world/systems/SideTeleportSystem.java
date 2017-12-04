package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.t3.ld40.Box2DScale;
import com.t3.ld40.world.components.BodyComponent;


public class SideTeleportSystem extends IteratingSystem {

    ComponentMapper<BodyComponent> mapper;
    Array<Entity> entities;
    float left,right;

    public SideTeleportSystem(float left, float right) {
        super(Family.all(BodyComponent.class).get());
        this.left = Box2DScale.toReal(left);
        this.right = Box2DScale.toReal(right);
        mapper =  ComponentMapper.getFor(BodyComponent.class);
        entities = new Array<Entity>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entities.add(entity);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        for(Entity entity : entities){
            BodyComponent cBody = mapper.get(entity);
            Body body = cBody.body;

            if(body.getPosition().x < left){
                body.setTransform(right, body.getPosition().y, body.getAngle());
            } else if(body.getPosition().x > right){
                body.setTransform(left, body.getPosition().y, body.getAngle());
            }
        }
        entities.clear();
    }
}
