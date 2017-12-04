package com.t3.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;


public class Options {
    static final public Vector2 gravity = new Vector2(0f,0f);
    static final public float PPM = 10f;
    static final public float updatePhysicsStep = 1/60f;

    static public float width = 800f;
    static public float height = 480f;

    static public float orthoWidth = width / 2.0f;
    static public float orthoHeight = height / 2.0f;

    static public float realWidth = Gdx.graphics.getWidth();
    static public float realHeight = Gdx.graphics.getHeight();

    static public float aspectRatio = realWidth / realHeight;
}
