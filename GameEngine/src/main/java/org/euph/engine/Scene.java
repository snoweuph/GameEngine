package org.euph.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The Definition of a scene. a scene stores entities with their components.
 * The Component Handling is done by the Scenes own instance of an ECS.
 *
 * @author snoweuph
 * @version 1.0
 */
public class Scene {
    private boolean destroyed = false;
    private final EntityComponentManager ECS;
    private final Entity ROOT;
    private final String NAME;
    private List<Entity> entityList = new ArrayList<>();
    private Map<Entity, List<Entity>> entityChildrenMap = new HashMap<>();
    private Map<Entity, Entity> entityParentMap = new HashMap<>();

    //Constructor
    /** Creates a new Unnamed {@link Scene}.
     *
     * @author snoweuph
     */
    protected Scene() {
        NAME = "unnamed Scene";
        ECS = new EntityComponentManager();
        ROOT = new Entity(this);
        SceneManager.createScene(this);
    }
    /** Creates a new {@link Scene} with a name.
     *
     * @param name the name of the new {@link Scene}.
     *
     * @author snoweuph
     */
    protected Scene(String name){
        NAME = name;
        ECS = new EntityComponentManager();
        ROOT = new Entity(this);
        SceneManager.createScene(this);
    }

    //Entity Handling
    /** Creates a new {@link Entity} in the {@link Scene} with a specific Parent, or if its null with the ROOT as its Parent.
     *
     * @param entity the newly created {@link Entity}.
     * @param parent the Parent to set.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
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
    /** completely deletes an {@link Entity} and all its Children.
     *
     * @param entity the {@link Entity} to delete.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected void deleteEntity(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        List<Entity> children = entityChildrenMap.get(entity);
        for(Entity child : children){
            deleteEntity(child);
        }
        entityList.remove(entity);
        ECS.deleteEntity(entity);
        unbindParent(entity);
        entityChildrenMap.remove(entity);
        entityParentMap.remove(entity);
        entity.setDestroyed();
    }
    /** sets the Parent of an {@link Entity}, if the Parent is null, it will be instead set to th ROOT of the {@link Scene}.
     *
     * @param entity the {@link Entity} of which to change the Parent.
     * @param parent the new Parent.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected void setParent(Entity entity, Entity parent){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        unbindParent(entity);
        bindParent(entity, parent == null ? ROOT : parent);
    }
    /** Sets the Parent of an {@link Entity} to the ROOT of the {@link Scene}.
     *
     * @param entity the Entity, which should get the ROOT as its Parent.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected void setParentRoot(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        unbindParent(entity);
        bindParent(entity, ROOT);
    }

    //Parent Binding
    /** Unbinds the Parent of an {@link  Entity}.
     *
     * @param entity the entity of which the Parent should be unbound.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    private void unbindParent(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        Entity parent = entityParentMap.get(entity);
        List<Entity> childrenOfParent = entityChildrenMap.get(parent);
        childrenOfParent.remove(entity);
        entityChildrenMap.put(parent, childrenOfParent);
    }
    /** Binds an {@link Entity} as a Parent to another {@link Entity}.
     *
     * @param entity the {@link Entity} which should get the Parent.
     * @param parent the {@link Entity} which should be the Parent.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
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
    /**@return whether the Entity is Destroyed and waits for GC or not.
     *
     * @author snoweuph
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    /**@return the {@link EntityComponentManager ECS} of this {@link Scene}.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected EntityComponentManager getECS(){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return ECS;
    }
    /**@return the Name of this {@link Scene}.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    public String getName() {
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return NAME;
    }
    /**@return the Parent of an {@link Entity}. If the Parent is the ROOT, it returns null.
     *
     * @param entity the {@link Entity} of which to get the Parent.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected Entity getParent(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        Entity parent = entityParentMap.get(entity);
        return parent.equals(ROOT) ? null : parent;
    }
    /**@return the list of all Children of a specific {@link Entity}
     *
     * @param entity the {@link Entity} of which to get the Children.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected List<Entity> getChildren(Entity entity){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return entityChildrenMap.get(entity);
    }
    /**@return the list of all {@link Entity Entities} of this {@link Scene}.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected List<Entity> getEntities() {
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        return entityList;
    }

    //Setter
    /** Sets this {@link Scene} as Destroyed, ready for GC.
     *
     * @throws IllegalStateException The Scene is accessed, though its already destroyed and waiting for GC.
     *
     * @author snoweuph
     */
    protected void destroy(){
        if(destroyed) throw new IllegalStateException("This Scene is Destroyed and Waiting for GC. It shouldn't have anymore references");
        destroyed = true;
    }
}
