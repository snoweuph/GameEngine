package org.euph.engine.entityComponentSystem;

import java.util.List;

//TODO:JavaDoc
public class EngineSystem {

    protected static List<Component> getComponentInstances(Class<? extends Component> componentClass){
        return EntityComponentSystem.getComponentInstances(componentClass);
    }
}
