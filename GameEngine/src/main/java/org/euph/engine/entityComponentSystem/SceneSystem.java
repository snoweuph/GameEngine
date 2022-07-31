package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;

//TODO: implement Serialization in next Version
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
    //TODO: make it so that scene loading and unloading is run async and returns its status
    public static void loadScene(Scene scene, SceneLoadMode mode){
        //TODO: implement Scene Load mode
        if(!unloadedScenes.contains(scene))return;
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
