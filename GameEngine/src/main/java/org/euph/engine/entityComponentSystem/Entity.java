package org.euph.engine.entityComponentSystem;

import java.util.ArrayList;

//TODO: JavaDoc
public class Entity {

    private boolean destroyed = false;

    public Entity(){
        EntityComponentSystem.createEntity(this);
    }
    public void putComponent(Component component){
        putComponent(component, new ArrayList<>());
    }
    private ArrayList<Class<? extends Component>> putComponent(Component component, ArrayList<Class<? extends Component>> componentsOnEntityList){
        //If Already Destroyed Ignore and return
        if(destroyed) throw new IllegalStateException("This Entity is Destroyed an Waiting for GC. It shouldn't have anymore references");
        //Remove the Old References from the ECS
        if(component.getEntity() != null) EntityComponentSystem.removeComponentReferences(component);
        //Create a list of Existing Components on the Entity, if not an empty one was passed
        ArrayList<Class<? extends Component>> componentsOnEntity = componentsOnEntityList;
        if(componentsOnEntity.size() <= 0){
            ArrayList<Class<? extends Component>> tempComponentsOnEntity = componentsOnEntity;
            EntityComponentSystem.getComponentsOnEntity(this).forEach(c -> tempComponentsOnEntity.add(c.getClass()));
            //Filter of Duplicate Entries.
            componentsOnEntity = (ArrayList<Class<? extends Component>>) tempComponentsOnEntity.stream().distinct();
        }
        //Create a List to store all Added Components
        ArrayList<Class<? extends Component>> addedComponents = new ArrayList<>();
        //Iterate over Required Components
        for (Component requiredComponent: component.getRequiredComponents()){
            //Continue if Required Component is on Entity
            if(componentsOnEntity.contains(requiredComponent.getClass())) continue;
            //Run this Function Recursive for the Required Component, add the Added Components to the list of Components on this Entity and then remove duplicates
            //TODO: Implement some Infinite loop Safety
            componentsOnEntity.addAll(putComponent(requiredComponent, componentsOnEntity));
            addedComponents.add(requiredComponent.getClass());
            componentsOnEntity = (ArrayList<Class<? extends Component>>) componentsOnEntity.stream().distinct();
        }
        //Remove Duplicate Entries of the List added Components
        addedComponents = (ArrayList<Class<? extends Component>>) addedComponents.stream().distinct();
        //Set the new Entity
        component.setEntity(this);
        //Add the References to the ECS
        EntityComponentSystem.addComponentReferences(component, this);
        //Return list of added Components
        return addedComponents;
    }

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
}
