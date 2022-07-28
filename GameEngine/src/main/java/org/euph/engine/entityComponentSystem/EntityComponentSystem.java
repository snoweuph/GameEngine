package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;
import java.util.HashMap;

//TODO: JavaDoc
public class EntityComponentSystem {

    private static HashMap<Class<? extends Component>, ArrayList<Component>> componentInstancesMap; // K = a Component; V = all instances of this Component;
    private static HashMap<Entity, ArrayList<Component>> entityComponentMap;

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
        Class<? extends Component> componentClass= component.getClass();
        //Test if an Arraylist for this Component already exists, if not, create one.
        if(!componentInstancesMap.containsKey(componentClass)){
            componentInstancesMap.put(componentClass, new ArrayList<>());
        }
        //Add the Entry to the Entity-Component Map
        ArrayList<Component> entityComponentsList = entityComponentMap.get(entity);
        entityComponentsList.add(component);
        entityComponentMap.put(entity, entityComponentsList);
        //Append the Component Instance to the appropriate ArrayList inside the HashMap;
        ArrayList<Component> instanceList = componentInstancesMap.get(componentClass);
        instanceList.add(component);
        componentInstancesMap.put(componentClass, instanceList);
    }
    protected static void removeComponentReferences(Component component){
        //References to the Components Class
        Class<? extends Component> componentClass= component.getClass();
        //Test if this Component has Any Entries, if not return
        if(!componentInstancesMap.containsKey(componentClass)) return;
        //Remove References from instances Map, remove Entry if empty
        ArrayList<Component> instanceList = componentInstancesMap.get(componentClass);
        instanceList.remove(component);
        if(instanceList.size() <= 0){
            componentInstancesMap.remove(componentClass);
        }else {
            componentInstancesMap.put(componentClass, instanceList);
        }
        //Remove References from the Entity-Component Map
        Entity entity = component.getEntity();
        ArrayList<Component> entityComponentsList = entityComponentMap.get(entity);
        entityComponentsList.remove(component);
        entityComponentMap.put(entity, entityComponentsList);
    }

}
