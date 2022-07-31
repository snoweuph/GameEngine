package org.euph.engine.entityComponentSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: JavaDoc -> Scene names only purpose is to be displayed in debug information or loading screens. It should not get any more functionality, like filtering scenes by their name.
public class Scene implements Serializable {
    private boolean destroyed = false;
    private final EntityComponentSystem ECS;
    private final Entity ROOT;
    private final String NAME;
    private List<Entity> entityList = new ArrayList<>();
    private Map<Entity, List<Entity>> entityChildrenMap = new HashMap<>();
    private Map<Entity, Entity> entityParentMap = new HashMap<>();

    //Constructor
    protected Scene() {
        NAME = "unnamed Scene";
        ECS = new EntityComponentSystem();
        ROOT = new Entity(this);
        SceneSystem.createScene(this);
    }
    protected Scene(String name){
        NAME = name;
        ECS = new EntityComponentSystem();
        ROOT = new Entity(this);
        SceneSystem.createScene(this);
    }

    protected void createEntity(Entity entity, Entity parent){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        if(parent != null){
            bindParent(entity, parent);
        }else {
            bindParent(entity, ROOT);
        }
        ECS.createEntity(entity);
        entityList.add(entity);
    }
    protected void deleteEntity(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        entityList.remove(entity);
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
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        unbindParent(entity);
        bindParent(entity, parent == null ? ROOT : parent);
    }
    protected void setParentRoot(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        unbindParent(entity);
        bindParent(entity, ROOT);
    }
    private void unbindParent(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        Entity parent = entityParentMap.get(entity);
        List<Entity> childrenOfParent = entityChildrenMap.get(parent);
        childrenOfParent.remove(entity);
        entityChildrenMap.put(parent, childrenOfParent);
    }
    private void bindParent(Entity entity, Entity parent){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        if(!entityChildrenMap.containsKey(parent)){
            entityChildrenMap.put(parent, new ArrayList<>());
        }
        List<Entity> children = entityChildrenMap.get(parent);
        children.add(entity);
        entityChildrenMap.put(parent, children);
        entityParentMap.put(entity, parent);
    }

    //Getter
    public boolean isDestroyed() {
        return destroyed;
    }
    protected EntityComponentSystem getECS(){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return ECS;
    }
    public String getName() {
        return NAME;
    }
    //INFO: will return null, if the parent is the ROOT entity;
    protected Entity getParent(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        Entity parent = entityParentMap.get(entity);
        return parent.equals(ROOT) ? null : parent;
    }
    protected List<Entity> getChildren(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return entityChildrenMap.get(entity);
    }
    protected List<Entity> getEntities() {
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return entityList;
    }
    //Setter
    protected void destroy(){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        destroyed = true;
    }
}
