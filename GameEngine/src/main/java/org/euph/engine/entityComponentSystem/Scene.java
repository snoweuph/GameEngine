package org.euph.engine.entityComponentSystem;

import org.euph.engine.entityComponentSystem.Entity;
import org.euph.engine.entityComponentSystem.EntityComponentSystem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Define what a Scene is. store structure of parents and child Transforms in this Scene
//TODO: JavaDoc
public class Scene {
    //Question: does a Scene store Transform Components or Entities ? -> Entities
    //new Question: where to store the Transforms and their connection then? -> it's directly linked to the connections of entities

    //Entities need to store their parent entity in the scene
    //transforms need to know their parents
    //not all entities have transforms
    //all transforms have a corresponding entity
    //each scene has one root entity that is immutable.
    //COULDDO: give scene a name that can be used when displaying some information.
    //INFO: Scene names only purpose is to be displayed in debug info or loading screens. It should not get any more functionality, like filtering scenes by their name.

    //TODO: -> scenes can be loaded and not loaded -> should the Scene System be part of the systems of the ECS os should be the ECS a part of the SceneSystem?

    private final EntityComponentSystem ECS;
    private final Entity ROOT;
    private final String NAME;
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
