package org.euph.engine.entityComponentSystem;

import java.util.List;

/** A Base class for all Systems of the ECS.
 * This class also is a wrapper for some protected functions of the ECS that should to
 * be accessed by these kind of systems, but should not be public fo safety.
 *
 * @author snoweuph
 * @version 1.0
 */
public abstract class EngineSystem {

    /** Wraps the {@link EntityComponentSystem#getComponentInstances(Class) getComponentInstances(Class)} function
     * of the {@link EntityComponentSystem ECS} that is protected, so that it's available to the Systems of the ECS that need it.
     *
     * @param componentClass the type of component to search for.
     * @return the list of all Instances of this type of Component.
     *
     * @author snoweuph
     */
    protected static List<Component> getComponentInstances(Class<? extends Component> componentClass){
        //return EntityComponentSystem.getComponentInstances(componentClass);
        //TODO:return it from Scene
        return null;
    }
}
