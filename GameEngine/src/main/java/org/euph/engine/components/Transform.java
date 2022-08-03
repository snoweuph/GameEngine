package org.euph.engine.entityComponentSystem.components;

import org.euph.engine.Component;
import org.euph.engine.Entity;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

//TODO: getters and setters that are giving basic functionality, like a .translate(Vector3f translation) and .setTranslation(Vector3f translation)
//TODO: JavaDoc

/** This Component stores the Translation, Rotation and Scale of an Object in the Scene.
 * Through it its also possible to get and set the child transforms.
 * This {@link Component} is part of
 *
 * @author snoweuph
 * @version 1.0
 */
public class Transform extends Component {
    private Matrix4f transformationMatrix;
    public Transform(){
        transformationMatrix = new Matrix4f();
    }
    public Transform(Vector3f translation, Vector3f rotation, Vector3f scale){
        transformationMatrix = new Matrix4f();
        transformationMatrix.translate(translation);
        transformationMatrix.rotate((float) Math.toRadians(rotation.x % 360), new Vector3f(1, 0, 0));
        transformationMatrix.rotate((float) Math.toRadians(rotation.y % 360), new Vector3f(0, 1, 0));
        transformationMatrix.rotate((float) Math.toRadians(rotation.z % 360), new Vector3f(0, 0, 1));
        transformationMatrix.scale(scale);
    }
    public Transform(Transform transform) throws CloneNotSupportedException {
        transformationMatrix = new Matrix4f();
        transformationMatrix = (Matrix4f) transform.clone();
    }
    @Override
    protected List<Component> getRequiredComponents() {
        return new ArrayList<>();
    }
    //Getter
    public Transform getParent(){
        Entity entity = getEntity();
        Entity parentEntity = entity.getParent();
        if(parentEntity == null) return null;
        return (Transform) parentEntity.getComponent(Transform.class);
    }
    //INFO: This is Quite expensive
    public List<Transform> getChildren(){
        Entity entity = getEntity();
        List<Entity> children = entity.getChildren();
        if(children.size() == 0)return new ArrayList<>();
        List<Transform> childTransforms = new ArrayList<>();
        for(Entity child : children){
            Transform childTransform = (Transform) child.getComponent(Transform.class);
            if(childTransform == null)continue;
            childTransforms.add(childTransform);
        }
        return childTransforms;
    }
}
