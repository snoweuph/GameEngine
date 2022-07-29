package org.euph.engine.entityComponentSystem;

import org.euph.engine.util.ClassReflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: JavaDoc
public class EntityComponentSystem {

    private static final HashMap<Class<? extends Component>, List<Component>> componentInstancesMap = new HashMap<>(); // K = a Component; V = all instances of this Component;
    private static final HashMap<Entity, List<Component>> entityComponentMap = new HashMap<>();

    protected static void createEntity(Entity entity){
        //Test if this instance already has a references, if yes return.
        if(entityComponentMap.containsKey(entity)) return;
        //Create new Entry for this Entity.
        entityComponentMap.put(entity, new ArrayList<>());
    }
    protected static void deleteEntity(Entity entity){
        //Test if this instance already has a references, if no return.
        if(!entityComponentMap.containsKey(entity)) return;
        //Remove the References for all Components on the Entity
        for (Component component : entityComponentMap.get(entity)){
            removeComponentReferences(component);
        }
        //Remove it from the Entity-Component Map
        entityComponentMap.remove(entity);
    }
    protected static void addComponentReferences(Component component, Entity entity){
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
        //Append the Component Instance to the appropriate ArrayLists inside the HashMap;
        for(Class<? extends Component> target : targets){
            List<Component> instanceList = componentInstancesMap.get(target);
            instanceList.add(component);
            componentInstancesMap.put(target, instanceList);
        }
    }
    protected static void removeComponentReferences(Component component){
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
            if(instanceList.size() <= 0){
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
    protected static void removeComponentReferences(Entity entity, Class<? extends Component> componentClass){
        //Test if Entity has any of these Components
        List<Component> entityComponents = entityComponentMap.get(entity);
        List<Class<? extends Component>> targets = getComponentInheritanceList(componentClass);
        targets.add(componentClass);
        //Create HasMap to Temporarily store what needs to be Removed
        HashMap<Class<? extends Component>, List<Component>> componentsToRemove = new HashMap<>();
        for (Component component : entityComponents){
            if(!targets.contains(component.getClass()))continue;
            if(!componentsToRemove.containsKey(component.getClass())){
                componentsToRemove.put(component.getClass(), new ArrayList<>());
            }
            List<Component> removeList = componentsToRemove.get(component.getClass());
            removeList.add(component);
            componentsToRemove.put(component.getClass(), removeList);
        }
        if(componentsToRemove.size() <= 0)return;
        //Remove Component Instance References
        for(Class<? extends Component> key : componentsToRemove.keySet()){
            List<Component> instanceList = componentInstancesMap.get(key);
            for(Component component : componentsToRemove.get(key)){
                instanceList.remove(component);
            }
            if(instanceList.size() <= 0){
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
    protected static List<Component> getComponentsOnEntity(Entity entity){
        return entityComponentMap.get(entity);
    }
    protected static List<Component> getComponentInstances(Class<? extends  Component> componentClass){
        return componentInstancesMap.get(componentClass);
    }
    private static List<Class<? extends Component>> getComponentInheritanceList(Class<?extends Component> componentClass){
        return new ArrayList<>(ClassReflection.reflections.getSubTypesOf(componentClass));
    }
}
