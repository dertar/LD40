package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.t3.ld40.Options;
import com.t3.ld40.world.comparators.LayerZComparator;
import com.t3.ld40.world.components.PositionComponent;
import com.t3.ld40.world.components.TextureComponent;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {
    public SpriteBatch spriteBatch;
    public OrthographicCamera camera;
    private Array<Entity> queue;

    private ComponentMapper<TextureComponent> cTexture;
    private ComponentMapper<PositionComponent> cPosition;
    Comparator<Entity> comparator = new LayerZComparator();

    public RenderSystem ()
    {
        super(Family.all(PositionComponent.class, TextureComponent.class).get(), new LayerZComparator());
        this.spriteBatch = new SpriteBatch();
        queue = new Array<Entity>();
        camera = new OrthographicCamera();

        camera.setToOrtho(false, Options.orthoWidth, Options.orthoHeight);

        cTexture = ComponentMapper.getFor(TextureComponent.class);
        cPosition = ComponentMapper.getFor(PositionComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        camera.update();

        queue.sort(comparator);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for(Entity entity : queue){
            TextureComponent texture = cTexture.get(entity);
            PositionComponent pos = cPosition.get(entity);
            if(texture.textureRegion != null){
                float height = texture.textureRegion.getRegionHeight();
                float width = texture.textureRegion.getRegionWidth();

                spriteBatch.draw (texture.textureRegion, pos.position.x - width/2, pos.position.y - height/2, width/2, height/2, width,
                        height, 1.0f, 1.0f,(float) Math.toDegrees(pos.rotation));
            }
        }
        spriteBatch.end();
        queue.clear();
    }
}
