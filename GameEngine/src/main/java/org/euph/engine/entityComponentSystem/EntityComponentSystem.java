package org.euph.engine.entityComponentSystem;

import org.euph.engine.util.ProjectReflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The ECS itself. It Stores all needed references and is responsible to keep track of these and that they don't get corrupted.
 * Because of its responsibility it has an extremely tight Security and only expose what is really needed to be exposed.
 *
 * @author snoweuph
 * @version 1.1
 */
public class EntityComponentSystem {

    /** This Map Keeps track of all Instances of all {@link Component } Types.
     * The Systems of the ECS will use it to get all instances of specific {@link Component} to do their operation on the data these components hold.
     */
    private final Map<Class<? extends Component>, List<Component>> componentInstancesMap = new HashMap<>();
    /** This Map Keeps track of all the Instances of {@link Component Components} that are on an {@link Entity}.
     * This is needed to ensure that each {@link Entity} has all the {@link Component Components}, the {@link Component Components} on it require.
     * an example would be that a MeshRenderer requires a Mesh component to work.
     * Keeping track and exposing some functionality of it through {@link Entity Entities} is also important to get some basic functionality working.
     */
    private final Map<Entity, List<Component>> entityComponentMap = new HashMap<>();

    //Constructor
    public EntityComponentSystem(){

    }

