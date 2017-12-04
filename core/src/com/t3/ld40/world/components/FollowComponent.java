package com.t3.ld40.world.components;

import com.badlogic.ashley.core.Component;
import com.t3.ld40.world.gameoobjects.GameObject;

public class FollowComponent implements Component{
    public GameObject object;

    public FollowComponent(GameObject object){
        this.object = object;
    }
}
