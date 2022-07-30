package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;

//TODO: store all scenes, implement scene functionality, like serialization and scene managing on runtime.
//TODO: Scene Entity Bridge -> allows to move Entities from on Scene to another
public class SceneSystem {

    private static List<Scene> scenes = new ArrayList<>();

    protected static List<Component> getComponentInstances(Class<? extends Component> componentClass){
        List<Component> instances = new ArrayList<>();
        for (Scene scene : scenes){
            instances.addAll(scene.getECS().getComponentInstances(componentClass));
        }
        return instances;
    }
}
