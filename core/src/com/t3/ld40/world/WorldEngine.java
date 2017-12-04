package com.t3.ld40.world;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.t3.ld40.Assets;
import com.t3.ld40.Box2DScale;
import com.t3.ld40.Options;
import com.t3.ld40.world.components.FollowComponent;
import com.t3.ld40.world.components.MainShipComponent;
import com.t3.ld40.world.gameoobjects.GameObject;
import com.t3.ld40.world.gameoobjects.ShipExtra;
import com.t3.ld40.world.gameoobjects.ShipMain;
import com.t3.ld40.world.systems.*;

public class WorldEngine implements InputProcessor
{
    PooledEngine engine;
    RenderSystem render;
    PhysicsDebugSystem physicsDebugSystem;
    PhysicsSystem physicsSystem;
    FollowCameraSystem followCameraSystem;
    ParallaxSystem parallaxSystem;
    SideTeleportSystem sideTeleportSystem;
    GeneratorSystem generatorSystem;
    Assets assets;
    Spawner spawner;
    public WorldEngine(Assets assets)
    {
        this.assets = assets;

        engine = new PooledEngine();

        render = new RenderSystem();
        physicsSystem = new PhysicsSystem();
        parallaxSystem = new ParallaxSystem(render.spriteBatch, render.camera, assets.textureAtlas.findRegion("background"));
        sideTeleportSystem = new SideTeleportSystem(0,Options.orthoWidth);
        physicsDebugSystem = new PhysicsDebugSystem(physicsSystem.world,render.camera);
        followCameraSystem = new FollowCameraSystem(render.camera);
        spawner = new Spawner(physicsSystem, assets);
        generatorSystem = new GeneratorSystem(spawner);

        engine.addSystem(parallaxSystem);
        engine.addSystem(render);
        engine.addSystem(physicsSystem);
        engine.addSystem(followCameraSystem);
        engine.addSystem(generatorSystem);
        //engine.addSystem(sideTeleportSystem);
    }

    public void showPhysicsDebug (boolean show) {
        Gdx.app.debug("engine", "physics debug " + show);
        if(show){
            engine.addSystem(physicsDebugSystem);
        }else{
            engine.removeSystem(physicsDebugSystem);
        }
    }

    private void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            showPhysicsDebug(true);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            showPhysicsDebug(false);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            render.camera.zoom -= 0.02;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            render.camera.zoom += 0.02;
        }
    }

    public void update(float delta) {
        handleInput();

        engine.update(delta);
        spawner.ships.addAll(spawner.newShips);

        for(GameObject s : spawner.newShips){
            engine.addEntity( s.entity );

        }
        spawner.newShips.clear();


        spawner.astaroids.addAll(spawner.newAstaroids);

        for(GameObject s : spawner.newAstaroids){
            engine.addEntity( s.entity );
        }
        spawner.newAstaroids.clear();

    }

    public void newGame() {
        spawner.newGame();
        spawner.ship.entity.add(new MainShipComponent());
        engine.addEntity(spawner.ship.entity);


    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    Vector3 worldTouch = new Vector3();
    QueryCallback callback = new QueryCallback() {
        @Override
        public boolean reportFixture(Fixture fixture) {
            if(fixture.testPoint(worldTouch.x,worldTouch.y))
            {
                GameObject ship = (GameObject)fixture.getBody().getUserData();
                if(ship.canBeClicked) {
                    ship.cVel.impulse = true;
                    Gdx.app.debug("clicked", "engine " + ship.cVel.isActive);
                }
            }
            return true;

        }
    };

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        worldTouch.set(screenX, screenY, 0f);
        render.camera.unproject(worldTouch);
        worldTouch.set(Box2DScale.toReal(worldTouch.x), Box2DScale.toReal(worldTouch.y), 0f);

        physicsSystem.world.QueryAABB(callback, worldTouch.x + 0.1f, worldTouch.y + 0.1f,worldTouch.x - 0.1f, worldTouch.y - 0.1f);
        return true;
     }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
