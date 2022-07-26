package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** This is the Definition of an Entity inside the ECS.
 *
 * @author snoweuph
 * @version 1.0
 */
public class Entity {

    private boolean destroyed = false;
    //Constructor
    public Entity(){
        EntityComponentSystem.createEntity(this);
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
        if(component.getEntity() != null) EntityComponentSystem.removeComponentReferences(component);
        //Set the new Entity
        component.setEntity(this);
        //Add the References to the ECS
        EntityComponentSystem.addComponentReferences(component, this);
        //Create a list of Existing Components on the Entity, if not an empty one was passed
        List<Class<? extends Component>> componentsOnEntity = componentsOnEntityList;
        if(componentsOnEntity.size() == 0){
            List<Class<? extends Component>> tempComponentsOnEntity = componentsOnEntity;
            EntityComponentSystem.getComponentsOnEntity(this).forEach(c -> tempComponentsOnEntity.add(c.getClass()));
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
        EntityComponentSystem.removeComponentReferences(component);
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
        EntityComponentSystem.removeComponentReferences(this, componentClass);
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
        EntityComponentSystem.deleteEntity(this);
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

    /** @return a list of all Components that are on this Entity
     *
     * @author snoweuph
     */
    public List<Component> getComponents(){
        return EntityComponentSystem.getComponentsOnEntity(this);
    }
}
