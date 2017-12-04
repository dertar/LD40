package com.t3.ld40;

public class Box2DScale {

    static public float toReal(float pixels) {
        return pixels / Options.PPM;
    }

    static public float toPixels(float real) {
        return real * Options.PPM;
    }
}
