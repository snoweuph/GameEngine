package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: JavaDoc -> Scene names only purpose is to be displayed in debug information or loading screens. It should not get any more functionality, like filtering scenes by their name.
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

    protected void createEntity(Entity entity, Entity parent){
        if(parent != null){
            bindParent(entity, parent);
        }else {
            bindParent(entity, ROOT);
        }
        ECS.createEntity(entity);
    }
    protected void deleteEntity(Entity entity){
        ECS.deleteEntity(entity);
        unbindParent(entity);
        List<Entity> children = entityChildrenMap.get(entity);
        for(Entity child : children){
            ECS.deleteEntity(child);
            child.setDestroyed();
            entityParentMap.remove(child);
        }
        entityChildrenMap.remove(entity);
        entityParentMap.remove(entity);
    }
    //INFO: When null, parent will be set to ROOT of scene
    protected void setParent(Entity entity, Entity parent){
        unbindParent(entity);
        bindParent(entity, parent == null ? ROOT : parent);
    }
    protected void setParentRoot(Entity entity){
        unbindParent(entity);
        bindParent(entity, ROOT);
    }
    private void unbindParent(Entity entity){
        Entity parent = entityParentMap.get(entity);
        List<Entity> childrenOfParent = entityChildrenMap.get(parent);
        childrenOfParent.remove(entity);
        entityChildrenMap.put(parent, childrenOfParent);
    }
    private void bindParent(Entity entity, Entity parent){
        if(!entityChildrenMap.containsKey(parent)){
            entityChildrenMap.put(parent, new ArrayList<>());
        }
        List<Entity> children = entityChildrenMap.get(parent);
        children.add(entity);
        entityChildrenMap.put(parent, children);
        entityParentMap.put(entity, parent);
    }

    //Getter
    protected EntityComponentSystem getECS(){
        return ECS;
    }
    public String getName() {
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
