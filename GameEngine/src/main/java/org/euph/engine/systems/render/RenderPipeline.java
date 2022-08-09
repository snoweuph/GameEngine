package org.euph.engine.systems.render;

import org.euph.engine.Entity;
import org.euph.engine.components.renderer.Camera;

import java.util.List;

//TODO: this
//TODO: JavaDoc
public abstract class RenderPipeline {

    //Info:
    /* Each RenderPipline needs:
        - ShaderProgram -> some will have them on materials
        - a Function that will make it render stuff and return it -> WITH A CAMERA AS INPUT
     */

    public abstract void render(List<Entity> entities, List<Camera> cameras, List<Entity> lights, List<?>... args);
}
