package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.t3.ld40.world.components.FollowComponent;

public class FollowCameraSystem extends IteratingSystem {

    ComponentMapper<FollowComponent> cFollow;
    OrthographicCamera camera;
    Entity entity;

    public FollowCameraSystem(final OrthographicCamera camera) {
        super(Family.all(FollowComponent.class).get());
        cFollow = ComponentMapper.getFor(FollowComponent.class);
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.entity = entity;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if(entity != null){
            FollowComponent follow = cFollow.get(entity);
            camera.position.set(follow.object.cPos.position);
        }
    }
}
