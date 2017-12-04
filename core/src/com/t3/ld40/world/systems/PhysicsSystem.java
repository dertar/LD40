package com.t3.ld40.world.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.t3.ld40.Box2DScale;
import com.t3.ld40.Options;
import com.t3.ld40.world.components.BodyComponent;
import com.t3.ld40.world.components.PositionComponent;
import com.t3.ld40.world.components.VelocityComponent;
import com.t3.ld40.world.gameoobjects.*;
import javafx.util.Pair;

public class PhysicsSystem extends IteratingSystem {
    public static final short BIT_SHIP = 0x0001;
    public static final short BIT_ASTEROIDS = 0x0002;

    Array<Entity> queue;

    ComponentMapper<BodyComponent> cBody = ComponentMapper.getFor(BodyComponent.class);
    ComponentMapper<PositionComponent> cPos = ComponentMapper.getFor(PositionComponent.class);
    ComponentMapper<VelocityComponent> cVel = ComponentMapper.getFor(VelocityComponent.class);

    private float accumulator = 0f;
    public World world;

    ObjectMap<Pair<Body, Body>, Vector2[]> contactedToJoin = new ObjectMap<Pair<Body, Body>, Vector2[]>();

    Array<WeldJoint> jointsBetweenShips = new Array<WeldJoint>();
    Array<Pair<Body,Body>> removeObject = new Array<Pair<Body, Body>>();

    public PhysicsSystem() {
        super(Family.all(BodyComponent.class, PositionComponent.class, VelocityComponent.class).get());
        world = new World(Options.gravity,true);

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Pair<Body,Body> pair = new Pair<Body, Body>(contact.getFixtureA().getBody(), contact.getFixtureB().getBody());


                if((pair.getKey().getUserData().getClass().equals(ShipMain.class) || pair.getKey().getUserData().getClass().equals(ShipExtra.class)) &&
                        (pair.getValue().getUserData().getClass().equals(ShipMain.class) || pair.getValue().getUserData().getClass().equals(ShipExtra.class))){
                    contactedToJoin.put(pair,contact.getWorldManifold().getPoints());

                    GameObject a = (GameObject)pair.getKey().getUserData(),
                            b = (GameObject)pair.getValue().getUserData();

                    if(a.canBeClicked || b.canBeClicked){
                        a.canBeClicked = b.canBeClicked = true;
                    }
                }

                if((pair.getKey().getUserData().getClass().equals(Asteroid.class) || pair.getValue().getUserData().getClass().equals(Asteroid.class)) &&
                        (pair.getValue().getUserData().getClass().equals(ShipMain.class) || pair.getValue().getUserData().getClass().equals(ShipExtra.class) ||
                                pair.getKey().getUserData().getClass().equals(ShipMain.class) || pair.getKey().getUserData().getClass().equals(ShipExtra.class))){

                    removeObject.add(pair);
                }



            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        queue = new Array<Entity>();
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        if(accumulator >= Options.updatePhysicsStep) {
            if(contactedToJoin.size != 0){
                for(ObjectMap.Entry<Pair<Body,Body>, Vector2[]> entry: contactedToJoin){
                    WeldJointDef jointDef = new WeldJointDef();

                    for(Vector2 vec : entry.value) {
                        jointDef.initialize(entry.key.getKey(), entry.key.getValue(),  vec);
                        jointsBetweenShips.add((WeldJoint)world.createJoint(jointDef));
                    }

                }
                contactedToJoin.clear();
            }

            if(removeObject.size != 0){
                for(Pair<Body,Body> entry: removeObject){
                    world.destroyBody(entry.getKey());
                    world.destroyBody(entry.getValue());
                }
                removeObject.clear();
            }

            world.step(Options.updatePhysicsStep, 1, 1);

            for(Entity entity : queue){
                BodyComponent body = cBody.get(entity);
                PositionComponent pos = cPos.get(entity);
                VelocityComponent vel = cVel.get(entity);

//                if(vel.isActive)
    {
                    Vector2 bodyVel = body.body.getLinearVelocity();
                    Vector2 direction = new Vector2();
                    float rotation = body.body.getAngle();
                    direction.x = (float) Math.cos(rotation);
                    direction.y = (float) Math.sin(rotation);

                    direction = direction.nor();

                    if(vel.impulse){
                        if (bodyVel.x < vel.velocity.x){
                            body.body.applyLinearImpulse(new Vector2(direction.x, 0),
                                    body.body.getWorldPoint(new Vector2(0,0)), true);

                        }

                        if (bodyVel.y < vel.velocity.y){
                            body.body.applyLinearImpulse(new Vector2(0, direction.y),
                                    body.body.getWorldPoint(new Vector2(0,0)), true);
                        }
                        vel.impulse = false;
                    }

                }
                Vector2  realPos = body.body.getPosition();

                //Gdx.app.debug(realPos.x + "", realPos.y + "");
                pos.position.x = Box2DScale.toPixels(realPos.x);
                pos.position.y = Box2DScale.toPixels(realPos.y);

                pos.rotation = body.body.getAngle();
            }
            accumulator = 0f;
            queue.clear();
        }

    }

    public Body createBody(float x, float y, BodyDef.BodyType type, Shape shape,
                           boolean sleep, boolean bullet, short catBits, short maskBits){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;

        bodyDef.position.set(x,y);

        Body ret = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.05f;

        fixtureDef.filter.categoryBits = catBits;
        fixtureDef.filter.maskBits = maskBits;

        ret.createFixture(fixtureDef);
        ret.setSleepingAllowed(sleep);
        ret.setBullet(bullet);

        shape.dispose();

        return ret;
    }

    public Shape createCircle(float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius (radius);
        return shape;
    }

    public Shape createRect(float width, float height){
        PolygonShape shape = new PolygonShape();
//        shape.set(new float[]{
//                0,0,
//                width,0,
//                width,height,
//                0,height
//        });
        shape.setAsBox(width/2f, height/2f);

        return shape;
    }
}