    /** This will register a newly created {@link Entity} from inside its Constructor.
     *
     * @param entity the {@link Entity} that is currently created.
     *
     * @author snoweuph
     */
    protected void createEntity(Entity entity){
        //Test if this instance already has a references, if yes return.
        if(entityComponentMap.containsKey(entity)) return;
        //Create new Entry for this Entity.
        entityComponentMap.put(entity, new ArrayList<>());
    }
    /** This will do the unregistering for an {@link Entity} that is currently getting deleted.
     * after this the {@link Entity#destroyed destroyed status} should be set to true to ensure that no more operations will be done on that {@link Entity}.
     *
     * @param entity the {@link Entity} to unregister.
     *
     * @author snoweuph
     */
    protected void deleteEntity(Entity entity){
        //Test if this instance already has a references, if no return.
        if(!entityComponentMap.containsKey(entity)) return;
        //Remove the References for all Components on the Entity
        for (Component component : entityComponentMap.get(entity)){
            removeComponentReferences(component);
        }
        //Remove it from the Entity-Component Map
        entityComponentMap.remove(entity);
    }
    /** This will register a {@link Component} to an {@link Entity}.
     *
     * @param component the {@link Component} to register.
     * @param entity the {@link Entity} to register to.
     *
     * @author snoweuph
     */
    protected void addComponentReferences(Component component, Entity entity){
        //References to the Components Class
        Class<? extends Component> componentClass = component.getClass();
        List<Class<? extends Component>> targets = getComponentInheritanceList(componentClass);
        targets.add(componentClass);
        //Test if an Arraylist for the targeted Components already exists, if not, create one.
        for(Class<? extends Component> target : targets){
            if(!componentInstancesMap.containsKey(target)){
                componentInstancesMap.put(target, new ArrayList<>());
            }

        }
        //Add the Entry to the Entity-Component Map
        List<Component> entityComponentsList = entityComponentMap.get(entity);
        entityComponentsList.add(component);
        entityComponentMap.put(entity, entityComponentsList);
        //Append the Component Instance to the appropriate ArrayLists inside the Map;
        for(Class<? extends Component> target : targets){
            List<Component> instanceList = componentInstancesMap.get(target);
            instanceList.add(component);
            componentInstancesMap.put(target, instanceList);
        }
    }
    /** This will unregister a {@link Component} from an {@link Entity}.
     * This will also mean that this {@link Component} won't be recognized by the Systems anymore to ensure safety.
     *
     * @param component the {@link Component} to unregister.
     *
     * @author snoweuph
     */
    protected void removeComponentReferences(Component component){
        //References to the Components Class
        Class<? extends Component> componentClass = component.getClass();
        List<Class<? extends Component>> targets = getComponentInheritanceList(componentClass);
        targets.add(componentClass);
        //Test if this Component has Any Entries, if not return
        if(!componentInstancesMap.containsKey(componentClass)) return;
        //Remove References from instances Map, remove Entry if empty
        for(Class<? extends Component> target : targets){
            List<Component> instanceList = componentInstancesMap.get(target);
            instanceList.remove(component);
            if(instanceList.size() == 0){
                componentInstancesMap.remove(target);
            }else {
                componentInstancesMap.put(target, instanceList);
            }
        }
        //Remove References from the Entity-Component Map
        Entity entity = component.getEntity();
        List<Component> entityComponentsList = entityComponentMap.get(entity);
        entityComponentsList.remove(component);
        entityComponentMap.put(entity, entityComponentsList);
    }
    /** This will remove all {@link Component Components} of a specific type from an {@link Entity}.
     *
     * @param entity the {@link Entity} from what to remove.
     * @param componentClass the {@link Component} type to remove.
     *
     * @author snoweuph
     */
    protected void removeComponentReferences(Entity entity, Class<? extends Component> componentClass){
        //Test if Entity has any of these Components
        List<Component> entityComponents = entityComponentMap.get(entity);
        List<Class<? extends Component>> targets = getComponentInheritanceList(componentClass);
        targets.add(componentClass);
        //Create HasMap to Temporarily store what needs to be Removed
        Map<Class<? extends Component>, List<Component>> componentsToRemove = new HashMap<>();
        for (Component component : entityComponents){
            if(!targets.contains(component.getClass()))continue;
            if(!componentsToRemove.containsKey(component.getClass())){
                componentsToRemove.put(component.getClass(), new ArrayList<>());
            }
            List<Component> removeList = componentsToRemove.get(component.getClass());
            removeList.add(component);
            componentsToRemove.put(component.getClass(), removeList);
        }
        if(componentsToRemove.size() == 0)return;
        //Remove Component Instance References
        for(Class<? extends Component> key : componentsToRemove.keySet()){
            List<Component> instanceList = componentInstancesMap.get(key);
            for(Component component : componentsToRemove.get(key)){
                instanceList.remove(component);
            }
            if(instanceList.size() == 0){
                componentInstancesMap.remove(key);
            }else {
                componentInstancesMap.put(key, instanceList);
            }
        }
        //Remove References from the Entity Component
        List<Component> entityComponentsList = entityComponentMap.get(entity);
        for(Class<? extends Component> key : componentsToRemove.keySet()){
            for (Component component : componentsToRemove.get(key)){
                entityComponentsList.remove(component);
            }
            entityComponentMap.put(entity, entityComponentsList);

        }
    }
    /** This will get all Components that are on an {@link Entity}.
     *
     * @param entity the {@link Entity} of which we want to know all {@link Component Components} from.
     * @return the list of all {@link Component Components} on this Entity.
     *
     * @author snoweuph
     */
    protected List<Component> getComponentsOnEntity(Entity entity){
        return entityComponentMap.get(entity);
    }
    /** This will list all instance of a specific {@link Component} type.
     *
     * @param componentClass  the {@link Component} type of which we want to know all instances.
     * @return the list of all instances.
     *
     * @author snoweuph
     */
    protected List<Component> getComponentInstances(Class<? extends  Component> componentClass){
        return componentInstancesMap.get(componentClass);
    }
    /** This is used at multiple places inside the ECS to work with the Inheritance of Components.
     * Without this it wouldn't be possible that if we try to remove all components of Type Collider from an {@link Entity},
     * that not Only all Colliders but also all inherited {@link Component} Types like BoxCollider or MeshCollider get deleted.
     *
     * @param componentClass the {@link Component} type of which we want to gather classes that inherit from it.
     * @return the list of {@link Component} types that inherit from the input type.
     *
     * @author snoweuph
     */
    private List<Class<? extends Component>> getComponentInheritanceList(Class<?extends Component> componentClass){
        return new ArrayList<>(ProjectReflection.DATA.getSubTypesOf(componentClass));
    }
}
