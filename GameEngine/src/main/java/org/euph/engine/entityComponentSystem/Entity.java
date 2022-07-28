package org.euph.engine.entityComponentSystem;

//TODO: JavaDoc
//TODO: Handle Required Components
public class Entity {

    private boolean destroyed = false;

    public Entity(){
        EntityComponentSystem.createEntity(this);
    }

    public void putComponent(Component component){
        //If Already Destroyed Ignore and return
        if(destroyed) throw new IllegalStateException("This Entity is Destroyed an Waiting for GC. It shouldn't have anymore references");
        //Remove the References from the ECS
        if(component.getEntity() != null) EntityComponentSystem.removeComponentReferences(component);
        //Set the new Entity
        component.setEntity(this);
        //Add the References to the ECS
        EntityComponentSystem.addComponentReferences(component, this);
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
