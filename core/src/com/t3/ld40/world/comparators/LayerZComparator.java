package com.t3.ld40.world.comparators;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.t3.ld40.world.components.PositionComponent;

import java.util.Comparator;

public class LayerZComparator implements Comparator<Entity> {

    private ComponentMapper<PositionComponent> cPosition;

    public LayerZComparator() {
        cPosition = ComponentMapper.getFor(PositionComponent.class);
    }

    @Override
    public int compare(Entity o1, Entity o2) {
        return (int) Math.signum (cPosition.get(o2).position.z - cPosition.get(o1).position.z);
    }
}
