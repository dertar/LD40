package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.t3.ld40.Options;
import com.t3.ld40.world.Spawner;
import com.t3.ld40.world.components.BodyComponent;
import com.t3.ld40.world.components.MainShipComponent;
import com.t3.ld40.world.components.PositionComponent;
import com.t3.ld40.world.gameoobjects.Asteroid;
import com.t3.ld40.world.gameoobjects.Ship;

import java.lang.management.OperatingSystemMXBean;

public class GeneratorSystem extends IteratingSystem {

    Spawner spawner;
    Entity entity;

    ComponentMapper<PositionComponent> cPos;
    ComponentMapper<MainShipComponent> cShip;
    float lastH = 0f;
    float deltaH = 40f;
    public GeneratorSystem(Spawner spawner) {
        super(Family.all(PositionComponent.class, MainShipComponent.class).get());
        cPos = ComponentMapper.getFor(PositionComponent.class);
        cShip = ComponentMapper.getFor(MainShipComponent.class);
        this.spawner = spawner;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        this.entity = entity;
    }

    @Override
    public void update(float delta){
        super.update(delta);

        PositionComponent pos = cPos.get(entity);

        if(pos.position.y - deltaH >= lastH){
            Gdx.app.debug(pos.position.y + "", lastH + "");
            lastH = generate(new Vector2(pos.position.x,pos.position.y), new Vector2(Options.orthoWidth, Options.orthoHeight), 25, 125, 80f, 20f);
        }

    }

    float generate(Vector2 posFrom, Vector2 posTo, float minGap, float maxGap, float chanceToAsteroids, float chanceToModule){

        float h = posFrom.y;
        while(h < posFrom.y + posTo.y){

            float dice = MathUtils.random(chanceToAsteroids + chanceToModule);

            float nextIncH = MathUtils.random(minGap, maxGap);
            h += nextIncH;
            Gdx.app.debug(dice + "", "");
            if(dice <= chanceToAsteroids){
                Asteroid asteroid = spawner.createAsteroid(MathUtils.random(0, posTo.x), h, MathUtils.random(360f) );
                spawner.newAstaroids.add(asteroid);
            }else {
                Ship ship = spawner.createShip(MathUtils.random(0, posTo.x), h, MathUtils.random(360f) );
                spawner.newShips.add(ship);
            }
        }

        return h;
    }


}
