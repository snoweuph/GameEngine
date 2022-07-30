package org.euph.engine.entityComponentSystem.components;

import org.euph.engine.entityComponentSystem.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

//TODO: this
//TODO: JavaDoc

/** This Component stores the Translation, Rotation and Scale of an Object in the Scene.
 * Through it its also possible to get and set the child transforms.
 * This {@link Component} is part of
 *
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
}
