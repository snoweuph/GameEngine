package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;

//TODO NEXT:
/* - Implement Serialized Scene Objects
   - Make Scene Loading into an extra Thread that is just used for reading Serialized Scene Files
     (will return its current loading status, so that this can be used in a Loading screen or similar)
   - Make a Scene Writer Thread, that write Scenes to a Serialized file, can be accessed by a SceneSystem#saveScene(String path, Scene scene)
 */
/** The SceneSystem, it manages all {@link Scene Scenes}, their loading and unloading and saving.
 *
 * @author snoweuph
 * @version 1.0
 */
public class SceneSystem {
    private static final List<Scene> scenes = new ArrayList<>();
    private static final List<Scene> loadedScenes = new ArrayList<>();;
    private static final List<Scene> unloadedScenes = new ArrayList<>();

    //Scene Management
    /** This will be called from the Constructor of any {@link Scene}, and will add it to the list of Scenes.
     *
     * @param scene the new {@link Scene} that should be registered.
     *
     * @author snoweuph
     */
    protected static void createScene(Scene scene){
        unloadedScenes.add(scene);
    }
    /** This will completely remove a {@link Scene} and set it to destroyed, so that it waits for GC.
     *
     * @param scene the {@link Scene} which should be removed and Destroyed.
     *
     * @author snoweuph
     */
    public static void removeScene(Scene scene){
        if(loadedScenes.contains(scene)){
            unloadScene(scene);
        }
        unloadedScenes.remove(scene);
        scene.destroy();
    }
    /** This will load a {@link Scene} from the list of unloaded Scenes.
     * The Scene can either be loaded in Additive or Single Mode.
     * In Single Mode, all other Scenes will be first unloaded.
     *
     * @param scene the {@link Scene} which should be loaded.
     * @param mode the way in which the {@link Scene} should be loaded.
     *
     * @author snoweuph
     */
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
    /** Unloads a Scene which is loaded.
     *
     * @param scene the {@link Scene} which should be unloaded.
     *
     * @author snoweuph
     */
    public static void unloadScene(Scene scene){
        if(!loadedScenes.contains(scene))return;
        loadedScenes.remove(scene);
        unloadedScenes.add(scene);
    }

    //Getter
    /** @return the list of all registered {@link Scene Scenes}.
     *
     * @author snoweuph
     */
    public static List<Scene> getScenes() {
        return scenes;
    }
    /** @return whether a {@link Scene} is loaded or not.
     *
     * @param scene the {@link Scene} of which the status should be returned.
     *
     * @author snoweuph
     */
    public static boolean isLoaded(Scene scene){
        return loadedScenes.contains(scene);
    }
    /** @return whether a {@link Scene} is not loaded.
     *
     * @param scene the {@link Scene} of which the status should be returned.
     *
     * @author snoweuph
     */
    public static  boolean isUnloaded(Scene scene){
        return unloadedScenes.contains(scene);
    }

    /** @return the list of all Instances of a Specified {@link Component} type of all loaded {@link Scene Scenes}.
     *
     * @param componentClass the {@link Component} type, of which all Instances should bre returned.
     *
     * @author snoweuph
     */
    protected static List<Component> getComponentInstances(Class<? extends Component> componentClass){
        List<Component> instances = new ArrayList<>();
        for (Scene scene : loadedScenes){
            instances.addAll(scene.getECS().getComponentInstances(componentClass));
        }
        return instances;
    }
}
