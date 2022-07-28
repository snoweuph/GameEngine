package org.euph;

import org.euph.engine.entityComponentSystem.Entity;
import org.euph.engine.entityComponentSystem.components.Test2;
import org.euph.engine.entityComponentSystem.components.Test3;
import org.euph.engine.entityComponentSystem.components.TestComponent;
import org.euph.engine.entityComponentSystem.systems.display.DisplayManager;
import org.euph.engine.entityComponentSystem.systems.display.Window;
import org.euph.engine.util.ClassReflection;
import org.lwjgl.PointerBuffer;
import org.lwjgl.util.remotery.Remotery;
import org.lwjgl.util.remotery.RemoteryGL;
import org.reflections.Reflections;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    public static void main(String[] args) {

        //Initialize the Window Manager
        DisplayManager.init();

        //Profiler Setup
        PointerBuffer rmt_pointer = PointerBuffer.allocateDirect(1);
        Remotery.rmt_CreateGlobalInstance(rmt_pointer);
        RemoteryGL.rmt_BindOpenGL();

        //Create the Window
        Window win = DisplayManager.createWindow(1280, 720, 0, "Hello World", true, false, true, true);

        //Show the Window
        win.show(true);

        Entity e = new Entity();

        //run the Main Loop
        while (!win.shouldClose()){
            //Start Sampling for Profiler
            Remotery.rmt_BeginCPUSample("CPU Sampling", Remotery.RMTSF_Aggregate, null);
            //Run Main Loop
            mainLoop(win);

            e.putComponent(new TestComponent());
            e.putComponent(new Test2());
            e.putComponent(new Test3());

            Remotery.rmt_BeginCPUSample("Lib Sampling", Remotery.RMTSF_Aggregate, null);
            e.removeAllComponents(TestComponent.class);
            Remotery.rmt_EndCPUSample();

            //Log Delta Time to Profiler
            Remotery.rmt_LogText("Main Window Delta Time is:" + win.getDelta());
            //End Sampling
            Remotery.rmt_EndCPUSample();
        }

        //Cleanup everything after closing
        DisplayManager.cleanUp();
        Remotery.rmt_DestroyGlobalInstance(rmt_pointer.get(0));
    }

    public static void mainLoop(Window win) {
        //Update Inputs, Events and Callbacks
        glfwPollEvents();
        //Update the Window
        win.update();
    }
}