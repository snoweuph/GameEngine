package org.euph.engine.entityComponentSystem;

import org.euph.engine.entityComponentSystem.Entity;
import org.euph.engine.entityComponentSystem.EntityComponentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Define what a Scene is. store structure of parents and child Transforms in this Scene
//TODO: Make Scene Serializable -> Store Scenes in extra files / have a System to create Scenes -> for code usage having a function that creates the wanted scene is enough, but for an Editor Serialization is needed to improve performance.
//TODO: JavaDoc
//INFO: Scene names only purpose is to be displayed in debug info or loading screens. It should not get any more functionality, like filtering scenes by their name.
//TODO: -> scenes can be loaded and not loaded -> should the Scene System be part of the systems of the ECS os should be the ECS a part of the SceneSystem? -> ECS should be part of SCENE
public class Scene {

    private final EntityComponentSystem ECS;
    private final Entity ROOT;
    private final String NAME;
    private List<Entity> entityList = new ArrayList<>();
    private Map<Entity, List<Entity>> entityChildrenMap = new HashMap<>();
    private Map<Entity, Entity> entityParentMap = new HashMap<>();

    //Constructor
    public Scene() {
        NAME = "unnamed Scene";
        ECS = new EntityComponentSystem();
        ROOT = new Entity(this);
    }
    public Scene(String name){
        NAME = name;
        ECS = new EntityComponentSystem();
        ROOT = new Entity(this);
    }

    //Getter
    protected EntityComponentSystem getECS(){
        return ECS;
    }
    public String getNAME() {
        return NAME;
    }
    //INFO: will return null, if the parent is the ROOT entity;
    protected Entity getParent(Entity entity){
        Entity parent = entityParentMap.get(entity);
        return parent.equals(ROOT) ? null : parent;
    }
    protected List<Entity> getChildren(Entity entity){
        return entityChildrenMap.get(entity);
    }
}
