package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** This is the Definition of an Entity inside the ECS.
 *
 * @author snoweuph
 * @version 1.1
 */
public class Entity {

    private boolean destroyed = false;
    private final Scene SCENE;
    //Constructor
    public Entity(Scene scene){
        SCENE = scene;
        SCENE.createEntity(this, null);
    }
    public Entity(Scene scene, Entity parent){
        SCENE = scene;
        SCENE.createEntity(this, parent);
    }

    //Component Handling
    /** Puts a Component onto this Entity.
     *
     * @param component the Component to put onto this.
     *
     * @return returns this Entity, so that functions can be stacked.
     *
     * @author snoweuph
     */
    public Entity putComponent(Component component){
        putComponent(component, new ArrayList<>());
        return this;
    }
    /** The Recursive Function for Putting a Component onto an Entity and also adding all required Components.
     * This Function is wrapped by {@link #putComponent(Component)}.
     * parameter `List<Class<? extends Component>> componentsOnEntityList` and return value `List<Class<? extends Component>>` are Important
     * for keeping track of what Components are on the Entity without making Lots of Calls to {@link EntityComponentSystem#getComponentsOnEntity(Entity)},
     * while trying to add all required Components.
     *
     * @throws IllegalStateException This Entity is Destroyed and Waiting for GC. It shouldn't have anymore references
     *
     * @param component The Component it should add.
     * @param componentsOnEntityList The List of Components that are on the Entity.
     *
     * @return the List of the Classes of Components It added in that Cycle.
     *
     * @author snoweuph
     */
    private List<Class<? extends Component>> putComponent(Component component, List<Class<? extends Component>> componentsOnEntityList){
        //If Already Destroyed Ignore and return
        if(destroyed) throw new IllegalStateException("This Entity is Destroyed and Waiting for GC. It shouldn't have anymore references");
        //Remove the Old References from the ECS
        if(component.getEntity() != null) SCENE.getECS().removeComponentReferences(component);
        //Set the new Entity
        component.setEntity(this);
        //Add the References to the ECS
        SCENE.getECS().addComponentReferences(component, this);
        //Create a list of Existing Components on the Entity, if not an empty one was passed
        List<Class<? extends Component>> componentsOnEntity = componentsOnEntityList;
        if(componentsOnEntity.size() == 0){
            List<Class<? extends Component>> tempComponentsOnEntity = componentsOnEntity;
            SCENE.getECS().getComponentsOnEntity(this).forEach(c -> tempComponentsOnEntity.add(c.getClass()));
            //Filter of Duplicate Entries.
            componentsOnEntity = tempComponentsOnEntity.stream().distinct().collect(Collectors.toList());
        }
        //Create a List to store all Added Components
        List<Class<? extends Component>> addedComponents = new ArrayList<>();
        //Iterate over Required Components
        for (Component requiredComponent: component.getRequiredComponents()){
            //Continue if Required Component is on Entity
            if(componentsOnEntity.contains(requiredComponent.getClass())) continue;
            //Run this Function Recursive for the Required Component, add the Added Components to the list of Components on this Entity and then remove duplicates
            componentsOnEntity.addAll(putComponent(requiredComponent, componentsOnEntity));
            addedComponents.add(requiredComponent.getClass());
            componentsOnEntity = componentsOnEntity.stream().distinct().collect(Collectors.toList());
        }
        //Return list of added Components
        return addedComponents;
    }
    /** Removes a Specific Component from the Entity.
     *
     * @param component the Component to remove.
     *
     * @return returns this Entity, so that functions can be stacked.
     *
     * @author snoweuph
     */
    public Entity removeComponent(Component component){
        //If Already Destroyed Ignore and return
        if(destroyed) throw new IllegalStateException("This Entity is Destroyed and Waiting for GC. It shouldn't have anymore references");
        SCENE.getECS().removeComponentReferences(component);
        return this;
    }
    /** Removes all Components of a Specific Type that are on an Entity
     *
     * @param componentClass the Type of Component that should be removed
     *
     * @return this Entity, so that functions can be stacked.
     *
     * @author snoweuph
     */
    public Entity removeAllComponents(Class<?extends Component> componentClass){
        //If Already Destroyed Ignore and return
        if(destroyed) throw new IllegalStateException("This Entity is Destroyed and Waiting for GC. It shouldn't have anymore references");
        SCENE.getECS().removeComponentReferences(this, componentClass);
        return this;
    }

    //Entity Handling
    /** This removes all Entries inside the ECS of this entity, so that the entity can wait for Garbage Collecting
     *
     * @implNote Remove all References to this Entity after running this.
     *
     * @author snoweuph
     */
    public void destroy(){
        destroyed = true;
        SCENE.deleteEntity(this);
    }

    //Getter
    /** @return Whether this Entity is already destroyed.
     *
     * @implNote If this returns True, Remove all References to this Entity to let it get Garbage Collected.
     *
     * @author snoweuph
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    /** @return a list of all Components that are on this Entity.
     *
     * @author snoweuph
     */
    public List<Component> getComponents(){
        return SCENE.getECS().getComponentsOnEntity(this);
    }
    /** A Filtered Version of {@link #getComponents()}.
     *
     * @param componentClass the Component type to filter for.
     * @return a filtered list of all Components that are on this Entity
     *
     * @author snoweuph
     */
    public List<Component>  getComponents(Class<? extends Component> componentClass){
        return SCENE.getECS().getComponentsOnEntity(this).stream().filter(x -> x.getClass().equals(componentClass)).toList();
    }
    /** A Version of {@link #getComponents(Class)} that returns only the first found object.
     *
     * @param componentClass  the Component type to filter for.
     * @return the first instance of the specified {@link Component} on this Entity. If none found, it returns null.
     *
     * @author snoweuph
     */
    public Component getComponent(Class<? extends Component> componentClass){
        List<Component> components= SCENE.getECS().getComponentsOnEntity(this).stream().filter(x -> x.getClass().equals(componentClass)).toList();
        return components.size() == 0 ? null : components.get(0);
    }

    /** @return the {@link Scene} this is in.
     *
     * @author snoweuph
     */
    public Scene getSCENE() {
        return SCENE;
    }
    /** @return the parent {@link Entity}.
     *
     * @author snoweuph
     */
    public Entity getParent(){
        return SCENE.getParent(this);
    }
    /** @return the list of all children.
     *
     * @author snoweuph
     */
    public List<Entity> getChildren(){
        return  SCENE.getChildren(this);
    }

    //Setter
    //INFO: Should Only be used to speedup loops in SCENE
    protected void setDestroyed() {
        destroyed = true;
    }
    public void setParent(Entity parent){
        SCENE.setParent(this, parent);
    }
    public void setParent(){
        SCENE.setParentRoot(this);
    }

}
