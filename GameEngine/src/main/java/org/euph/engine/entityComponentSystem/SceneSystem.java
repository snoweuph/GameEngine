package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;

//TODO: Next Version:
/* - Implement Serialized Scene Objects
   - Make Scene Loading into an extra Thread that is just used for reading Serialized Scene Files
     (will return its current loading status, so that this can be used in a Loading screen or similar)
   - Make a Scene Writer Thread, that write Scenes to a Serialized file, can be accessed by a SceneSystem#saveScene(String path, Scene scene)
 */
public class SceneSystem {
    private static List<Scene> scenes = new ArrayList<>();
    private static List<Scene> loadedScenes = new ArrayList<>();;
    private static List<Scene> unloadedScenes = new ArrayList<>();

    protected static void createScene(Scene scene){
        unloadedScenes.add(scene);
    }
    //INFO: removes scene from memory
    public static void removeScene(Scene scene){
        if(loadedScenes.contains(scene)){
            unloadScene(scene);
        }
        unloadedScenes.remove(scene);
    }
    public static void loadScene(Scene scene, SceneLoadMode mode){
        if(!unloadedScenes.contains(scene))return;
        if(mode == SceneLoadMode.ADDITIVE){
            unloadedScenes.remove(scene);
            loadedScenes.add(scene);
            return;
        }
        for(Scene loadedScene : loadedScenes){
            unloadScene(loadedScene);
        }
        unloadedScenes.remove(scene);
        loadedScenes.add(scene);
    }
    public static void unloadScene(Scene scene){
        if(!loadedScenes.contains(scene))return;
        loadedScenes.remove(scene);
        unloadedScenes.add(scene);
    }

    public static List<Scene> getScenes() {
        return scenes;
    }
    public static boolean isLoaded(Scene scene){
        return loadedScenes.contains(scene);
    }
    public static  boolean isUnloaded(Scene scene){
        return unloadedScenes.contains(scene);
    }


    protected static List<Component> getComponentInstances(Class<? extends Component> componentClass){
        List<Component> instances = new ArrayList<>();
        for (Scene scene : loadedScenes){
            instances.addAll(scene.getECS().getComponentInstances(componentClass));
        }
        return instances;
    }
}
