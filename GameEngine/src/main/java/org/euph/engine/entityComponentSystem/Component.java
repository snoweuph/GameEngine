package org.euph.engine.entityComponentSystem;

import java.util.List;

/** This class contains the basic abstraction for a Component that will be used for an ECS.
 *
 * @implNote Classes extending this, should only store Data and don't have any functionality themselves.
 *
 * @author snoweuph
 * @version 1.0
 */
public abstract class Component {

    //The Entity the Component is on.
    private Entity entity;

    /** @return a list of new Instances of required Components.
     *
     * @author snoweuph
     */
    protected abstract List<Component> getRequiredComponents();

    //Getter
    /** @return the Entity this Component is on.
     *
     * @author snoweuph
     */
    public Entity getEntity() {
        return entity;
    }

    //Setter
    /** Sets the Entity this Component is on.
     *
     * @param entity the entity this should be set on.
     *
     * @author snoweuph
     */
    protected void setEntity(Entity entity) {
        this.entity = entity;
    }
}
