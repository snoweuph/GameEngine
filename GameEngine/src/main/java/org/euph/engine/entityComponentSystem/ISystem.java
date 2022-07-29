package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;

public class ISystem {

    protected static List<Component> getComponentInstances(Class<? extends  Component> componentClass){
        return EntityComponentSystem.getComponentInstances(componentClass);
    }
}
