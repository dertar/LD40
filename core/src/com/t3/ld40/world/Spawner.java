package com.t3.ld40.world;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.t3.ld40.Assets;
import com.t3.ld40.Box2DScale;
import com.t3.ld40.world.components.FollowComponent;
import com.t3.ld40.world.gameoobjects.*;
import com.t3.ld40.world.systems.PhysicsSystem;

import java.util.ArrayList;

public class Spawner {

    PhysicsSystem physicsSystem;
    Assets assets;
    public Array<Ship> newShips = new Array<Ship>();


    public Array<Asteroid> newAstaroids = new Array<Asteroid>();
    public Array<Asteroid> astaroids = new Array<Asteroid>();

    public ShipMain ship;
    public Array<GameObject> ships = new Array<GameObject>();

    public Spawner(final PhysicsSystem physicsSystem, final Assets assets){
        this.physicsSystem = physicsSystem;
        this.assets = assets;

    }

    public Asteroid createAsteroid(float x, float y, float rotation){
        Asteroid asteroid = new Asteroid(physicsSystem.createBody(Box2DScale.toReal(x),Box2DScale.toReal(y),
                BodyDef.BodyType.DynamicBody,
                physicsSystem.createCircle(1.6f),false,false, PhysicsSystem.BIT_SHIP, (short)-1)
        );
        asteroid.cBody.body.setUserData(asteroid);
        asteroid.cBody.body.setTransform(asteroid.cBody.body.getPosition(), (float)Math.toRadians(rotation));
        asteroid.cTexture.textureRegion = assets.textureAtlas.findRegion("asteroid");

        return asteroid;
    }

    public Ship createShip(float x, float y, float rotation){
        Ship ship = new ShipExtra(physicsSystem.createBody(Box2DScale.toReal(x),Box2DScale.toReal(y),
                BodyDef.BodyType.DynamicBody,
                physicsSystem.createRect(Box2DScale.toReal(32f),Box2DScale.toReal(32f)),false,false, PhysicsSystem.BIT_SHIP, (short)-1)
        );
        ship.cBody.body.setUserData(ship);
        ship.cBody.body.setTransform(ship.cBody.body.getPosition(), (float)Math.toRadians(rotation));
        ship.cTexture.textureRegion = assets.textureAtlas.findRegion("ship_extra");

        return ship;
    }

    public Ship newGame() {

        ships.clear();
         ship = new ShipMain(
                physicsSystem.createBody(Box2DScale.toReal(110f),Box2DScale.toReal(100f),
                        BodyDef.BodyType.DynamicBody,
                        physicsSystem.createRect(Box2DScale.toReal(32f),Box2DScale.toReal(32f)),false,false, PhysicsSystem.BIT_SHIP , (short)-1));
        ship.cVel.isActive = true;
        ship.canBeClicked = true;

        ship.cTexture.textureRegion = assets.textureAtlas.findRegion("ship_main");
        ship.entity.add(new FollowComponent(ship));

        ship.cBody.body.setUserData(ship);

        ship.cBody.body.setTransform(ship.cBody.body.getPosition(), (float)Math.toRadians(90));


        newShips.add(createShip(78f, 150f, 0f));
        newShips.add(createShip(110f, 150f, 270f));
        newShips.add(createShip(142f, 150f, 180f));

        return ship;
    }

}
