package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.t3.ld40.Options;

public class PhysicsDebugSystem extends IteratingSystem {
    Box2DDebugRenderer render;
    OrthographicCamera camera;
    Matrix4 scale;
    World world;

    public PhysicsDebugSystem(World world, OrthographicCamera camera) {
        super(Family.all().get());
        this.camera = camera;
        render = new Box2DDebugRenderer();
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        scale = new Matrix4 (camera.combined);
        scale.scale(Options.PPM,Options.PPM, 1f);
        render.render(world, scale);

    }
}
