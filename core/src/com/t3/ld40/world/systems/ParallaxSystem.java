package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.t3.ld40.Options;

public class ParallaxSystem extends IteratingSystem {
    SpriteBatch batch;
    OrthographicCamera camera;
    TextureRegion background;
    public ParallaxSystem(final SpriteBatch batch, final OrthographicCamera camera, TextureRegion background) {
        super(Family.all().get());

        this.batch = batch;
        this.camera = camera;
        this.background = background;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    @Override
    public void update(float delta) {
        super.update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(this.background, 0, 0, Options.orthoWidth, Options.orthoHeight);
        batch.end();
    }
}
